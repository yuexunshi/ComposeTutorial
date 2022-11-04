package version.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @ClassName EasyUpload.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年10月25日 14:58:00
 */


class VersionGradle : Plugin<Project> {
    override fun apply(project: Project) {
    }
}

object Version {
    const val androidGradle = "4.1.3"
    const val kotlin = "1.8.10"
    const val kotlinxCoroutines = "1.5.0"
    const val aws = "2.25.0"
    const val junit = "4.12"
    const val truth = "1.1.3"
    const val mockitoKotlin = "3.2.0"
    const val robolectric = "4.5.1"
    const val androidxCore = "1.3.0"
    const val kotlinxSerialization = "1.2.2"
}

object Depends {
    const val kotlinxCoroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.kotlinxCoroutines}"
    const val kotlinxCoroutinesCoreTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.kotlinxCoroutines}"
    const val kotlinStdLibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.kotlin}"
    const val awsAndroidCore = "com.amazonaws:aws-android-sdk-core:${Version.aws}"
    const val awsAndroidAuth = "com.amazonaws:aws-android-sdk-auth-userpools:${Version.aws}"
    const val awsAndroidIot = "com.amazonaws:aws-android-sdk-iot:${Version.aws}"
    const val awsAndroidTestUtils = "com.amazonaws:aws-java-sdk-test-utils:1.12.9"
    const val androidxTestCore = "androidx.test:core:${Version.androidxCore}"
    const val androidxTestCoreKtx = "androidx.test:core-ktx:${Version.androidxCore}"
    const val junit = "junit:junit:${Version.junit}"
    const val truth = "com.google.truth:truth:${Version.truth}"
    const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:${Version.mockitoKotlin}"
    const val robolectric = "org.robolectric:robolectric:${Version.robolectric}"
}