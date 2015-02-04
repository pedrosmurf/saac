package models

import scala.slick.jdbc.JdbcBackend.Database._
import scala.slick.driver.PostgresDriver.simple._

object DB {
  val connection = Database.forURL(
    url = "jdbc:postgres://flvdzjblusialy:xqilpKZH8rsBPMXnSVR-JXUOcV@ec2-54-235-193-41.compute-1.amazonaws.com:5432/dl94q4dnrenfr",
    user = "flvdzjblusialy",
    password = "",
    driver = "org.postgresql.Driver")
}