package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.helper.util.TwitterUtil;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TwitterControllerIntTest {

  private CrdDao twitterDao;
  private Service twitterService;
  private Controller twitterController;

  String hashtag = "#hellotwitter";
  String text =
      "@Sarma_Priya1 Testing twitter controller " + hashtag + " " + System.currentTimeMillis();

  String coordinates = "45.0:45.0";

  @Before
  public void setUp() {
    String consumerkey = System.getenv("CONSUMERKEY");
    String consumerSecret = System.getenv("CONSUMERSECRET");
    String accessToken = System.getenv("ACCESSTOKEN");
    String tokenSecret = System.getenv("TOKENSECRET");

    System.out.println(consumerkey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);
    HttpHelper twitterHttpHelper = new TwitterHttpHelper(consumerkey, consumerSecret, accessToken,
        tokenSecret);

    this.twitterDao = new TwitterDao(twitterHttpHelper);
    this.twitterService = new TwitterService(twitterDao);
    this.twitterController = new TwitterController(twitterService);
  }

  @Test
  public void postTweet() {

    String[] testInput = {"post", text, coordinates};

    //valid input check
    try {
      Tweet postTweet = twitterController.postTweet(testInput);
      assertNotNull(postTweet);
      assertEquals(postTweet.getText(), text);
    }
    catch (IllegalArgumentException e) {
    assertEquals(e.getMessage(), "USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

    //invalid input check
    try {
      twitterController.postTweet(new String[]{"", text, coordinates});
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

    //invalid input check
    try {
      twitterController.postTweet(new String[]{"post", "", coordinates});
      fail();
    } catch (IllegalArgumentException e) {
    assertEquals(e.getMessage(), "USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

    //invalid input check
    try {
      twitterController.postTweet(new String[]{"post", text, ""});
      fail();
    } catch (IllegalArgumentException e) {
    assertEquals(e.getMessage(), "USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

  }

  @Test
  public void showTweet() {

    String[] postTweetInput = {"post", text, coordinates};
    String id = "";
    String fields = "retweet_count";

    // post tweet
    try {
      Tweet postTweet = twitterController.postTweet(postTweetInput);
      id = postTweet.getId();
    }
    catch (IllegalArgumentException e) {
    assertEquals(e.getMessage(), "USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

    String[] validTweetInput = {"show", id, fields};

    //  valid input check
    try {
      Tweet showTweet = twitterController.showTweet(validTweetInput);
      assertNotNull(showTweet);
      assertEquals(showTweet.getText(), text);
      assertEquals(showTweet.getId(), id);
    }
    catch (IllegalArgumentException e) {
    assertEquals(e.getMessage(), "\"USAGE: TwitterApp show tweet_id \\\"field1,fields2,...\\\"\"");
    }


    // invalid input check
    try {
      twitterController.showTweet(new String[] {"show"});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "\"USAGE: TwitterApp show tweet_id \\\"field1,fields2,...\\\"\"");
    }

    // invalid input check
    try {
      twitterController.showTweet(new String[] {" "});
      fail();
    }
    catch (IllegalArgumentException e) {
    assertEquals(e.getMessage(), "\"USAGE: TwitterApp show tweet_id \\\"field1,fields2,...\\\"\"");
    }

  }

  @Test
  public void deleteTweet() {
    String[] postTweetInput = {"post", text, coordinates};
    String id = "";

    // post tweet
    try{
      Tweet postInputTweet = twitterController.postTweet(postTweetInput);
      id = postInputTweet.getId();
    } catch(IllegalArgumentException e) {
      assertEquals(e.getMessage(), "USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

    // valid input
    try{
      List<Tweet> postInputTweet = twitterController.deleteTweet(new String[]{"delete", id});
      assertNotNull(postInputTweet);
    } catch(IllegalArgumentException e) {
      assertEquals(e.getMessage(), "\"USAGE: TwitterApp delete tweet_id1, tweet_id2, ....");
    }

    // invalid input
    try{
      List<Tweet> postInputTweet = twitterController.deleteTweet(new String[]{id});
      assertNotNull(postInputTweet);
    } catch(IllegalArgumentException e) {
      assertEquals(e.getMessage(), "\"USAGE: TwitterApp delete tweet_id1, tweet_id2, ....");
    }

    // invalid input
    try{
      List<Tweet> postInputTweet = twitterController.deleteTweet(new String[]{"delete"});
      assertNotNull(postInputTweet);
    } catch(IllegalArgumentException e) {
      assertEquals(e.getMessage(), "\"USAGE: TwitterApp delete tweet_id1, tweet_id2, ....");
    }
  }

}