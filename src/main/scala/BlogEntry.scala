package hatedabot

final case class BlogEntry(
  link        :BLOG_URL,
  title       :String,
  description :String
) {
  import BlogEntry._

  def tweetString(hashtag:String = null):String = {
    val tag = Option(hashtag).collect{case s if ! s.isEmpty => "#" + s }.getOrElse("")
    (link + " " + title + " " + tag + " \n"+ description).take(LIMIT)
  }
}

object BlogEntry{
  val LIMIT = 140

  def apply(x:scala.xml.NodeSeq):BlogEntry = {
    BlogEntry(
      (x \ "link").text,
      (x \ "title").text,
      (x \ "description").text
    )
  }
}
