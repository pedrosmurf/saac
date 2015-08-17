package models

case class SaacException(reason: SaacExceptionReason, params: List[String] = Nil) extends Throwable {
  override def getMessage = reason.value
}

sealed trait SaacExceptionReason {
  def value = ""
}

case object UnknownReason extends SaacExceptionReason {
  override def value = "unknown.reason"
}

case object SemesterHoursReason extends SaacExceptionReason {
  override def value = "semester.hours.reason"
}

case object TotalHoursReason extends SaacExceptionReason {
  override def value = "total.hours.reason"
}