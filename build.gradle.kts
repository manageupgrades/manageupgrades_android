buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
    }
}

apply(plugin = "com.android.library")
apply(plugin = "kotlin-android")
apply(plugin = "maven-publish")
apply(plugin = "signing")
apply(plugin = "io.github.gradle-nexus.publish-plugin" version "1.3.0")

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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



afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.github.abhilashusha"
                artifactId = "manageupgrades_android"
                version = "1.0.3"
                
                from(components["release"])

                pom {
                    name.set("ManageUpgrade Android")
                    description.set("Android library for managing app updates and maintenance mode")
                    url.set("https://github.com/abhilashusha/manageupgrades_android")
                    
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
                        connection.set("scm:git:git://github.com/abhilashusha/manageupgrades_android.git")
                        developerConnection.set("scm:git:ssh://github.com/abhilashusha/manageupgrades_android.git")
                        url.set("https://github.com/abhilashusha/manageupgrades_android")
                    }
                }
            }
        }
    }
}
