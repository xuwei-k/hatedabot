package hatedabot

import org.specs2.Specification

final class Spec extends Specification{ def is=
  "DB"              ! e1 ^
  "BlogEntry apply" ! e2 ^
  end

  def e1 = {
    val db = new DB[Long](10)
    db.insert(List(1,1,2,2,2,3,3,4))

    {
      db.selectAll === List(1,2,3,4)
    }and{
      db.insert(List(2,4,4,5))
      db.selectAll === List(1,2,3,4,5)
    }and{
      db.insert((6L to 12).toList)
      db.selectAll === List(3,4,5,6,7,8,9,10,11,12)
    }
  }

  def e2 = {
    {entry:BlogEntry =>
      val str = entry.tweetString(Set("Scala","Scalajp"))
      println(str)

      {
        entry.link should be startWith "http"
      }and{
        str.size must be_<=(BlogEntry.LIMIT)
      }and{
        str must not contain("@")
      }
    }.forall(Main.getEntries("Scala",Set("absj31")))
  }
}


