package hatedabot

import scala.util.control.Exception.allCatch
import java.io.File

object Main{
  private[this] val HATENA = "http://k.hatena.ne.jp/keywordblog/"+(_:String)+"?mode=rss"

  def main(args: Array[String]): Unit = {
    val file = new File(
      allCatch.opt(args.head).getOrElse("config.scala")
    )
    run(file)
  }

  def run(file: File): Unit = {
    val env = Env.fromConfigFile(file)
    import env._, env.config._

    val firstData = getEntries(keyword, blockUsers)
    db.insert(firstData.map{_.link}.toList)
    printDateTime()
    println("first insert data = " + firstData)
    firstData.take(firstTweetCount).reverseIterator.foreach { entry =>
      Thread.sleep(tweetInterval.toMillis)
      client.tweet(entry.tweetString(hashtags))
    }
    loop(env)
  }

  @annotation.tailrec
  def loop(env: Env): Unit = {
    import env._, env.config._
    try {
      Thread.sleep(interval.toMillis)
      val oldIds = db.selectAll
      val newData = getEntries(keyword, blockUsers).filterNot{ a => oldIds.contains(a.link)}
      db.insert(newData.map{_.link}.toList)
      newData.reverseIterator.foreach { e =>
        Thread.sleep(env.config.tweetInterval.toMillis)
        env.client.tweet(e.tweetString(hashtags))
      }
    } catch {
      case e: Throwable =>
        try {
          printDateTime()
          e.printStackTrace()
          Mail(e.getMessage, e.getStackTrace.mkString("\n"), mail)
        } catch {
          case e: Throwable =>
            e.printStackTrace()
        }
    }
    loop(env.reload)
  }

  def printDateTime(): Unit = {
    val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm")
    println(df.format(new java.util.Date))
  }

  def getEntries(keyword: String, blockUsers: Set[String]): Seq[BlogEntry] = {
    (xml.XML.load(HATENA(keyword)) \ "item").map{
      BlogEntry.apply
    }.filterNot{ e =>
      blockUsers.contains(e.creator)
    }
  }
}
