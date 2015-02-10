name := """apps"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  cache,
  javaJpa.exclude("org.hibernate.javax.persistence", "hibernate-jpa-2.0-api"),
  javaJdbc,
  "org.hibernate" % "hibernate-entitymanager" % "4.3.8.Final",
  "mysql" % "mysql-connector-java" % "5.1.34",
  "com.cloudinary" % "cloudinary" % "1.0.8",
  "org.springframework.data" % "spring-data-elasticsearch" % "1.1.1.RELEASE",
  "org.elasticsearch" % "elasticsearch" % "1.4.2",
  "org.jsoup" % "jsoup" % "1.8.1"
)
