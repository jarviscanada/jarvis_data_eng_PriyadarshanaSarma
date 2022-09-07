package ca.jrvs.apps.twitter.spring;

import ca.jrvs.apps.twitter.TwitterCLIApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * `@SpringBootApplication` is a convenience annotation that adds all the following:
 * <p>
 * - @Configuration - @ComponentScan - @EnableAutoConfiguration
 */
@SpringBootApplication(scanBasePackages = "ca.jrvs.apps.twitter")
public class TwitterCLISpringBoot implements CommandLineRunner {

  private TwitterCLIApp app;

  @Autowired
  public TwitterCLISpringBoot(TwitterCLIApp app) {
    this.app = app;
  }

  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(TwitterCLISpringBoot.class);
    //turn OFF web
    springApplication.setWebApplicationType(WebApplicationType.NONE);
    springApplication.run(args);
  }

  @Override
  public void run(String... args) throws Exception {
    app.run(args);
  }
}
