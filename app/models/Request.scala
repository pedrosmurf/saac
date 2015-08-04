package models

import scala.slick._
import scala.slick.driver.PostgresDriver.simple._
import scala.slick.lifted.{ ProvenShape, ForeignKeyQuery }

case class Request(
  id: Option[Long],
  status: Status,
  user: User,
  activity: Activity,
  event: String,
  description: String,
  participation: String,
  institution: String,
  period: String,
  workload: Long,
  validWorkload: Long,
  document: String,
  comment: String)

case class RequestTable(
  id: Option[Long],
  status: String,
  userEnrollment: String,
  activity: String,
  event: String,
  description: String,
  participation: String,
  institution: String,
  period: String,
  workload: Long,
  validWorkload: Long,
  document: String,
  comment: String,
  userMapId: Long) {

  def toEntity = {

    Request(
      id,
      Status(status),
      Users.findByEnrollment(userEnrollment),
      Activity(activity),
      event,
      description,
      participation,
      institution,
      period,
      workload,
      validWorkload,
      document,
      comment)
  }
}

class Requests(tag: Tag) extends Table[RequestTable](tag, "requests") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def status = column[String]("status")
  def userEnrollment = column[String]("user_enrollment")
  def activity = column[String]("activity")
  def event = column[String]("event")
  def description = column[String]("description")
  def participation = column[String]("participation")
  def institution = column[String]("institution")
  def period = column[String]("period")
  def workload = column[Long]("workload")
  def validWorkload = column[Long]("valid_workload")
  def document = column[String]("document")
  def comment = column[String]("comment")
  def userMapId = column[Long]("user_map_id")

  def user = foreignKey("user_fk", userEnrollment, Users.users)(_.enrollment, onDelete = ForeignKeyAction.Cascade)
  def userMap = foreignKey("userMap_fk", userMapId, UserMaps.userMaps)(_.id, onDelete = ForeignKeyAction.Cascade)

  def * = (id.?, status, userEnrollment, activity, event, description, participation, institution, period, workload, validWorkload, document, comment, userMapId) <> ((RequestTable.apply _).tupled, RequestTable.unapply)
}

object Requests {
  val requests = TableQuery[Requests]

  def createTable = {
    DB.connection.withSession {
      implicit session =>
        println("===================")
        for (s <- requests.ddl.createStatements.toList) yield (println(s))
    }
  }

  def create(request: Request): Boolean = {
    DB.connection.withSession {
      implicit session =>
        val createdRequest = toTable(request).copy(status = Created.value)
        requests.+=(createdRequest) match {
          case 1 =>
            UserMaps.updateWorkload(createdRequest.userMapId)
            true
          case _ => false
        }
    }
  }

  def update(request: Request): Boolean = {
    DB.connection.withSession {
      implicit session =>
        val updateRequest = toTable(request)
        val query = for (r <- requests.filter(_.id === updateRequest.id)) yield (r)
        query.update(updateRequest) match {
          case 1 =>
            UserMaps.updateWorkload(updateRequest.userMapId)
            true
          case _ => false
        }
    }
  }

  def listByStudent(enrollment: String): List[Request] = {
    DB.connection.withSession {
      implicit session =>
        val query = (for (r <- requests.filter(request => request.userEnrollment === enrollment)) yield (r)).list
        query.map(_.toEntity)
    }
  }

  def listByTeacher(enrollment: String): List[Request] = {
    DB.connection.withSession {
      implicit session =>
        val query = (for (
          r <- requests.filter(
            request =>
              request.status === Submitted.value ||
                request.status === Rejected.value ||
                request.status === Corrected.value ||
                request.status === Evaluated.value)
        ) yield (r)).list
        query.map(_.toEntity)
    }
  }

  def listAll: List[Request] = {
    DB.connection.withSession {
      implicit session =>
        val query = (for (r <- requests) yield (r)).list
        query.map(_.toEntity)
    }
  }

  def get(id: Long): Request = {
    DB.connection.withSession {
      implicit session =>
        val query = (for (r <- requests.filter(_.id === id)) yield (r)).first
        query.toEntity
    }
  }

  def findByUserMapId(id: Option[Long]): List[Request] = {
    DB.connection.withSession {
      implicit session =>
        id match {
          case None => Nil
          case Some(userMapId) =>
            val query = (for (r <- requests.filter(_.userMapId === userMapId)) yield (r)).list
            query.map(_.toEntity)
        }
    }
  }

  def submit(id: Long): Boolean = {
    DB.connection.withSession {
      implicit session =>
        val req = Requests.get(id)
        val requestUpdated = req.copy(status = Submitted)
        Requests.update(requestUpdated)
    }
  }

  def complet(id: Long): Boolean = {
    DB.connection.withSession {
      implicit session =>
        val req = Requests.get(id)
        val requestUpdated = req.copy(status = Completed)
        Requests.update(requestUpdated)
    }
  }

  def correct(id: Long): Boolean = {
    DB.connection.withSession {
      implicit session =>
        val req = Requests.get(id)
        val requestUpdated = req.copy(status = Corrected)
        Requests.update(requestUpdated)
    }
  }

  def evaluat(id: Long): Boolean = {
    DB.connection.withSession {
      implicit session =>
        val req = Requests.get(id)
        val requestUpdated = req.copy(status = Evaluated, validWorkload = req.workload)
        Requests.update(requestUpdated) match {
          case false => false
          case true =>
            UserMaps.updateValidWorkload(toTable(requestUpdated).userMapId)
            true
        }
    }
  }

  private def toTable(request: Request) = {
    RequestTable(
      request.id,
      request.status.value,
      request.user.enrollment,
      request.activity.value,
      request.event,
      request.description,
      request.participation,
      request.institution,
      request.period,
      request.workload,
      request.validWorkload,
      request.document,
      request.comment,
      UserMaps.getIdByUserEnrollment(request.user.enrollment))
  }
}