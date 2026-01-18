// Root build.gradle.kts
plugins {
    // Android Application Plugin - Must be 8.1.0 or higher for Gradle 8.2
    id("com.android.application") version "8.1.1" apply false
    
    // Android Library Plugin - Match version above
    id("com.android.library") version "8.1.1" apply false
    
    // Kotlin Plugin - Must be 1.9.0 or higher
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    
    // Firebase Plugin
    id("com.google.gms.google-services") version "4.4.1" apply false
}