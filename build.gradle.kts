plugins {
    kotlin("jvm") version "1.5.21"
    java
}

group = "com.github.minxyzgo"
version = "0.0.2"

repositories {
    maven{ url = uri("https://maven.aliyun.com/nexus/content/groups/public/")}
    maven{ url = uri("https://www.jitpack.io")}
   // jcenter()
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("com.github.RW-HPS.RW-HPS:Server:1.4.0-M1-DEV2")
    testImplementation("com.squareup.okhttp3:okhttp:4.9.1")
    testImplementation("io.netty:netty-all:4.1.66.Final")
    testImplementation("com.google.code.gson:gson:2.8.6")
    testImplementation("com.alibaba:fastjson:1.2.58")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.0")
}
