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

object AdminController extends SaacController {

  val createForm = Form(tuple(
    "enrollment" -> nonEmptyText,
    "name" -> nonEmptyText,
    "password" -> nonEmptyText,
    "role" -> nonEmptyText))

  def listAll = Authenticated {
    implicit request =>
      val users = Users.listAll

      Ok(views.html.user.list(users))
  }

  def createStudent = Authenticated {
    implicit request =>

      Ok(views.html.user.create(Student, createForm))
  }

  def createTeacher = Authenticated {
    implicit request =>

      Ok(views.html.user.create(Teacher, createForm))
  }

  def edit(enrollment: String) = Authenticated {
    implicit request =>

      val user = Users.findByEnrollment(enrollment)
      val form = (user.enrollment, user.name, user.password, user.role.value)
      Ok(views.html.user.edit(createForm.fill(form)))
  }

  def remove(enrollment: String) = Authenticated {
    implicit request =>

      if (Users.remove(enrollment)) {
        Redirect(routes.ApplicationController.index).flashing("message" -> "remove.success", "type" -> "success")
      } else {
        Redirect(routes.ApplicationController.index).flashing("message" -> "remove.error", "type" -> "error")
      }

  }

  def update = Authenticated {
    implicit request =>
      createForm.bindFromRequest.fold(
        formWithErrors => {
          println(formWithErrors)
          Redirect(routes.ApplicationController.index).flashing("message" -> "save.error", "type" -> "error")
        },
        form => {
          if (Users.update(User(form._1, form._3, form._2, Role(form._4)))) {
            Redirect(routes.ApplicationController.index).flashing("message" -> "save.success", "type" -> "success")
          } else {
            Redirect(routes.ApplicationController.index).flashing("message" -> "save.error", "type" -> "error")
          }
        })
  }

  def save = Authenticated {
    implicit request =>
      createForm.bindFromRequest.fold(
        formWithErrors => {
          println(formWithErrors)
          Redirect(routes.ApplicationController.index).flashing("message" -> "save.error", "type" -> "error")
        },
        form => {
          if (Users.save(User(form._1, form._3, form._2, Role(form._4)))) {
            val user = Users.findByEnrollment(form._1)
            user.role match{
              case Student =>
                val dir = new File(s"files/${user.enrollment}")
                dir.mkdir
              case _ => //do nothing
            }
            Redirect(routes.ApplicationController.index).flashing("message" -> "save.success", "type" -> "success")
          } else {
            Redirect(routes.ApplicationController.index).flashing("message" -> "save.error", "type" -> "error")
          }
        })
  }

}