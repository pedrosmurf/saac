package models

import scala.slick.jdbc.JdbcBackend.Database._
import scala.slick.driver.PostgresDriver.simple._

//object DB {
//  val connection = Database.forURL(url = "jdbc:postgresql://ec2-54-235-193-41.compute-1.amazonaws.com:5432/dl94q4dnrenfr",
//    user = "flvdzjblusialy",
//    password = "xqilpKZH8rsBPMXnSVR-JXUOcV",
//    driver = "org.postgresql.Driver")
//}

object DB {
  val connection = Database.forURL(url = "jdbc:postgresql://localhost:5432/saac",
    user = "saac",
    password = "544c",
    driver = "org.postgresql.Driver")
}