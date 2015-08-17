package models

import play.api.libs.functional.syntax.functionalCanBuildApplicative
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.__
import play.api.i18n.Messages
import play.api.libs.json._
import play.api.libs.functional.syntax._

sealed trait Activity {
  val value: String
  val kind: Kind
  val maxWorkload:Long
  val maxWorkloadPerActivity:Long
}

object Activity {
  def apply(activity: String) = {
    activity match {
      case "foreign_language_course" => ForeignLanguageCourse
      case "computer_course" => ComputerCourse
      case "complement_course" => ComplementCourse
      case "science_research" => ScienceResearch
      case "research_project" => ResearchProject
      case "study_group" => StudyGroup
      case "univerty_week_presentation" => UnivertyWeekPresentation
      case "congress_presentation" => CongressPresentation
      case "academic_award" => AcademicAward
      case "publication" => Publication
      case "publication_isbn" => PublicationISBN
      case "publication_isbn_chapter" => PublicationISBNChapter
      case "publication_book" => PublicationBook
      case "publication_abstract_local" => PublicationAbstractLocal
      case "publication_abstract_region" => PublicationAbstractRegion
      case "publication_abstract_nation" => PublicationAbstractNation
      case "publication_abstract_international" => PublicationAbstractInternational
      case "publication_magazine_local" => PublicationMagazineLocal
      case "publication_magazine_nation" => PublicationMagazineNation
      case "publication_magazine_international" => PublicationMagazineInternational
      case "publication_magazine_specific" => PublicationMagazineSpecific
      case "publication_paper" => PublicationPaper
      case "pet" => PET
      case "tutoring" => Tutoring
      case "event_participation" => EventParticipation
      case "lab_internship" => LabInternship
      case "internship" => Internship
      case "event_planning" => EventPlanning
      case "class_planning" => ClassPlanning
      case "school_participation" => SchoolParticipation
      case "community_participation" => CommunityParticipation

    }
  }

  def unapply(activity: Activity): String = {
    activity match {
      case ForeignLanguageCourse => "foreign_language_course"
      case ComputerCourse => "computer_course"
      case ComplementCourse => "complement_course"
      case ScienceResearch => "science_research"
      case ResearchProject => "research_project"
      case StudyGroup => "study_group"
      case UnivertyWeekPresentation => "univerty_week_presentation"
      case CongressPresentation => "congress_presentation"
      case AcademicAward => "academic_award"
      case Publication => "publication"
      case PublicationISBN => "publication_isbn"
      case PublicationISBNChapter => "publication_isbn_chapter"
      case PublicationBook => "publication_book"
      case PublicationAbstractLocal => "publication_abstract_local"
      case PublicationAbstractRegion => "publication_abstract_region"
      case PublicationAbstractNation => "publication_abstract_nation"
      case PublicationAbstractInternational => "publication_abstract_international"
      case PublicationMagazineLocal => "publication_magazine_local"
      case PublicationMagazineNation => "publication_magazine_nation"
      case PublicationMagazineInternational => "publication_magazine_international"
      case PublicationMagazineSpecific => "publication_magazine_specific"
      case PublicationPaper => "publication_paper"
      case PET => "pet"
      case Tutoring => "tutoring"
      case EventParticipation => "event_participation"
      case LabInternship => "lab_internship"
      case Internship => "internship"
      case EventPlanning => "event_planning"
      case ClassPlanning => "class_planning"
      case SchoolParticipation => "school_participation"
      case CommunityParticipation => "community_participation"
    }
  }

  def all: List[Activity] = {
    List(
      ForeignLanguageCourse,
      ComputerCourse,
      ComplementCourse,
      ScienceResearch,
      ResearchProject,
      StudyGroup,
      UnivertyWeekPresentation,
      CongressPresentation,
      AcademicAward,
      Publication,
      PublicationISBN,
      PublicationISBNChapter,
      PublicationBook,
      PublicationAbstractLocal,
      PublicationAbstractRegion,
      PublicationAbstractNation,
      PublicationAbstractInternational,
      PublicationMagazineLocal,
      PublicationMagazineNation,
      PublicationMagazineInternational,
      PublicationMagazineSpecific,
      PublicationPaper,
      PET,
      Tutoring,
      EventParticipation,
      LabInternship,
      Internship,
      EventPlanning,
      ClassPlanning,
      SchoolParticipation,
      CommunityParticipation)
  }

  def byKind(kind: Kind): List[Activity] = {
    all.filter(_.kind == kind)
  }

  def toJson(activities: List[Activity]): String = {
    val values = activities.map(Activity.toJson(_)).mkString(",")
    "["+ values  +"]"
  }
  def toJson(activity: Activity): String = {
    s"""{"value":"${activity.value}","kind":${Kind.toJson(activity.kind)},"maxWorkload":${activity.maxWorkload}}"""
  }
}

case object ForeignLanguageCourse extends Activity {
  override val value = "foreign_language_course"
  override val kind = Teaching
  override val maxWorkload = 60L
  override val maxWorkloadPerActivity = 60L
}
case object ComputerCourse extends Activity {
  override val value = "computer_course"
  override val kind = Teaching
  override val maxWorkload = 60L
  override val maxWorkloadPerActivity = 60L
}
case object ComplementCourse extends Activity {
  override val value = "complement_course"
  override val kind = Teaching
  override val maxWorkload = 60L
  override val maxWorkloadPerActivity = 60L
}
case object ScienceResearch extends Activity {
  override val value = "science_research"
  override val kind = Research
  override val maxWorkload = 25L
  override val maxWorkloadPerActivity = 100L
}
case object ResearchProject extends Activity {
  override val value = "research_project"
  override val kind = Research
  override val maxWorkload = 20L
  override val maxWorkloadPerActivity = 80L
}
case object StudyGroup extends Activity {
  override val value = "study_group"
  override val kind = Research
  override val maxWorkload = 15L
  override val maxWorkloadPerActivity = 60L
}
case object UnivertyWeekPresentation extends Activity {
  override val value = "univerty_week_presentation"
  override val kind = Research
  override val maxWorkload = 8L
  override val maxWorkloadPerActivity = 48L
}
case object CongressPresentation extends Activity {
  override val value = "congress_presentation"
  override val kind = Research
  override val maxWorkload = 8L
  override val maxWorkloadPerActivity = 48L
}
case object AcademicAward extends Activity {
  override val value = "academic_award"
  override val kind = Research
  override val maxWorkload = 15L
  override val maxWorkloadPerActivity = 60L
}
case object Publication extends Activity {
  override val value = "publication"
  override val kind = Research
  override val maxWorkload = 20L
  override val maxWorkloadPerActivity = 80L
}
case object PublicationISBN extends Activity {
  override val value = "publication_isbn"
  override val kind = Research
  override val maxWorkload = 20L
  override val maxWorkloadPerActivity = 80L
}
case object PublicationISBNChapter extends Activity {
  override val value = "publication_isbn_chapter"
  override val kind = Research
  override val maxWorkload = 10L
  override val maxWorkloadPerActivity = 50L
}
case object PublicationBook extends Activity {
  override val value = "publication_book"
  override val kind = Research
  override val maxWorkload = 15L
  override val maxWorkloadPerActivity = 60L
}
case object PublicationAbstractLocal extends Activity {
  override val value = "publication_abstract_local"
  override val kind = Research
  override val maxWorkload = 2L
  override val maxWorkloadPerActivity = 20L
}
case object PublicationAbstractRegion extends Activity {
  override val value = "publication_abstract_region"
  override val kind = Research
  override val maxWorkload = 3L
  override val maxWorkloadPerActivity = 30L
}
case object PublicationAbstractNation extends Activity {
  override val value = "publication_abstract_nation"
  override val kind = Research
  override val maxWorkload = 4L
  override val maxWorkloadPerActivity = 40L
}
case object PublicationAbstractInternational extends Activity {
  override val value = "publication_abstract_international"
  override val kind = Research
  override val maxWorkload = 5L
  override val maxWorkloadPerActivity = 40L
}
case object PublicationMagazineLocal extends Activity {
  override val value = "publication_magazine_local"
  override val kind = Research
  override val maxWorkload = 10L
  override val maxWorkloadPerActivity = 50L
}
case object PublicationMagazineNation extends Activity {
  override val value = "publication_magazine_nation"
  override val kind = Research
  override val maxWorkload = 15L
  override val maxWorkloadPerActivity = 60L
}
case object PublicationMagazineInternational extends Activity {
  override val value = "publication_magazine_international"
  override val kind = Research
  override val maxWorkload = 20L
  override val maxWorkloadPerActivity = 80L
}
case object PublicationMagazineSpecific extends Activity {
  override val value = "publication_magazine_specific"
  override val kind = Research
  override val maxWorkload = 5L
  override val maxWorkloadPerActivity = 20L
}
case object PublicationPaper extends Activity {
  override val value = "publication_paper"
  override val kind = Research
  override val maxWorkload = 5L
  override val maxWorkloadPerActivity = 20L
}
case object PET extends Activity {
  override val value = "pet"
  override val kind = General
  override val maxWorkload = 25L
  override val maxWorkloadPerActivity = 100L
}
case object Tutoring extends Activity {
  override val value = "tutoring"
  override val kind = General
  override val maxWorkload = 25L
  override val maxWorkloadPerActivity = 100L
}
case object EventParticipation extends Activity {
  override val value = "event_participation"
  override val kind = General
  override val maxWorkload = 2L
  override val maxWorkloadPerActivity = 40L
}
case object LabInternship extends Activity {
  override val value = "lab_internship"
  override val kind = General
  override val maxWorkload = 15L
  override val maxWorkloadPerActivity = 60L
}
case object Internship extends Activity {
  override val value = "internship"
  override val kind = General
  override val maxWorkload = 20L
  override val maxWorkloadPerActivity = 60L
}
case object EventPlanning extends Activity {
  override val value = "event_planning"
  override val kind = General
  override val maxWorkload = 10L
  override val maxWorkloadPerActivity = 40L
}
case object ClassPlanning extends Activity {
  override val value = "class_planning"
  override val kind = General
  override val maxWorkload = 8L
  override val maxWorkloadPerActivity = 40L
}
case object SchoolParticipation extends Activity {
  override val value = "school_participation"
  override val kind = General
  override val maxWorkload = 15L
  override val maxWorkloadPerActivity = 60L
}
case object CommunityParticipation extends Activity {
  override val value = "community_participation"
  override val kind = Extension
  override val maxWorkload = 15L
  override val maxWorkloadPerActivity = 100L
}