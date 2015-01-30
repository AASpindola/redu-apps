name := """apps"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  cache,
  javaJpa.exclude("org.hibernate.javax.persistence", "hibernate-jpa-2.0-api"),
  javaJdbc,
  "org.hibernate" % "hibernate-entitymanager" % "4.3.8.Final",
  "mysql" % "mysql-connector-java" % "5.1.34"
)
