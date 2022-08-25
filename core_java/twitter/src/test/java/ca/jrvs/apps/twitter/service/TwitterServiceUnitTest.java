package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.helper.util.TwitterUtil;
import ca.jrvs.apps.twitter.model.Tweet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {

  @Mock
  CrdDao twitterDao;

  @InjectMocks
  TwitterService twitterService;

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

  @Test
  public void postTweet() {

    Tweet createdValidTweet = TwitterUtil.buildTweet(validText, latitude, longitude);
    Tweet createdInValidTweet = TwitterUtil.buildTweet(invalidText, latitude, longitude);

    when(twitterDao.create(any())).thenReturn(new Tweet());
    twitterService.postTweet(createdValidTweet);

    //exception expected here
    when(twitterDao.create(any())).thenThrow(new RuntimeException("mock"));
    try {
      twitterService.postTweet(createdValidTweet);
      fail();
    } catch (RuntimeException e) {
      assertEquals("mock", e.getMessage());
    }

    //exception expected here
    twitterService.postTweet(createdInValidTweet);

  }

  @Test
  public void showTweet() {

    when(twitterDao.findById(any())).thenReturn(new Tweet());
    twitterService.showTweet("1234567891234567", null);

    //exception expected here
    when(twitterDao.findById(any())).thenThrow(new RuntimeException("mock"));
    try {
      twitterService.showTweet("1234567891234567", null);
      fail();
    } catch (RuntimeException e) {
      assertEquals("mock", e.getMessage());
    }

    //exception expected here
    twitterService.showTweet("123456-----", null);

  }

  @Test
  public void deleteTweets() {

    String[] ids = {"1234567891234567"};
    when(twitterDao.deleteById(any())).thenReturn(new Tweet());
    twitterService.deleteTweets(ids);

    //exception expected here
    when(twitterDao.deleteById(any())).thenReturn(new RuntimeException("mock"));
    try {
      twitterService.deleteTweets(ids);
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    }

    //exception expected here
    twitterService.deleteTweets(new String[]{"123----------"});
  }
}