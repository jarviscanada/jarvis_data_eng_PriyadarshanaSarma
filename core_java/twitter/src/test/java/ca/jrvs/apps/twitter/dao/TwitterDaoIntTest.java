package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.assertEquals;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.helper.util.TwitterUtil;
import ca.jrvs.apps.twitter.model.Tweet;
import org.junit.Before;
import org.junit.Test;

public class TwitterDaoIntTest {

  private TwitterDao twitterDao;

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
  }

  @Test
  public void create() {
    String hashtag = "#helloTwitter";
    String text =
        "@Sarma_Priya1 Exploring Twitter API " + hashtag + " " + System.currentTimeMillis();
    Double latitude = 30d;
    Double longitude = -30d;

    Tweet postTweet = TwitterUtil.buildTweet(text, latitude, longitude);
    Tweet response = twitterDao.create(postTweet);
    assertEquals(text, response.getText());
    /* assertEquals(latitude, response.getCoordinates().getCoordinates().get(0));
    assertEquals(longitude, response.getCoordinates().getCoordinates().get(1));
    assertEquals(2, response.getCoordinates().getCoordinates().size()); */

  }

  @Test
  public void findById() {
    String hashtag = "#helloTwitter";
    String text =
        "@Sarma_Priya1 Exploring Twitter API-2 " + hashtag + " " + System.currentTimeMillis();
    Double latitude = 40d;
    Double longitude = -40d;

    Tweet postTweet = TwitterUtil.buildTweet(text, latitude, longitude);
    Tweet created_tweet = twitterDao.create(postTweet);

    Tweet found_tweet = twitterDao.findById(created_tweet.getId());

    assertEquals(found_tweet.getId(), created_tweet.getId());
    assertEquals(found_tweet.getText(), created_tweet.getText());
  }

  @Test
  public void deleteById() {
    String hashtag = "#helloTwitter";
    String text =
        "@Sarma_Priya1 Exploring Twitter API-3 " + hashtag + " " + System.currentTimeMillis();
    Double latitude = 50d;
    Double longitude = -50d;

    Tweet postTweet = TwitterUtil.buildTweet(text, latitude, longitude);
    Tweet created_tweet = twitterDao.create(postTweet);

    Tweet deleted_tweet = twitterDao.deleteById(created_tweet.getId());

    assertEquals(deleted_tweet.getId(), created_tweet.getId());
    assertEquals(deleted_tweet.getText(), created_tweet.getText());
  }
}
