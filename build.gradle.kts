import java.time.Duration
import java.util.Base64

plugins {
    id("com.android.library") version "8.2.0"
    id("org.jetbrains.kotlin.android") version "1.9.20"
    id("maven-publish")
    id("signing")
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
}

android {
    namespace = "com.manageupgrades"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        targetSdk = 34
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            
            // Read credentials from gradle.properties or environment variables
            username.set(System.getenv("SONATYPE_USERNAME") ?: findProperty("sonatype.username") as String?)
            password.set(System.getenv("SONATYPE_PASSWORD") ?: findProperty("sonatype.password") as String?)
        }
    }
    connectTimeout.set(Duration.ofMinutes(3))
    clientTimeout.set(Duration.ofMinutes(3))
    transitionCheckOptions {
        maxRetries.set(60)
        delayBetween.set(Duration.ofSeconds(10))
    }
}

signing {
    val signingKey = System.getenv("SIGNING_KEY") ?: findProperty("signing.key") as String?
    val signingPassword = System.getenv("SIGNING_PASSWORD") ?: findProperty("signing.password") as String?
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.github.abhilashusha"
                artifactId = "manageupgrades-android"
                version = "1.0.0"
                
                from(components["release"])

                pom {
                    name.set("ManageUpgrade Android")
                    description.set("Android library for managing app updates and maintenance mode")
                    url.set("https://github.com/abhilashusha/manageupgrades-android")
                    
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    
                    developers {
                        developer {
                            id.set("abhilashusha")
                            name.set("Abhilash Usha")
                            email.set("abhilash.usha@gmail.com")
                        }
                    }
                    
                    scm {
                        connection.set("scm:git:git://github.com/abhilashusha/manageupgrades-android.git")
                        developerConnection.set("scm:git:ssh://github.com/abhilashusha/manageupgrades-android.git")
                        url.set("https://github.com/abhilashusha/manageupgrades-android")
                    }
                }
            }
        }
    }
}
