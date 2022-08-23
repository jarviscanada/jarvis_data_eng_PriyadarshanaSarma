package ca.jrvs.apps.twitter.dao.helper.util;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.List;

public class TwitterUtil {

  public static Tweet buildTweet(String text, Double latitude, Double longitude) {
    Tweet tweet = new Tweet();
    Coordinates coordinates = new Coordinates();
    tweet.setText(text);
    List<Double> coordinatesList = new ArrayList<>();
    coordinatesList.add(latitude);
    coordinatesList.add(longitude);
    coordinates.setCoordinates(coordinatesList);
    tweet.setCoordinates(coordinates);
    return tweet;
  }
}
