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

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 1.2
 * @version 6.1
 */
plugins {
	`java-library`
	idea
	`maven-publish`
	id("me.champeau.gradle.jmh")
}

description = "Jenetics - Java Genetic Algorithm Library"

extra["moduleName"] = "io.jenetics.base"

dependencies {
	testImplementation(Libs.ApacheCommonsMath)
	testImplementation(Libs.TestNG)
	testImplementation(Libs.EqualsVerifier)
	testImplementation(Libs.PRNGine)

	jmh(Libs.PRNGine)
	jmh("org.openjdk.jmh:jmh-core:1.23")
	jmh("org.openjdk.jmh:jmh-generator-annprocess:1.23")
}

tasks.test { dependsOn(tasks.compileJmhJava) }

jmh {
	include = listOf(".*BitArrayPerf.*")
}