/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 */
package org.jenetics.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar

import org.apache.tools.ant.filters.ReplaceTokens

import org.jenetics.gradle.Version

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @since @__version__@
 * @version @__version__@ &mdash; <em>$Date: 2013-10-26 $</em>
 */
class PackagingPlugin implements Plugin<Project> {

	private static final String TASK_NAME_JARJAR = 'jarjar'
	private static final String TASK_NAME_PACKAGING = 'packaging'

	private final Calendar now = Calendar.getInstance()
	private final int year = now.get(Calendar.YEAR)
	private final String copyrightYear = "2007-${year}"

	private Project project = null
	private String identifier = null
	private String version = null

	private File buildDir = null
	private File exportDir = null
	private File exportProjectDir = null
	private File exportProjectLibDir = null
	private File exportLibDir = null
	private File exportJavadocDir = null
	private File exportReportDir = null
	private File exportScriptDir = null

	private def textContentReplacements = [:]

	@Override
	void apply(final Project project) {
		this.project = project
		version = project.rootProject.version
		identifier = "${project.rootProject.name}-${version}"

		buildDir = project.rootProject.buildDir
		exportDir = new File("${buildDir}/package/${identifier}")
		exportProjectDir = new File("${exportDir}/project")
		exportProjectLibDir = new File("${exportProjectDir}/buildSrc/lib")
		exportLibDir = new File("${exportDir}/lib")
		exportJavadocDir = new File("${exportDir}/javadoc")
		exportReportDir = new File("${exportDir}/report")
		exportScriptDir = new File("${exportDir}/script")

		textContentReplacements = [
			__identifier__: identifier,
			__year__: copyrightYear
		]

		project.extensions.create('packaging', PackagingPluginExtension)

		if (project.plugins.hasPlugin('java')) {
			jarjar()
		}
		packaging()
	}

	private boolean isRootProject() {
		return project == project.rootProject
	}

	private void jarjar() {
		project.task(TASK_NAME_JARJAR, type: Jar, dependsOn: 'jar') {
			baseName = "${project.name}-all"

			from project.files(project.sourceSets.main.output.classesDir)
			from {
				project.configurations.compile.collect {
					it.isDirectory() ? it : project.zipTree(it)
				}
			}

			manifest {
				attributes(
					'Implementation-Title': "${project.name}-all",
					'Implementation-Versionv': project.version,
					'Implementation-URL': project.packaging.url,
					'Implementation-Vendor': project.packaging.author,
					'ProjectName': project.packaging.name,
					'Version': project.version,
					'Maintainer': project.packaging.author
				)
			}
		}
	}

	private void packaging() {
		def dependencies = []
		if (project.tasks.findByPath(TASK_NAME_JARJAR) != null) {
			dependencies += TASK_NAME_JARJAR
		}
		if (project.tasks.findByPath('build') != null) {
			dependencies += 'build'
		}
		if (project.tasks.findByPath('jarjar') != null) {
			dependencies += 'jarjar'
		}
		if (project.tasks.findByPath('javadoc') != null) {
			dependencies += 'javadoc'
		}

		project.task(TASK_NAME_PACKAGING, dependsOn: dependencies) {
			ext {
				identifier = this.identifier
				exportDir = this.exportDir
				exportProjectDir = this.exportProjectDir
				exportProjectLibDir = this.exportProjectLibDir
				exportLibDir = this.exportLibDir
				exportJavadocDir = this.exportJavadocDir
				exportReportDir = this.exportReportDir
				exportScriptDir = this.exportScriptDir
			}

			doLast {
				if (isRootProject()) {
					copyRoot()
				} else {
					copy()
				}
			}
		}

		// Copy the test-reports
		if (project.tasks.findByPath('test') != null) {
			project.tasks.findByPath('test').doLast {
				project.copy {
					from("${project.buildDir}/reports") {
						include 'tests/**'
						include 'jacoco/**'
						include '*.gradle'
						exclude '.gradle'
					}
					into exportReportDir
				}
			}
		}

		// Copy the javadoc.
		if (project.tasks.findByPath('javadoc') != null) {
			project.tasks.findByPath('javadoc').doLast {
				copyDir(
					new File(project.buildDir, 'docs/javadoc'),
					project.name,
					exportJavadocDir
				)
			}
		}

		// Copy the pdf manual.
		if (project.tasks.findByPath('lyx') != null) {
			project.tasks.findByPath('build').doLast {
				project.copy {
					from("${project.buildDir}/doc") {
						include '*.pdf'
					}
					into exportDir
				}
			}
		}


		if (project.plugins.hasPlugin('java')) {
			project.tasks.findByPath('build').doLast {
				// Copy the external jar dependencies.
				project.configurations.testRuntime.each { jar ->
					if (jar.name.endsWith('.jar') &&
						!jar.name.startsWith('org.jeneti'))
					{
						project.copy {
							from jar
							into exportProjectLibDir
						}
					}
				}
			}

			project.tasks.findByPath('jarjar').doLast {
				// Copy the build library
				project.copy {
					from("${project.buildDir}/libs")
					into exportLibDir
				}
			}
		}

	}

	private void copy() {
		copyDir(project.projectDir, exportProjectDir)
	}

	private void copyRoot() {
		// Copy the files in the root directory.
		project.copy {
			from('.') {
				include '*'
				excludes = IGNORED_FILES
			}
			includeEmptyDirs = false
			into exportProjectDir
			filter(ReplaceTokens, tokens: textContentReplacements)
		}

		copyDir(new File('gradle'), exportProjectDir)
		copyDir(new File('buildSrc'), exportProjectDir)
	}

	private void copyDir(final File source, final File target) {
		copyDir(source, source.name, target)
	}

	private void copyDir(final File source, final String sinto, final File target) {
		// Copy the text files with text pattern replacement.
		project.copy {
			from(source.absoluteFile) {
				includes = TEXT_FILE_PATTERN
				excludes = IGNORED_FILES
				into sinto
			}
			includeEmptyDirs = false
			into target
			filter(ReplaceTokens, tokens: textContentReplacements)
		}

		// Copy the rest, without replacement.
		project.copy {
			from(source.absoluteFile) {
				excludes = TEXT_FILE_PATTERN + IGNORED_FILES
				into sinto
			}
			includeEmptyDirs = false
			into target
		}
	}

	private static final def IGNORED_FILES = [
		'bin/**',
		'build/**',
		'build.xml',
		'.classpath',
		'*.dblite',
		'.gradle/**',
		'gradle-app.setting',
		'.groovy/*',
		'.hgrc',
		'*.iml',
		'*.ipr',
		'*.iws',
		'manifest.mf',
		'nbbuild/**',
		'.nb-gradle/**',
		'nbproject/**',
		'out/**',
		'.project',
		'random-x86_64',
		'.settings/**',
		'*.so',
		'test-output/**',
		'wiki/**'
	]

	private static final def TEXT_FILE_PATTERN = [
		'**/*.bat',
		'**/*.c',
		'**/*.cpp',
		'**/*.gradle',
		'**/*.groovy',
		'**/*.h',
		'**/*.hpp',
		'**/*.html',
		'**/*.java',
		'**/*.log',
		'**/*.lyx',
		'**/*.md',
		'**/*.properties',
		'**/*.sh',
		'**/*.txt',
		'**/*.xml'
	]

}

class PackagingPluginExtension {
	String name = 'Jenetics'
	String author = 'Franz Wilhelmstötter'
	String url = 'http://jenetics.sourceforge.net'
}

