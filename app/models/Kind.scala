package models

import play.api.libs.functional.syntax.functionalCanBuildApplicative
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.__
import play.api.i18n.Messages
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads

sealed trait Kind {
  val value: String
}

object Kind {
  def apply(value: String): Kind = {
    value match {
      case "teaching" => Teaching
      case "research" => Research
      case "general" => General
      case "extension" => Extension
      case "sport" => Sport
      case "cultural" => Cultural
    }
  }
  def unapply(kind: Kind): String = {
    kind match {
      case Teaching => "teaching"
      case Research => "research"
      case General => "general"
      case Extension => "extension"
      case Sport => "sport"
      case Cultural => "cultural"
    }
  }

  def toJson(kind: Kind) = {
    s"""{"value":"${kind.value}"}"""
  }
  
//    implicit val reader = new Reads[Kind] {
//	  override def re(JsPath \ "value").read[String])(Kind.apply _)

}

case object Teaching extends Kind {
  override val value = "teaching"
}
case object Research extends Kind {
  override val value = "research"
}
case object General extends Kind {
  override val value = "general"
}
case object Extension extends Kind {
  override val value = "extension"
}
case object Sport extends Kind {
  override val value = "sport"
}
case object Cultural extends Kind {
  override val value = "cultural"
}