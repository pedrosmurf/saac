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

  def save(user: User): Boolean = {
    DB.connection.withSession {
      implicit session =>
        user.role match {
          case Teacher =>
            val userTable = toTable(user)
            users.+=(userTable) match {
              case 1 => true
              case _ => false
            }
          case Student =>
            val userTable = toTable(user)
            users.+=(userTable) match {
              case 1 =>
                UserMaps.create(UserMap(None, user, true, Nil, 0, 0, MapCreated))
                true
              case _ => false
            }
          case _ =>
            false
        }
    }
  }

  def update(user: User): Boolean = {
    DB.connection.withSession {
      implicit session =>
        val updateUser = toTable(user)
        val query = for (u <- users.filter(_.enrollment === updateUser.enrollment)) yield (u)
        query.update(updateUser) match {
          case 1 => true
          case _ => false
        }
    }
  }

  def remove(enrollment: String): Boolean = {
    DB.connection.withSession {
      implicit session =>
        (for (u <- users.filter(_.enrollment === enrollment)) yield (u))delete match {
          case 1 => true
          case _ => false
        }
    }
  }

  def listAll: List[User] = {
    DB.connection.withSession {
      implicit session =>
        val query = (for (u <- users) yield (u)).list
        query.map(_.toEntity)
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