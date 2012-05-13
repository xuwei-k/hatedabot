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

    def tweet(data:Seq[BlogEntry]){
      data.reverseIterator.foreach{ entry =>
        Thread.sleep(tweetInterval.inMillis)
        client.tweet(entry.tweetString(hashtags))
      }
    }

    def entries() = getEntries(keyword,blockUsers)

    val firstData = entries()
    db.insert(firstData.map{_.link}:_*)
    println("first insert data = " + firstData)
    if(firstTweet){
      tweet(firstData)
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

  def getEntries(keyword:String,blockUsers:Set[String] = Set.empty):Seq[BlogEntry] = {
    (xml.XML.load(HATENA(keyword)) \ "item").map{
      BlogEntry.apply
    }.filterNot{ e =>
      val a = blockUsers.contains(e.creator)
      if(a)println("block ! " + e)
      a
    }
  }
}
