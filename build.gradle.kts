plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.power-assert") version "2.2.21"
    `jvm-test-suite`
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    jvmToolchain(21)
    sourceSets.all {
        languageSettings { languageVersion = "2.2" }
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}

//tasks.register<Exec>("doBeep") {
//    commandLine("afplay", "/System/Library/CoreServices/PowerChime.app/Contents/Resources/connect_power.aif")
//}
//tasks.test { finalizedBy("doBeep") }

//tasks {
//    test {
//        testLogging {
//            events("passed", "skipped", "failed")
//        }
//    }
//}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
}