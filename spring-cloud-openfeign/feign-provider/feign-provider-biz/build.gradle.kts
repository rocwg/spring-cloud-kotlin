dependencies {
    implementation(project(":spring-cloud-openfeign:feign-provider:feign-provider-api"))
    implementation("mysql:mysql-connector-java")
    implementation("com.baomidou:mybatis-plus-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
