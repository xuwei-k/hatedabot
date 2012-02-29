package hatedabot

import com.twitter.util.Duration

abstract class Config{
  val twitter:TwitterSettings
  val keyword:String
  val interval:Duration
  val dbSize:Int = 100
}

abstract class TwitterSettings{
  val consumerKey:String
  val consumerSecret:String
  val accessToken:String
  val accessTokenSecret:String
}

