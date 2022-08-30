package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.List;

public class TwitterService implements Service {

  private CrdDao crdDao;

  public TwitterService(CrdDao crdDao) {
    this.crdDao = crdDao;
  }

  /**
   * Validate and post a user input Tweet
   *
   * @param tweet tweet to be created
   * @return created tweet
   * @throws IllegalArgumentException if text exceed max number of allowed characters
   */
  @Override
  public Tweet postTweet(Tweet tweet) {

    //validate if tweet exceeds maximum allowed characters
    validatePostTweet(tweet);
//
    //create tweet via dao
    return (Tweet) crdDao.create(tweet);
  }

  /**
   * Search a tweet by ID
   *
   * @param id     tweet id
   * @param fields set fields not in the list to null
   * @return Tweet object which is returned by the Twitter API
   * @throws IllegalArgumentException if id or fields param is invalid
   */
  @Override
  public Tweet showTweet(String id, String[] fields) {

    validateId(id);

    return (Tweet) crdDao.findById(id);
  }

  /**
   * Delete Tweet(s) by id(s).
   *
   * @param ids tweet IDs which will be deleted
   * @return A list of Tweets
   * @throws IllegalArgumentException if one of the IDs is invalid.
   */
  @Override
  public List<Tweet> deleteTweets(String[] ids) {

    List<Tweet> tweetList = new ArrayList<>();
    for (String id : ids) {
      validateId(id);
      tweetList.add((Tweet) crdDao.deleteById(id));
    }
    return tweetList;
  }

  private void validatePostTweet(Tweet tweet) {

    //validate if tweet exceeds maximum allowed characters
    if (tweet.getText().length() > 140) {
      throw new IllegalArgumentException("Tweet exceeded maximum allowed characters of 140");
    }
  }

  //validate Id field
  private void validateId(String id) {

    if (id.isEmpty()) {
      throw new IllegalArgumentException("Id id empty");
    }

    try {
      Long.parseUnsignedLong(id);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid Id");
    }
  }

}
