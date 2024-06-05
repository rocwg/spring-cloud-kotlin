plugins {
    id("com.google.devtools.ksp")
}

dependencies {
    implementation("com.google.guava:guava")
    compileOnly("io.github.livk-cloud:spring-auto-service")
    implementation("com.google.devtools.ksp:symbol-processing-api:2.0.0-1.0.21")
    implementation("com.squareup:kotlinpoet:1.16.0")
}