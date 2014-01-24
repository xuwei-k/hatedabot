package hatedabot

import scala.util.control.Exception.allCatch
import com.twitter.util.Eval
import java.io.File

object Main{
  val HATENA = "http://k.hatena.ne.jp/keywordblog/"+(_:String)+"?mode=rss"

  def main(args:Array[String]){
    val file = new File(
      allCatch.opt(args.head).getOrElse("config.scala")
    )
    run(file)
  }

  def run(file:File){
    val conf = Eval[Config](file)
    import conf._

    val db = new DB[BLOG_URL](dbSize)
    val client = TweetClient(twitter)

    def tweet(data:Seq[BlogEntry]){
      data.reverseIterator.foreach{ entry =>
        Thread.sleep(tweetInterval.inMillis)
        client.tweet(entry.tweetString(hashtags))
      }
    }

    def entries() = {
      val c = Eval[Config](file)
      getEntries(c.keyword,c.blockUsers)
    }

    val firstData = entries()
    db.insert(firstData.map{_.link}:_*)
    println("first insert data = " + firstData)
    if(firstTweet){
      tweet(firstData)
    }

    def allCatchPrintStackTrace(body: => Any){
      try{
        val r = body
      }catch{
        case e: Throwable =>
          try{
            val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm")
            println(df.format(new java.util.Date))
            e.printStackTrace
            Mail(e.getMessage, e.getStackTrace.mkString("\n"), conf.mail)
          }catch{
            case e: Throwable =>
          }
      }
    }

    @annotation.tailrec
    def _run(){
      Thread.sleep(interval.inMillis)
      allCatchPrintStackTrace{
        val oldIds = db.selectAll
        val newData = entries().filterNot{a => oldIds.contains(a.link)}
        db.insert(newData.map{_.link}:_*)
        tweet(newData)
      }
      _run()
    }

    _run()
  }

  def getEntries(keyword: String, blockUsers: Set[String]): Seq[BlogEntry] = {
    (xml.XML.load(HATENA(keyword)) \ "item").map{
      BlogEntry.apply
    }.filterNot{ e =>
      val a = blockUsers.contains(e.creator)
//      if(a) println("block ! " + e)
      a
    }
  }
}
