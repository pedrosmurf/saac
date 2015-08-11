package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms.{ mapping, nonEmptyText, number, text, list, optional, longNumber, tuple }
import models.{ Kind, Users }
import models.Requests
import java.io._
import models._
import play.api.libs.json._

object StudentController extends SaacController {

  val createForm = Form(tuple(
    "id" -> optional(longNumber),
    "activity" -> nonEmptyText,
    "event" -> nonEmptyText,
    "description" -> nonEmptyText,
    "participation" -> nonEmptyText,
    "institution" -> nonEmptyText,
    "period" -> nonEmptyText,
    "workload" -> longNumber))

  def create = Authenticated {
    implicit request =>

      val kinds = List(Teaching, Research, General, Extension, Sport, Cultural)

      Ok(views.html.request.create(createForm, kinds))
  }

  def listByStudent = Authenticated {
    implicit request =>

      val enrollment = request.session("User")
      val requests = Requests.listByStudent(enrollment)
      val userMap = UserMaps.findByUserEnrollment(enrollment)

      Ok(views.html.request.list(requests, userMap))
  }
  def listByTeacher = Authenticated {
    implicit request =>

      val enrollment = request.session("User")
      val requests = Requests.listByTeacher(enrollment)

      Ok(views.html.request.list(requests, None))
  }

  def listAll = Authenticated {
    implicit request =>

      val requests = Requests.listAll

      Ok(views.html.request.list(requests, None))
  }

  def edit(id: Long) = Authenticated {
    implicit request =>

      val req = Requests.get(id)
      val kinds = List(Teaching, Research, General, Extension, Sport, Cultural)

      Ok(views.html.request.edit(req, createForm.fill((req.id, req.activity.value, req.event, req.description, req.participation, req.institution, req.period, req.workload)), kinds))
  }

  def view(id: Long) = Authenticated {
    implicit request =>

      val req = Requests.get(id)

      Ok(views.html.request.view(req))
  }

  def save = Authenticated(parse.multipartFormData) {
    implicit request =>
      createForm.bindFromRequest.fold(
        formWithErrors => {
          Redirect(routes.ApplicationController.index).flashing("message" -> "save.error", "type" -> "error")
        },
        form => {
          request.body.file("document").map { document =>

            val filename = document.filename
            val userEnrollment: String = request.session("User")

            val re = models.Request(
              None,
              models.Void,
              Users.findByEnrollment(userEnrollment),
              Activity(form._2),
              form._3,
              form._4,
              form._5,
              form._6,
              form._7,
              form._8,
              0,
              filename,
              "")

            if (Requests.create(re)) {
              val file = new File(s"files/$userEnrollment/$filename")
              document.ref.moveTo(file)
              Redirect(routes.ApplicationController.index).flashing("message" -> "save.success", "type" -> "success")
            } else {
              Redirect(routes.ApplicationController.index).flashing("message" -> "save.error", "type" -> "error")
            }
          }.getOrElse {
            val kinds = List(Teaching, Research, General, Extension, Sport, Cultural)
            Ok(views.html.request.create(createForm.fill(form), kinds)).flashing("message" -> "mising.file", "type" -> "error")
          }
        })
  }

  def update = Authenticated(parse.multipartFormData) {
    implicit request =>
      createForm.bindFromRequest.fold(
        formWithErrors => {
          Redirect(routes.ApplicationController.index).flashing("message" -> "save.error", "type" -> "error")
        },
        form => {
          val userEnrollment: String = request.session("User")
          val re = Requests.get(form._1.getOrElse(0L))
          request.body.file("document").map { document =>
            val filename = document.filename

            val requestUpdated = re.copy(
              activity = Activity(form._2),
              event = form._3,
              description = form._4,
              participation = form._5,
              institution = form._6,
              period = form._7,
              workload = form._8,
              document = filename)

            if (Requests.update(requestUpdated)) {
              val file = new File(s"files/$userEnrollment/$filename")
              document.ref.moveTo(file)
              Redirect(routes.ApplicationController.index).flashing("message" -> "save.success", "type" -> "success")
            } else {
              Redirect(routes.ApplicationController.index).flashing("message" -> "save.error", "type" -> "error")
            }
          }.getOrElse {
            val requestUpdated = re.copy(
              activity = Activity(form._2),
              event = form._3,
              description = form._4,
              participation = form._5,
              institution = form._6,
              period = form._7,
              workload = form._8)

            if (Requests.update(requestUpdated)) {
              Redirect(routes.ApplicationController.index).flashing("message" -> "save.success", "type" -> "success")
            } else {
              Redirect(routes.ApplicationController.index).flashing("message" -> "save.error", "type" -> "error")
            }
          }
        })
  }

  def submit(id: Long) = Authenticated {
    implicit request =>
      if (UserMaps.submit(id)) {
        Redirect(routes.ApplicationController.index).flashing("message" -> "save.success", "type" -> "success")
      } else {
        Redirect(routes.ApplicationController.index).flashing("message" -> "save.error", "type" -> "error")
      }
  }

  def complet(id: Long) = Authenticated {
    implicit request =>
      if (Requests.complet(id)) {
        Redirect(routes.ApplicationController.index).flashing("message" -> "save.success", "type" -> "success")
      } else {
        Redirect(routes.ApplicationController.index).flashing("message" -> "save.error", "type" -> "error")
      }
  }

  def correct(id: Long) = Authenticated {
    implicit request =>
      if (Requests.correct(id)) {
        Redirect(routes.ApplicationController.index).flashing("message" -> "save.success", "type" -> "success")
      } else {
        Redirect(routes.ApplicationController.index).flashing("message" -> "save.error", "type" -> "error")
      }
  }

  def at(path: String, file: String) = Authenticated {
    implicit request =>
      val document = new File("files/" + file)

      Ok.sendFile(document)
  }

  def getActivityByKind(kind: String) = Authenticated {
    implicit request =>
      val activities = Activity.byKind(Kind(kind))
      Ok(Activity.toJson(activities)).as(JSON)
  }

}