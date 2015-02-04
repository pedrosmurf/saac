package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import models.User
import play.api.data.Forms.{ mapping, nonEmptyText, number, text, list, single, ignored, tuple }
import models.Users
import models.Requests
import models.Role
import models.Teacher
import models.Admin
import models.Student

object ApplicationController extends SaacController {

  val loginForm = Form(tuple(
    "enrollment" -> nonEmptyText,
    "password" -> nonEmptyText))

  def index = Authenticated {
    implicit request =>

      val role = Role(request.session("Role")) //FIXME

      
      role match {
        case Teacher=>
          Redirect(routes.TeacherController.list).flashing(request.flash)
        case Admin =>
          Redirect(routes.StudentController.listAll).flashing(request.flash)
        case Student =>
          Redirect(routes.StudentController.listByStudent).flashing(request.flash)
      }
  }

  def login = Action {
    implicit request =>
      Ok(views.html.user.login())
  }

  def logout = Action {
    implicit request =>
      Redirect(routes.ApplicationController.index).withNewSession
  }

  def authenticate = Action {
    implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors => {
          Redirect(routes.ApplicationController.login).flashing("message" -> "login.error", "type" -> "error")
        },
        form => {
          Users.authenticate(form._1, form._2) match {
            case None =>
              Redirect(routes.ApplicationController.login).flashing("message" -> "login.error", "type" -> "error")
            case Some(user) =>
              Redirect(routes.ApplicationController.index).withSession(("User" -> user.enrollment), ("Role" -> user.role.value))
          }
        })
  }

}