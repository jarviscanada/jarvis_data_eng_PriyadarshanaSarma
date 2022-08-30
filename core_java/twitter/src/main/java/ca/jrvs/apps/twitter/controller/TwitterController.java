package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.dao.helper.util.TwitterUtil;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * The controller layer interacts and parsers user input (CLI args in this APP). It also calls the
 * service layer and returns results.
 */
public class TwitterController implements Controller {

  private static final String COORD_SEP = ":";
  private static final String COMMA = ",";
  private Service service;

  @Autowired
  public TwitterController(Service service) {
    this.service = service;
  }

  /**
   * Parse user argument and post a tweet by calling service classes
   *
   * @param args
   * @return a posted tweet
   * @throws IllegalArgumentException if args are invalid
   */


  @Override
  public Tweet postTweet(String[] args) {
    if (args.length != 3 || args[0] != "post") {
      throw new IllegalArgumentException(
          "USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

    String tweet_text = args[1];
    String coord = args[2];
    String[] coordinateArray = coord.split(COORD_SEP);
    if (coordinateArray.length != 2 || StringUtils.isEmpty(tweet_text)) {
      throw new IllegalArgumentException(
          "USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

    Double lat = null;
    Double lon = null;

    try {
      lat = Double.parseDouble(coordinateArray[0]);
      lon = Double.parseDouble(coordinateArray[1]);
    } catch (Exception e) {
      throw new IllegalArgumentException(
          "USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

    Tweet postTweet = TwitterUtil.buildTweet(tweet_text, lat, lon);
    return service.postTweet(postTweet);

  }

  /**
   * Parse user argument and search a tweet by calling service classes
   *
   * @param args
   * @return a tweet
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public Tweet showTweet(String[] args) {
    if (args.length < 2 || args[0] != "show") {
      throw new IllegalArgumentException(
          "\"USAGE: TwitterApp show tweet_id \\\"field1,fields2,...\\\"\"");
    }

    String id = args[1];
    String[] fields;

    if (args.length == 2) {
      fields = new String[]{};
    } else {
      fields = args[2].split(COMMA);
    }
    return service.showTweet(id, fields);

  }

  /**
   * Parse user argument and delete tweets by calling service classes
   *
   * @param args
   * @return a list of deleted tweets
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public List<Tweet> deleteTweet(String[] args) {

    if (args.length < 2 || args[0] != "delete") {
      throw new IllegalArgumentException("\"USAGE: TwitterApp delete tweet_id1, tweet_id2, ....");
    }

    String[] ids = args[1].split(COMMA);
    return service.deleteTweets(ids);

  }

}
