package models

import scala.slick.jdbc.JdbcBackend.Database._
import scala.slick.driver.PostgresDriver.simple._

object DB{
  val connection = Database.forURL(url = "jdbc:postgresql://localhost:5432/saac",user = "saac",password = "s44c", driver="org.postgresql.Driver")
}