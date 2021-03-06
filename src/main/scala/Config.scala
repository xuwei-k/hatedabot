package hatedabot

import scala.concurrent.duration._

abstract class Config{
  val twitter: TwitterSettings
  def keyword: String
  val hashtags: Set[String] = Set(keyword)
  val tweetInterval: Duration = 500 millis
  val interval: Duration
  val dbSize: Int = 100
  val firstTweetCount: Int
  val blockUsers: Set[String] = Set.empty
  def mail: Mail.Conf
}

abstract class TwitterSettings{
  val consumerKey: String
  val consumerSecret: String
  val accessToken: String
  val accessTokenSecret: String
}

