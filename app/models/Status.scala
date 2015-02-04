package models

sealed trait Status {
  val value: String
}

object Status {
  def apply(status: String) = {
    status match {
      case "void" => Void
      case "created" => Created
      case "rejected" => Rejected
      case "corrected" => Corrected
      case "evaluated" => Evaluated
      case "completed" => Completed
      case "canceled" => Canceled
      case "submitted" => Submitted
      case _ => Void
    }
  }

  def unapply(status: Status) = {
    status match {
      case Void => "void"
      case Created => "created"
      case Rejected => "rejected"
      case Corrected => "corrected"
      case Evaluated => "evaluated"
      case Completed => "completed"
      case Canceled => "canceled"
      case Submitted => "submitted"
      case _ => "void"
    }
  }
}

case object Void extends Status {
  override val value = "void"
}
case object Created extends Status {
  override val value = "created"
}
case object Submitted extends Status {
  override val value = "submitted"
}
case object Rejected extends Status {
  override val value = "rejected"
}
case object Corrected extends Status {
  override val value = "corrected"
}
case object Evaluated extends Status {
  override val value = "evaluated"
}
case object Completed extends Status {
  override val value = "completed"
}
case object Canceled extends Status {
  override val value = "canceled"
}
