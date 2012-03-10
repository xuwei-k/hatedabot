package hatedabot

import com.twitter.util.Duration
import com.twitter.conversions.time._

abstract class Config{
  val twitter:TwitterSettings
  def keyword:String
  val hashtags:Set[String] = Set(keyword)
  val tweetInterval:Duration = 500 millis
  val interval:Duration
  val dbSize:Int = 100
  val firstTweet:Boolean = false
}

abstract class TwitterSettings{
  val consumerKey:String
  val consumerSecret:String
  val accessToken:String
  val accessTokenSecret:String
}

