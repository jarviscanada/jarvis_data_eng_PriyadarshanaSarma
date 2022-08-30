package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.example.JSONParser;
import ca.jrvs.apps.twitter.model.Tweet;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TwitterDao constructs Twitter REST API URIs and make HTTP calls using HttpHelper
 */

public class TwitterDao implements CrdDao<Tweet, String> {

  //URI Constants
  private static final String API_BASE_URI = "https://api.twitter.com";
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy";

  //URI Symbols
  private static final String QUERY_SYM = "?";
  private static final String AMPERSAND = "&";
  private static final String EQUAL = "=";

  //Response code
  private static final int HTTP_OK = 200;

  private HttpHelper httpHelper;

  @Autowired
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  /**
   * Create an entity(Tweet) to the underlying storage
   *
   * @param tweet tweet that to be created
   * @return created entity
   */
  @Override
  public Tweet create(Tweet tweet) {

    URI uri;
    try {
      uri = getPostUri(tweet);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid tweet input", e);
    }

    HttpResponse response = httpHelper.httpPost(uri);
    return parseResponseBody(response, HTTP_OK);
  }

  private URI getPostUri(Tweet tweet) throws URISyntaxException {

    String status = tweet.getText();
    Double latitude = tweet.getCoordinates().getCoordinates().get(0);
    Double longitude = tweet.getCoordinates().getCoordinates().get(1);

    StringBuilder uriString = new StringBuilder();
    PercentEscaper percentEscaper = new PercentEscaper("", false);
    uriString.append(API_BASE_URI);
    uriString.append(POST_PATH);
    uriString.append(QUERY_SYM);
    uriString.append("status");
    uriString.append(EQUAL);
    uriString.append(percentEscaper.escape(status));
    uriString.append(AMPERSAND);
    uriString.append("latitude");
    uriString.append(EQUAL);
    uriString.append(latitude);
    uriString.append(AMPERSAND);
    uriString.append("longitude");
    uriString.append(EQUAL);
    uriString.append(longitude);
    return new URI(uriString.toString());
  }

  /**
   * Find an entity(Tweet) by its id
   *
   * @param id entity id
   * @return Tweet entity
   */
  @Override
  public Tweet findById(String id) {

    URI uri;
    try {
      uri = getGetUri(id);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    HttpResponse response = httpHelper.httpGet(uri);
    return parseResponseBody(response, HTTP_OK);
  }

  private URI getGetUri(String id) throws URISyntaxException {

    StringBuilder uriString = new StringBuilder();
    uriString.append(API_BASE_URI);
    uriString.append(SHOW_PATH);
    uriString.append(QUERY_SYM);
    uriString.append("id");
    uriString.append(EQUAL);
    uriString.append(id);
    return new URI(uriString.toString());
  }

  /**
   * Delete an entity(Tweet) by its ID
   *
   * @param id of the entity to be deleted
   * @return deleted entity
   */
  @Override
  public Tweet deleteById(String id) {

    URI uri;
    try {
      uri = getDelUri(id);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    HttpResponse response = httpHelper.httpPost(uri);
    return parseResponseBody(response, HTTP_OK);
  }

  private URI getDelUri(String id) throws URISyntaxException {

    StringBuilder uriString = new StringBuilder();
    uriString.append(API_BASE_URI);
    uriString.append(DELETE_PATH);
    uriString.append("/");
    uriString.append(id);
    uriString.append(".json");

    return new URI(uriString.toString());
  }

  /**
   * Check response status code and convert Response Entity to Tweet
   */
  public Tweet parseResponseBody(HttpResponse response, Integer expectedStatusCode) {

    Tweet tweet = null;

    int status = response.getStatusLine().getStatusCode();
    if (status != expectedStatusCode) {
      try {
        System.out.println(EntityUtils.toString(response.getEntity()));
      } catch (IOException e) {
        System.out.println("Response has no entity");
      }
      throw new RuntimeException("Unexpected HTTP status: " + status);
    }

    if (response.getEntity() == null) {
      throw new RuntimeException("Empty response body");
    }

    String jsonStr;
    try {
      jsonStr = EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      throw new RuntimeException("Failed to convert entity to String", e);
    }

    try {
      tweet = JSONParser.toObjectFromJson(jsonStr, Tweet.class);
    } catch (IOException e) {
      throw new RuntimeException("Unable to convert JSON String to Object", e);
    }

    return tweet;
  }

}
