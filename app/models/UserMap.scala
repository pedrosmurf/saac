package models

import scala.slick._
import scala.slick.driver.PostgresDriver.simple._
import scala.slick.lifted.{ ProvenShape, ForeignKeyQuery }

case class UserMap(
  id: Option[Long],
  user: User,
  active: Boolean,
  requests: List[Request],
  workload: Long,
  validWorkload: Long,
  status: UserMapStatus)

object UserMap {
  val creditToSubmit = 12
  val hourToCredit = 17
}

case class UserMapTable(
  id: Option[Long],
  userEnrollment: String,
  active: Boolean,
  workload: Long,
  validWorkload: Long,
  status: String) {

  def toEntity = {

    UserMap(
      id,
      Users.findByEnrollment(userEnrollment),
      active,
      Requests.findByUserMapId(id),
      workload,
      validWorkload,
      UserMapStatus(status))
  }
}

class UserMaps(tag: Tag) extends Table[UserMapTable](tag, "user_maps") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def userEnrollment = column[String]("user_enrollment")
  def active = column[Boolean]("active")
  def workload = column[Long]("workload")
  def validWorkload = column[Long]("valid_workload")
  def status = column[String]("status")

  def user = foreignKey("user_fk", userEnrollment, Users.users)(_.enrollment)

  def * = (id.?, userEnrollment, active, workload, validWorkload, status) <> ((UserMapTable.apply _).tupled, UserMapTable.unapply)
}

object UserMaps {
  val userMaps = TableQuery[UserMaps]

  def createTable = {
    DB.connection.withSession {
      implicit session =>
        println("===================")
        for (s <- userMaps.ddl.createStatements.toList) yield (println(s))
    }
  }
  def insert(userMap: UserMap): Int = {
    DB.connection.withSession {
      implicit session =>
        userMaps.+=(toTable(userMap))
    }
  }

  def getIdByUserEnrollment(enrollment: String): Long = {
    DB.connection.withSession {
      implicit session =>
        val query = (for (um <- userMaps.filter(_.userEnrollment === enrollment)) yield (um)).first
        query.toEntity.id.get
    }
  }
  def findByUserEnrollment(enrollment: String): Option[UserMap] = {
    DB.connection.withSession {
      implicit session =>
        val query = (for (um <- userMaps.filter(_.userEnrollment === enrollment)) yield (um)).firstOption
        query match {
          case None => None
          case Some(um) => Some(um.toEntity)
        }
    }
  }
  def updateWorkload(id: Long): Boolean = {
    DB.connection.withSession {
      implicit session =>
        val query = (for (um <- userMaps.filter(_.id === id)) yield (um))
        query.firstOption match {
          case None => false
          case Some(userMap) =>
            val newWorkload = userMap.toEntity.requests.map(_.workload).fold(0L)((acc, x) => acc + x)
            query.update(userMap.copy(workload = newWorkload)) match {
              case 1L => true
              case _ => false
            }
        }
    }
  }

  def updateValidWorkload(id: Long): Boolean = {
    DB.connection.withSession {
      implicit session =>
        val query = (for (um <- userMaps.filter(_.id === id)) yield (um))
        query.firstOption match {
          case None => false
          case Some(userMap) =>
            val newValidWorkload = userMap.toEntity.requests.map(_.validWorkload).fold(0L)((acc, x) => acc + x)
            query.update(userMap.copy(validWorkload = newValidWorkload)) match {
              case 1L => true
              case _ => false
            }
        }
    }
  }
  def submit(id: Long): Boolean = {
    DB.connection.withSession {
      implicit session =>
        val userMap = get(id)
        val submitedUserMap = userMap.copy(status = MapSubmitted)
        update(submitedUserMap) match {
          case true =>
            for (request <- submitedUserMap.requests) yield (Requests.submit(request.id.getOrElse(0L)))
            true
          case false => false
        }
    }
  }

  def store(id: Long): Boolean = {
    DB.connection.withSession {
      implicit session =>
        val userMap = get(id)
        val completedUserMap = userMap.copy(status = MapCompleted)
        update(completedUserMap)
    }
  }
  def get(id: Long): UserMap = {
    DB.connection.withSession {
      implicit session =>
        val query = (for (um <- userMaps.filter(_.id === id)) yield (um)).first
        query.toEntity
    }
  }
  def update(userMap: UserMap): Boolean = {
    DB.connection.withSession {
      implicit session =>
        val query = (for (um <- userMaps.filter(_.id === userMap.id)) yield (um))
        query.update(toTable(userMap)) match {
          case 1L => true
          case _ => false
        }
    }
  }

  def listSubmitted: List[UserMap] = {
    DB.connection.withSession {
      implicit session =>
        val query = (for (um <- userMaps.filter(_.status === MapSubmitted.value)) yield (um)).list
        query.map(_.toEntity)
    }
  }
  private def toTable(userMap: UserMap) = {
    UserMapTable(
      userMap.id,
      userMap.user.enrollment,
      userMap.active,
      userMap.workload,
      userMap.validWorkload,
      userMap.status.value)
  }
}