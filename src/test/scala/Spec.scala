package hatedabot

import org.specs2.Specification

class Spec extends Specification{ def is=
  "DB"              ! e1 ^
  "BlogEntry apply" ! e2 ^
  end

  def e1 = {
    val db = new DB[Long](10)
    db.insert(1,1,2,2,2,3,3,4)

    {
      db.selectAll === List(1,2,3,4)
    }and{
      db.insert(2,4,4,5)
      db.selectAll === List(1,2,3,4,5)
    }and{
      db.insert((6L to 12).toSeq :_*)
      db.selectAll === List(3,4,5,6,7,8,9,10,11,12)
    }
  }

    val link  = "http://d.hatena.ne.jp/tototoshi/20120228/1330435824"
    val title = "sbt cleanで消してほしくないファイルを指定する。"

    val data = {
      <item rdf:about="http://d.hatena.ne.jp/tototoshi/20120228/1330435824">
        <link>{link}</link>
        <dc:date>{"2012-02-28T22:30:24+09:00"}</dc:date>
        <description>{"""xsbt-start-script-plugin で sbt stage して作った target/start が clean したら消えちゃいましたよ。みたいなことを防げます。 cleanKeepFiles += new File(&amp;quot;target/start&amp;quot;) https://github.com/harrah/xsbt/blob/v0.11.2/main/Keys.sca... &lt;div class="more"&gt;&lt;a href="http://d.hatena.ne.jp/tototoshi/20120228/1330435824"&gt;続きを読む&lt;/a&gt;&lt;/div&gt;"""}</description>
        <dc:creator>tototoshi</dc:creator>
        <title>{title}</title>
      </item>
    }

  def e2 = {
    val obj = BlogEntry(data \\ "item")

    {
      obj.link === link
    }and{
      obj.title === title
    }and{
      obj.tweetString().size must be_<=(BlogEntry.LIMIT)
    }
  }
}


