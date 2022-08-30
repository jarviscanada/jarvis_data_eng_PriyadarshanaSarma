package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {

  @Mock
  TwitterService twitterService;

  @InjectMocks
  TwitterController twitterController;

  String hashtag = "#hellotwitter";
  String text =
      "@Sarma_Priya1 Testing twitter controller " + hashtag + " " + System.currentTimeMillis();

  String coordinates = "45.0:45.0";

  @Test
  public void postTweet() {

    String[] testInput = {"post", text, coordinates};

    when(twitterService.postTweet(any())).thenReturn(new Tweet());
    twitterController.postTweet(testInput);

    when(twitterService.postTweet(any())).thenThrow(new RuntimeException("mock"));
    try {
      twitterController.postTweet(testInput);
      fail();
    } catch (RuntimeException e) {
      assertEquals("mock", e.getMessage());
    }

    //invalid input check
    try {
      twitterController.postTweet(new String[]{"", text, coordinates});
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(),
          "USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

    //invalid input check
    try {
      twitterController.postTweet(new String[]{"post", "", coordinates});
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(),
          "USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

    //invalid input check
    try {
      twitterController.postTweet(new String[]{"post", text, ""});
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(),
          "USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

  }

  @Test
  public void showTweet() {

    String id = "123456789";
    String fields = "retweet_count";

    String[] validTweetInput = {"show", id, fields};

    //valid input check
    when(twitterService.showTweet(any(), any())).thenReturn(new Tweet());
    twitterController.showTweet(validTweetInput);

    when(twitterService.showTweet(any(), any())).thenThrow(new RuntimeException("mock"));
    try {
      twitterController.showTweet(validTweetInput);
      fail();
    } catch (RuntimeException e) {
      assertEquals("mock", e.getMessage());
    }

    // invalid input check
    try {
      twitterController.showTweet(new String[]{"show"});
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(),
          "\"USAGE: TwitterApp show tweet_id \\\"field1,fields2,...\\\"\"");
    }

    // invalid input check
    try {
      twitterController.showTweet(new String[]{" "});
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(),
          "\"USAGE: TwitterApp show tweet_id \\\"field1,fields2,...\\\"\"");
    }

  }

  @Test
  public void deleteTweet() {

    String id = "123456789";

    // valid input
    when(twitterService.deleteTweets(any())).thenReturn((new ArrayList<>()));
    twitterController.deleteTweet(new String[]{"delete", id});

    // invalid input
    try {
      List<Tweet> postInputTweet = twitterController.deleteTweet(new String[]{id});
      assertNotNull(postInputTweet);
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "\"USAGE: TwitterApp delete tweet_id1, tweet_id2, ....");
    }

    // invalid input
    try {
      List<Tweet> postInputTweet = twitterController.deleteTweet(new String[]{"delete"});
      assertNotNull(postInputTweet);
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "\"USAGE: TwitterApp delete tweet_id1, tweet_id2, ....");
    }

    when(twitterService.deleteTweets(any())).thenThrow(new RuntimeException("mock"));
    try {
      twitterController.deleteTweet(new String[]{"delete", id});
      fail();
    } catch (RuntimeException e) {
      assertEquals("mock", e.getMessage());
    }
  }

}