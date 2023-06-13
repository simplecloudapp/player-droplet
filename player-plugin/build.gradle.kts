subprojects {
    repositories {
        mavenCentral()
        maven {
            name = "papermc"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
    }
}