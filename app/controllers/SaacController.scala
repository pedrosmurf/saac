package controllers

import play.api._
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import play.Play
import play.api.libs.json._
import models._
import scala.concurrent.Promise
import play.api.http.ContentTypeOf
import play.api.http.Writeable
import play.api.i18n.Messages
import com.typesafe.config.ConfigException

trait SaacController extends Controller {
  class AuthRequest[A](val user: String, request: play.api.mvc.Request[A]) extends WrappedRequest[A](request)

  object Authenticated extends ActionBuilder[AuthRequest] {
    def invokeBlock[A](request: play.api.mvc.Request[A], block: (AuthRequest[A]) => Future[Result]) = {
      request.session.get("User").map { user =>
        block(new AuthRequest(user, request))
      } getOrElse {
        Future(Redirect(routes.ApplicationController.login).flashing("message" -> "login.error.authenticate", "type" -> "error"))
      }
    }
  }

}