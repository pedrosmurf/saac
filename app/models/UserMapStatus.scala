package models

sealed trait UserMapStatus {
  val value: String
}

object UserMapStatus {
  def apply(status: String) = {
    status match {
      case "created" => MapCreated
      case "rejected" => MapRejected
      case "corrected" => MapCorrected
      case "completed" => MapCompleted
      case "canceled" => MapCanceled
      case "submitted" => MapSubmitted
    }
  }

  def unapply(status: UserMapStatus) = {
    status match {
      case MapCreated => "created"
      case MapRejected => "rejected"
      case MapCorrected => "corrected"
      case MapCompleted => "completed"
      case MapCanceled => "canceled"
      case MapSubmitted => "submitted"
    }
  }
}

case object MapCreated extends UserMapStatus {
  override val value = "created"
}
case object MapSubmitted extends UserMapStatus {
  override val value = "submitted"
}
case object MapRejected extends UserMapStatus {
  override val value = "rejected"
}
case object MapCorrected extends UserMapStatus {
  override val value = "corrected"
}
case object MapCompleted extends UserMapStatus {
  override val value = "completed"
}
case object MapCanceled extends UserMapStatus {
  override val value = "canceled"
}
