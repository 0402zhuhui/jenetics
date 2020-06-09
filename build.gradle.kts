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
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */


import io.jenetics.gradle.Version
import io.jenetics.gradle.plugin.SetupPlugin

import java.time.Year
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 1.2
 * @version !__version__!
 */
plugins {
	signing
	packaging
}

rootProject.version = Jenetics.Version

allprojects {
	group =  Jenetics.Group
	version = Jenetics.Version

	repositories {
		flatDir {
			dirs("${rootDir}/buildSrc/lib")
		}
		mavenCentral()
	}
}

/*
 * Project configuration *after* the projects has been evaluated.
 */
gradle.projectsEvaluated {
	subprojects {
		plugins.apply(SetupPlugin::class)
	}
}

tasks.register<Zip>("zip") {
	val identifier = "${Jenetics.Name}-${Jenetics.Version}"

	from("build/package/${identifier}") {
		into(identifier)
	}

	archiveBaseName.set(identifier)
	archiveVersion.set(Jenetics.Version)

	doLast {
		val zip = file("${identifier}.zip")
		zip.renameTo(file("build/package${zip.name}"))
	}
}

//ext {
//	set("javaVersion", property("build.JavaVersion"))
//	set("now", ZonedDateTime.now())
//	set("year", Year.now())
//	//set("copyrightYear", "2007-${year}")
//	set("identifier", "${rootProject.name}-${version}")
//	set("manualDate", DateTimeFormatter.ofPattern("yyyy/MM/dd").format(ZonedDateTime.now()))
//	set("manualIdentifier", "${version}—${extra["manualDate"]}")
//	set("dateformat", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
//}



//subprojects { Project prj ->
//	prj.plugins.apply(SetupPlugin.class)
//}
//
//allprojects { Project prj ->
//	repositories {
//		mavenCentral()
//		jcenter()
//	}
//
//	if (prj.plugins.hasPlugin("java")) {
//		sourceCompatibility = javaVersion
//		targetCompatibility = javaVersion
//
//		jar {
//			manifest {
//				attributes(
//					"Implementation-Title": prj.name,
//					"Implementation-Version": prj.version,
//					"Implementation-URL": project.property("jenetics.Url"),
//					"Implementation-Vendor": project.property("jenetics.Name"),
//					"ProjectName": project.property("jenetics.Name"),
//					"Version": project.version,
//					"Maintainer": project.property("jenetics.Author"),
//					"Project": prj.name,
//					"Project-Version": prj.version,
//					"Built-By": System.properties["user.name"],
//					"Build-Timestamp": dateformat.format(now),
//					"Created-By": "Gradle ${gradle.gradleVersion}",
//					"Build-Jdk": "${System.properties["java.vm.name"]} " +
//						"(${System.properties["java.vm.vendor"]} " +
//						"${System.properties["java.vm.version"]})",
//					"Build-OS": "${System.properties["os.name"]} " +
//						"${System.properties["os.arch"]} " +
//						"${System.properties["os.version"]}"
//				)
//			}
//		}
//	}
//	if (prj.plugins.hasPlugin("jacoco")) {
//		prj.jacoco {
//			toolVersion = "0.8.5"
//		}
//	}
//}
//
//task updateGradle(type: Wrapper) {
//	gradleVersion = project.property("build.GradleVersion")
//}
//
//rootProject.gradle.projectsEvaluated {
//	def PROJECTS = [
//		"jenetics",
//		"jenetics.ext",
//		"jenetics.prog",
//		"jenetics.xml"
//	]
//
//	final Set<Project> projects = project.subprojects
//		.findAll { prj -> PROJECTS.contains(prj.name) }
//
//	if (!projects.isEmpty()) {
//		rootProject.task("alljavadoc", type: Javadoc) {
//			description = "Aggregates Javadoc API documentation of all subprojects."
//			group = JavaBasePlugin.DOCUMENTATION_GROUP
//
//			source = projects.javadoc.source
//			exclude "**/internal/**"
//			destinationDir rootProject.file("$project.buildDir/docs/javadoc")
//			classpath = rootProject.files(projects.javadoc.classpath)
//			title = "${project.name} documentation"
//
//			options {
//				memberLevel = JavadocMemberLevel.PROTECTED
//				version = true
//				author = true
//				docEncoding = "UTF-8"
//				charSet = "UTF-8"
//				windowTitle = "${project.name} ${project.version}"
//				docTitle = "<h1>${project.name} ${project.version}</h1>"
//
//				tags "apiNote:a:API Note:",
//					"implSpec:a:Implementation Requirements:",
//					"implNote:a:Implementation Note:"
//
//				links "https://docs.oracle.com/en/java/javase/11/docs/api"
//			}
//		}
//	}
//}
//
//// Create a zip file from the export directory.
//task zip(type: Zip) {
//	from("build/package/${identifier}") {
//		into identifier
//	}
//
//	archiveBaseName = rootProject.name
//	version = project.property("jenetics.Version")
//
//	doLast {
//		def zip = file("${identifier}.zip")
//		zip.renameTo(new File("build/package", zip.getName()))
//	}
//}
