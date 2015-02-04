package models

import scala.slick._
import scala.slick.driver.PostgresDriver.simple._
import scala.slick.lifted.{ ProvenShape, ForeignKeyQuery }

case class User(enrollment: String, password: String, name: String, role: Role)

case class UserTable(enrollment: String, password: String, name: String, role: String) {
  def toEntity = {
    User(enrollment, password, name, Role(role))
  }
}

class Users(tag: Tag) extends Table[UserTable](tag, "users") {
  def enrollment = column[String]("enrollment", O.PrimaryKey)
  def password = column[String]("password")
  def name = column[String]("name")
  def role = column[String]("role")

  def * = (enrollment, password, name, role) <> ((UserTable.apply _).tupled, UserTable.unapply)
}

object Users {
  val users = TableQuery[Users]

  def createTable = {
    DB.connection.withSession {
      implicit session =>
        for (s <- users.ddl.createStatements.toList) yield (println(s))
    }
  }

  def insert(user: User): Int = {
    DB.connection.withSession {
      implicit session =>
        users.+=(toTable(user))
    }
  }

  def authenticate(user: User): Option[User] = {
    DB.connection.withSession {
      implicit session =>
        val query = (for (u <- users.filter(u => u.enrollment === user.enrollment && u.password === user.password)) yield (u)).firstOption
        query match {
          case None => None
          case Some(userTable) =>
            Some(userTable.toEntity)
        }
    }
  }

  def authenticate(enrollment: String, password: String): Option[User] = {
    DB.connection.withSession {
      implicit session =>
        val query = (for (u <- users.filter(u => u.enrollment === enrollment && u.password === password)) yield (u)).firstOption
        query match {
          case None => None
          case Some(userTable) =>
            Some(userTable.toEntity)
        }
    }
  }

  def findByEnrollment(enrollment: String): User = {
    DB.connection.withSession {
      implicit session =>
        users.filter(_.enrollment === enrollment).first.toEntity
    }
  }

  private def toTable(user: User) = {
    UserTable(user.enrollment, user.password, user.name, user.role.value)
  }

}