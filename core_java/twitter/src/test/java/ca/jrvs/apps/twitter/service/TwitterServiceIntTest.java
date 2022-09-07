package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.helper.util.TwitterUtil;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;


public class TwitterServiceIntTest {

  String hashtag = "#hellotwitter";
  String validText =
      "@Sarma_Priya1 Testing twitter service " + hashtag + " " + System.currentTimeMillis();
  Double latitude = 30d;
  Double longitude = -30d;
  String invalidText =
      "@Sarma_Priya1 Testing twitter service " + hashtag + " " + System.currentTimeMillis()
          + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
          + "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"
          + "cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc"
          + "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"
          + "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee";
  private TwitterDao twitterDao;
  private TwitterService twitterService;

  @Before
  public void setUp() throws Exception {

    String consumerkey = System.getenv("CONSUMERKEY");
    String consumerSecret = System.getenv("CONSUMERSECRET");
    String accessToken = System.getenv("ACCESSTOKEN");
    String tokenSecret = System.getenv("TOKENSECRET");

    System.out.println(consumerkey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);
    HttpHelper twitterHttpHelper = new TwitterHttpHelper(consumerkey, consumerSecret, accessToken,
        tokenSecret);

    this.twitterDao = new TwitterDao(twitterHttpHelper);
    this.twitterService = new TwitterService(twitterDao);
  }

  @Test
  public void postTweet() {

    Tweet createdValidTweet = TwitterUtil.buildTweet(validText, latitude, longitude);

    Tweet postedValidTweet = twitterService.postTweet(createdValidTweet);

    Tweet createdInValidTextTweet = TwitterUtil.buildTweet(invalidText, latitude, longitude);
    twitterService.postTweet(createdInValidTextTweet);

    assertEquals(createdValidTweet.getText(), postedValidTweet.getText());
    assertNotNull(postedValidTweet.getText());

  }

  @Test
  public void showTweet() {

    Tweet createdValidTweet = TwitterUtil.buildTweet(validText, latitude, longitude);

    Tweet postedValidTweet = twitterService.postTweet(createdValidTweet);
    Tweet showTweet = twitterService.showTweet(postedValidTweet.getId(), null);

    // expect exception
    twitterService.showTweet("-----1234", null);

    assertEquals(showTweet.getId(), postedValidTweet.getId());
    assertNotNull(postedValidTweet.getId());

  }

  @Test
  public void deleteTweets() {

    Tweet createdValidTweet = TwitterUtil.buildTweet(validText, latitude, longitude);

    Tweet postedValidTweet = twitterService.postTweet(createdValidTweet);
    String[] ids = {postedValidTweet.getId()};
    List<Tweet> deletedTweets = twitterService.deleteTweets(ids);

    // expect exception
    twitterService.deleteTweets(new String[]{"------------------------"});

    assertNotNull(deletedTweets.get(0));

  }
}
