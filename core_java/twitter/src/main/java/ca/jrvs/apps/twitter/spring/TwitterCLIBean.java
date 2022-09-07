package ca.jrvs.apps.twitter.spring;

import ca.jrvs.apps.twitter.TwitterCLIApp;
import ca.jrvs.apps.twitter.controller.Controller;
import ca.jrvs.apps.twitter.controller.TwitterController;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

//@Configuration
public class TwitterCLIBean {

  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(TwitterCLIBean.class);
    TwitterCLIApp app = context.getBean(TwitterCLIApp.class);
    app.run(args);
  }

  @Bean
  public TwitterCLIApp twitterCLIApp(Controller controller) {
    return new TwitterCLIApp(controller);
  }

  @Bean
  public Controller twitterController(Service service) {
    return new TwitterController(service);
  }

  @Bean
  public Service twitterService(CrdDao dao) {
    return new TwitterService(dao);
  }

  @Bean
  public CrdDao crdDao(HttpHelper httpHelper) {
    return new TwitterDao(httpHelper);
  }

  @Bean
  HttpHelper helper() {
    String consumerkey = System.getenv("CONSUMERKEY");
    String consumerSecret = System.getenv("CONSUMERSECRET");
    String accessToken = System.getenv("ACCESSTOKEN");
    String tokenSecret = System.getenv("TOKENSECRET");

    return new TwitterHttpHelper(consumerkey, consumerSecret, accessToken,
        tokenSecret);

  }

}
