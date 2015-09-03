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
import scala.util.{ Failure, Success }

object TeacherController extends SaacController {

  val rejectForm = Form(tuple(
    "id" -> optional(longNumber),
    "comment" -> nonEmptyText))

  def list = Authenticated {
    implicit request =>

      val userMaps = UserMaps.listSubmitted

      Ok(views.html.userMap.list(userMaps))
  }

  def view(id: Long) = Authenticated {
    implicit request =>

      val userMap = UserMaps.get(id)

      Ok(views.html.userMap.view(userMap))
  }

  def evaluat(id: Long, userMapId: Long) = Authenticated {
    implicit request =>
      Requests.evaluat(id) match {
        case Success(result) =>
          Redirect(routes.TeacherController.view(userMapId)).flashing("message" -> "save.success", "type" -> "success")
        case Failure(exception) =>
          Redirect(routes.ApplicationController.index).flashing("message" -> exception.getMessage, "type" -> "error")
      }
  }

  def store(id: Long) = Authenticated {
    implicit request =>

      if (UserMaps.store(id)) {
        Redirect(routes.ApplicationController.index).flashing("message" -> "save.success", "type" -> "success")
      } else {
        Redirect(routes.ApplicationController.index).flashing("message" -> "save.error", "type" -> "error")
      }
  }

  def print(id: Long) = Authenticated {
    implicit request =>

      val userMap = UserMaps.get(id)

      Ok(views.html.userMap.print(userMap))

  }
  def reject(id: Long) = Authenticated {
    implicit request =>

      val req = Requests.get(id)

      Ok(views.html.request.reject(rejectForm.fill((req.id, req.comment))))

  }

  def doReject = Authenticated {
    implicit request =>
      rejectForm.bindFromRequest.fold(
        formWithErrors => {
          Redirect(routes.ApplicationController.index).flashing("message" -> "save.error", "type" -> "error")
        },
        form => {
          val userEnrollment: String = request.session("User")

          val re = Requests.get(form._1.getOrElse(0L))
          val requestUpdated = re.copy(status = Rejected, comment = re.comment + " \n" + form._2)

          Requests.update(requestUpdated) match {
            case Success(result) =>
              Redirect(routes.ApplicationController.index).flashing("message" -> "save.success", "type" -> "success")
            case Failure(exception) =>
              Redirect(routes.ApplicationController.index).flashing("message" -> exception.getMessage, "type" -> "error")
          }
        })
  }
}