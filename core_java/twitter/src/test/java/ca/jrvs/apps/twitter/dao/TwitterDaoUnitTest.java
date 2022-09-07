package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.util.TwitterUtil;
import ca.jrvs.apps.twitter.example.JSONParser;
import ca.jrvs.apps.twitter.model.Tweet;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {

  @Mock
  HttpHelper mockHelper;

  @InjectMocks
  TwitterDao twitterDao;

  // Test happy path
  // however, we don't want to call parseResponseBody
  // we will make a spyDao which can fake parseResponseBody return value
  String tweetJsonStr = "{\n"
      + "   \"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n"
      + "   \"id\":12345678901234567890,\n"
      + "   \"id_str\":\"12345678901234567890\",\n"
      + "   \"text\":\"test with mockito\",\n"
      + "   \"entities\":{\n"
      + "       \"hashtags\":[],"
      + "       \"user_mentions\":[]"
      + "   },\n"
      + "   \"coordinates\":null,\n"
      + "   \"retweet_count\":0,\n"
      + "   \"favorite_count\":0,\n"
      + "   \"favorited\":false,\n"
      + "   \"retweeted\":false\n"
      + "}";

  @Test
  public void create() {
    String hashtag = "#helloTwitter";
    String text =
        "@Sarma_Priya1 Exploring Twitter API " + hashtag + " " + System.currentTimeMillis();
    Double latitude = 30d;
    Double longitude = -30d;

    //exception expected here
    when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      twitterDao.create(TwitterUtil.buildTweet(text, latitude, longitude));
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    }

    when(mockHelper.httpPost(isNotNull())).thenReturn(null);
    TwitterDao spydao = Mockito.spy(twitterDao);
    try {
      Tweet expectedTweet = JSONParser.toObjectFromJson(tweetJsonStr, Tweet.class);

      // mock parseResponseBody
      doReturn(expectedTweet).when(spydao).parseResponseBody(any(), anyInt());
      Tweet responseTweet = spydao.create(TwitterUtil.buildTweet(text, latitude, longitude));
      assertNotNull(responseTweet);
      assertNotNull(responseTweet.getText());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void findById() {
    //exception expected here
    when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      twitterDao.findById("12345678901234567890");
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    }

    when(mockHelper.httpPost(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(twitterDao);
    try {
      Tweet expectedTweet = JSONParser.toObjectFromJson(tweetJsonStr, Tweet.class);
      doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
      Tweet responseTweet = spyDao.findById(expectedTweet.getId());
      assertNotNull(responseTweet);
      assertNotNull(responseTweet.getText());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void deleteById() {
    //exception expected here
    when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      twitterDao.findById("12345678901234567890");
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    }

    when(mockHelper.httpPost(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(twitterDao);
    try {
      Tweet expectedTweet = JSONParser.toObjectFromJson(tweetJsonStr, Tweet.class);
      doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
      Tweet responseTweet = spyDao.deleteById(expectedTweet.getId());
      assertNotNull(responseTweet);
      assertNotNull(responseTweet.getText());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}