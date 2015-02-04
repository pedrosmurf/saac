package models

sealed trait Role {
  val value: String
}

object Role {
  def apply(role: String) = {
    role match {
      case "admin" => Admin
      case "teacher" => Teacher
      case "student" => Student
      case _ => Student
    }
  }
  def unapply(role: Role) = {
    role match {
      case Admin => "admin" 
      case Teacher => "teacher" 
      case Student => "student" 
      case _ => "student"
    }
  }
}

case object Admin extends Role {
  override val value = "admin"
}
case object Student extends Role {
  override val value = "student"
}
case object Teacher extends Role {
  override val value = "teacher"
}