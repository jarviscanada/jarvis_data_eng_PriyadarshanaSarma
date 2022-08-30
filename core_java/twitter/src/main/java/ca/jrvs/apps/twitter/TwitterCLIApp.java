package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.controller.Controller;
import ca.jrvs.apps.twitter.controller.TwitterController;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.example.JSONParser;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import com.fasterxml.jackson.core.JsonProcessingException;

public class TwitterCLIApp {

  public final static String USAGE = "USAGE: TwitterCLIApp post|show|delete [options]";

  private static Controller twitterController;

  public TwitterCLIApp(Controller twitterController) {
    this.twitterController = twitterController;
  }

  public static void main(String[] args) {
    String consumerkey = System.getenv("CONSUMERKEY");
    String consumerSecret = System.getenv("CONSUMERSECRET");
    String accessToken = System.getenv("ACCESSTOKEN");
    String tokenSecret = System.getenv("TOKENSECRET");

    System.out.println(consumerkey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);
    HttpHelper twitterHttpHelper = new TwitterHttpHelper(consumerkey, consumerSecret, accessToken,
        tokenSecret);

    CrdDao twitterDao = new TwitterDao(twitterHttpHelper);
    Service twitterService = new TwitterService(twitterDao);
    twitterController = new TwitterController(twitterService);

    TwitterCLIApp twitterCLIApp = new TwitterCLIApp(twitterController);
    twitterCLIApp.run(args);
  }

  public void run(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException(USAGE);
    }

    switch (args[0].toLowerCase()) {
      case "post":
        printTweet(twitterController.postTweet(args));
        break;
      case "show":
        printTweet(twitterController.showTweet(args));
        break;
      case "delete":
        twitterController.deleteTweet(args).forEach(this::printTweet);
        break;
      default:
        throw new IllegalArgumentException(USAGE);
    }
  }

  private void printTweet(Tweet tweet) {

    try {
      System.out.println(JSONParser.toJson(tweet, true, false));
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Unable to convert tweet object to string", e);
    }
  }

}
