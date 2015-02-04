import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent.Future
import models.DB
import models.Users
import models.User
import models.Requests
import models.Admin
import models.Student
import models.Teacher
import models.UserMaps
import models.UserMap
import models.MapCreated

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Users.createTable
    Requests.createTable
    UserMaps.createTable
    //    Users.insert(User("admin", "admin", "Admin", Admin))
//    Users.insert(User("0949450", "123", "Pedro", Student))
    //    Users.insert(User("1234567", "123", "Joao", Student))
//    Users.insert(User("87654321", "123", "Maria", Teacher))
//    UserMaps.insert(UserMap(Some(1L), User("0949450", "123", "Pedro", Student), true, Nil, 0, 0, MapCreated))
  }
}