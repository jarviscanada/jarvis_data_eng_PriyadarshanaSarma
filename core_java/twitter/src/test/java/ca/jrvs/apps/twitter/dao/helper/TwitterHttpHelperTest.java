package ca.jrvs.apps.twitter.dao.helper;

import com.google.gdata.util.common.base.PercentEscaper;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;


public class TwitterHttpHelperTest {

  @Test
  public void httpPost() throws Exception {
    String consumerkey = System.getenv("CONSUMERKEY");
    String consumerSecret = System.getenv("CONSUMERSECRET");
    String accessToken = System.getenv("ACCESSTOKEN");
    String tokenSecret = System.getenv("TOKENSECRET");

    System.out.println(consumerkey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);
    HttpHelper twitterHttpHelper = new TwitterHttpHelper(consumerkey, consumerSecret, accessToken,
        tokenSecret);
    String status = "Hello Twitter";
    PercentEscaper percentEscaper = new PercentEscaper("", false);
    HttpResponse response = twitterHttpHelper.httpPost(
        new URI("https://api.twitter.com/1.1/statuses/update.json?status="
            + percentEscaper.escape(status)));

    System.out.println(EntityUtils.toString(response.getEntity()));
  }

  @Test
  public void httpGet() throws Exception {
    String consumerkey = System.getenv("CONSUMERKEY");
    String consumerSecret = System.getenv("CONSUMERSECRET");
    String accessToken = System.getenv("ACCESSTOKEN");
    String tokenSecret = System.getenv("TOKENSECRET");

    System.out.println(consumerkey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);
    HttpHelper twitterHttpHelper = new TwitterHttpHelper(consumerkey, consumerSecret, accessToken,
        tokenSecret);
    HttpResponse response = twitterHttpHelper.httpGet(
        new URI(
            "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=Sarma_Priya1"));
    System.out.println(EntityUtils.toString(response.getEntity()));
  }
}