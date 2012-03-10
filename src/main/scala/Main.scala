package hatedabot

import scala.util.control.Exception.allCatch
import com.twitter.util.Eval
import java.io.File

object Main{
  val HATENA = "http://k.hatena.ne.jp/keywordblog/"+(_:String)+"?mode=rss"

  def main(args:Array[String]){
    val configFile = new File(
      allCatch.opt(args.head).getOrElse("config")
    )
    val conf = Eval[Config](configFile)
    run(conf)
  }

  def run(conf:Config){
    import conf._

    val db = new DB[BLOG_URL](dbSize)
    val client = TweetClient(twitter)

    if(! firstTweet){
      val newData = getEntries(keyword)
      db.insert(newData.map{_.link}:_*)
      println("first insert data = " + newData)
    }

    @annotation.tailrec
    def _run(){
      Thread.sleep(interval.inMillis)
      allCatchPrintStackTrace{
        val oldIds = db.selectAll
        val newData = getEntries(keyword).filterNot{a => oldIds.contains(a.link)}
        db.insert(newData.map{_.link}:_*)
        newData.reverseIterator.foreach{ entry =>
          val str = entry.tweetString(hashtags)
          Thread.sleep(tweetInterval.inMillis)
          client tweet str
        }
      }
      _run()
    }

    _run()
  }

  def getEntries(keyword:String):Seq[BlogEntry] = {
    (xml.XML.load(HATENA(keyword)) \ "item").map{ BlogEntry.apply }
  }
}
