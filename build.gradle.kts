import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.50"
    id("org.jmailen.kotlinter") version "1.26.0"
    id("org.jetbrains.dokka") version "0.9.18"
}

group = "max.keycloak.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    //Application dependency block

    dokkaRuntime("org.jetbrains.dokka:dokka-fatjar:0.9.18")

    compile(kotlin("stdlib-jdk8"))
    compile("args4j:args4j:2.33")

    val log4jVersion = "2.11.1"
    compile("org.apache.logging.log4j:log4j-core:$log4jVersion")
    compile("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")
    compile("io.github.microutils:kotlin-logging:1.6.26")

    compile("com.google.code.gson:gson:2.8.5")

    val fuelVersion = "2.2.1"
    compile("com.github.kittinunf.fuel:fuel:$fuelVersion")
    compile("com.github.kittinunf.fuel:fuel-gson:$fuelVersion")

    compile("com.auth0:java-jwt:3.8.1")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")
    compile("commons-codec:commons-codec:1.12")
    compile("com.fatboyindustrial.gson-javatime-serialisers:gson-javatime-serialisers:1.1.1")
}

dependencies {
    //Core framework dependency block
    val jerseyVersion = "2.28"
    compile("org.glassfish.jersey.core:jersey-common:$jerseyVersion")
    compile("org.glassfish.jersey.inject:jersey-hk2:$jerseyVersion")
    compile("org.glassfish.jersey.containers:jersey-container-servlet-core:$jerseyVersion")
    compile("org.glassfish.jersey.containers:jersey-container-jetty-servlet:$jerseyVersion")
    // Remove jersey-media-json-jackson production
    compile("org.glassfish.jersey.media:jersey-media-json-jackson:$jerseyVersion")
    testImplementation("org.glassfish.jersey.test-framework:jersey-test-framework-core:$jerseyVersion")
    testImplementation("org.glassfish.jersey.test-framework.providers:jersey-test-framework-provider-grizzly2:$jerseyVersion")

    val jettyVersion = "9.4.18.v20190429"
    compile("org.eclipse.jetty:jetty-server:$jettyVersion")
    compile("org.eclipse.jetty:jetty-servlet:$jettyVersion")
    compile("org.eclipse.jetty:jetty-http:$jettyVersion")
    compile("org.eclipse.jetty:jetty-webapp:$jettyVersion")
    compile("org.eclipse.jetty:jetty-annotations:$jettyVersion")
    compile("org.eclipse.jetty:apache-jsp:$jettyVersion")

    compile("jstl:jstl:1.2")

    compile("org.keycloak:keycloak-admin-client:7.0.0")

    val scribeVersion = "6.8.1"
    compile("com.github.scribejava:scribejava-apis:$scribeVersion")
    compile("com.github.scribejava:scribejava-core:$scribeVersion")

    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:2.28.2")
    testImplementation("org.amshove.kluent:kluent:1.53")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allJava)
}

tasks.named<Jar>("jar") {
    configurations.compileClasspath.get().forEach { if (it.isDirectory) from(it) else from(zipTree(it)) }

    this.archiveName = "${project.name}.jar"
    this.destinationDir = file("$rootDir/build/bin")

    manifest { attributes["Main-Class"] = "max.keycloak.example.Main" }

    exclude(
        "META-INF/*.RSA",
        "META-INF/*.SF",
        "META-INF/*.DSA",
        "META-INF/DEPENDENCIES",
        "META-INF/NOTICE*",
        "META-INF/LICENSE*",
        "about.html"
    )
}
tasks.dokka {
    outputFormat = "javadoc"
    outputDirectory = "$buildDir/javadoc"
}
