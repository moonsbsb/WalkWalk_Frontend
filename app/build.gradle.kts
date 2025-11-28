import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "2.0.21"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
    id("com.google.devtools.ksp") version "2.0.21-1.0.27"
}
val secret = Properties()
val localPropertyFile = project.rootProject.file("local.properties")
if(localPropertyFile.exists()){
    localPropertyFile.inputStream().use { secret.load(it) }
}
android {
    namespace = "com.withwalk.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.withwalk.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 8
        versionName = "1.0.8"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = secret.getProperty("KAKAO_NATIVE_APP_KEY") ?: ""
        manifestPlaceholders["KAKAO_SDK_APP_KEY"] = secret.getProperty("KAKAO_SDK_APP_KEY") ?: ""

        buildConfigField("String", "KAKAO_SDK_APP_KEY", "${secret.getProperty("KAKAO_SDK_APP_KEY")}")
    }
    signingConfigs {
        create("release") {
            storeFile = file("/Users/moon/Android_app_key/app_key.jks")
            storePassword = secret.getProperty("MY_STORE_PASSWORD")
            keyAlias = "app_key_alias"
            keyPassword = secret.getProperty("MY_KEY_PASSWORD")
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
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

    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.1"
    }

    packaging {
        resources {
            pickFirsts += listOf(
                "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
            )
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity-ktx:1.10.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Compose
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.0")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    val nav_version = "2.9.3"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // 테스트
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // 코루틴
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    //데이트 피커
    implementation("androidx.compose.material3:material3:1.3.2")
    // 상태바
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.27.0")

    // 기타 라이브러리
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.kakao.maps.open:android:2.12.8")
    implementation("com.kakao.sdk:v2-user:2.19.0")
    implementation("com.kakao.sdk:v2-share:2.19.0")
    implementation("com.kakao.sdk:v2-talk:2.19.0")
    implementation("com.kakao.sdk:v2-friend:2.19.0")
    implementation("com.kakao.sdk:v2-navi:2.19.0")
    implementation("com.kakao.sdk:v2-cert:2.19.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")
}
