package hatedabot

import twitter4j._
import twitter4j.conf._

final case class TweetClient(conf:TwitterSettings) {

  val t = {
    val c = new ConfigurationBuilder
      c.setDebugEnabled(true)
      .setOAuthConsumerKey(conf.consumerKey)
      .setOAuthConsumerSecret(conf.consumerSecret)
      .setOAuthAccessToken(conf.accessToken)
      .setOAuthAccessTokenSecret(conf.accessTokenSecret);

    new TwitterFactory(c.build()).getInstance()
  }

  def tweet(str: String){
    t.updateStatus(str)
  }

}


