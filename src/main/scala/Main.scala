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
    val db = new DB[BLOG_URL](conf.dbSize)
    val client = TweetClient(conf.twitter)
    val url = HATENA(conf.keyword)
    while(true){
      allCatchPrintStackTrace{
        val o = (xml.XML.load(url) \ "item").map{ BlogEntry.apply }
        val oldIds = db.selectAll
        val newData = o.filterNot{a => oldIds.contains(a.link)}
        db.insert(newData.map{_.link}:_*)
        newData.reverseIterator.map{_.tweetString(conf.keyword)}.foreach{ Thread.sleep(500) ; client.tweet }
      }
      Thread.sleep(conf.interval.inMillis)
    }
  }

}
