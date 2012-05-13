package hatedabot

final case class BlogEntry(
  link        :BLOG_URL,
  title       :String,
  description :String,
  creator     :String
) {
  import BlogEntry._

  def tweetString(hashtags:Set[String] = Set.empty):String = {
    val tags = hashtags.collect{case s if ! s.isEmpty => "#" + s }.mkString(" ")
    Iterator(
      link,title.replace("@",""),tags,description.replace("@","")
    ).mkString("\n").take(LIMIT)
  }
}

object BlogEntry{
  val LIMIT = 140

  def apply(x:scala.xml.NodeSeq):BlogEntry = {
    BlogEntry(
      (x \ "link").text,
      (x \ "title").text,
      (x \ "description").text,
      (x \ "creator").text
    )
  }
}
