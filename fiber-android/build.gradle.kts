import java.net.URL
import java.io.FileOutputStream
plugins {
    id("com.android.library")
    id("maven-publish")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.b_labs.fiber_android"
    compileSdk = 33

    defaultConfig {
        minSdk = 28

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}
open class DownloadTokensJson : DefaultTask() {

    @TaskAction
    fun downloadFile() {
        val url =
            URL("https://raw.githubusercontent.com/Andrew2120/fiber-core/main/BTokens.kt")
        val outputDir = File("fiber-android/src/main/java/com/b_labs/fiber_android") // Specify the assets folder
        val outputFile = File(outputDir, "Tokens.kt")
        val connection = url.openConnection()
        connection.connect()
        val inputStream = connection.getInputStream()

        val outputStream = FileOutputStream(outputFile)
        val buffer = ByteArray(4096)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }

        inputStream.close()
        outputStream.close()
    }
}
tasks.register<DownloadTokensJson>("DownloadTokens")

tasks.getByPath("preBuild").dependsOn("DownloadTokens")

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.fiber"
                artifactId = "fiber-A"
                version = "1.2"
            }
        }
    }
}

dependencies {
    implementation("com.beust:klaxon:5.5")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}