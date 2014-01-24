import hatedabot._
import com.twitter.conversions.time._

new Config{
  val keyword = "Scala"
  val interval = 1.hours
  val mail = Mail.Conf(
    "",
    ""
  )

  val twitter = new TwitterSettings{
    val consumerKey       = ""
    val consumerSecret    = ""
    val accessToken       = ""
    val accessTokenSecret = ""
  }
}

