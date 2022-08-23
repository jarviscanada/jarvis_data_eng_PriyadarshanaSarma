package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "hashtags",
    "userMentions",
})
public class Entities {

  @JsonProperty("hashtags")
  private Hashtag[] hashtags;

  @JsonProperty("userMentions")
  private UserMention[] userMentions;

  public Entities() {
  }

  public Entities(Hashtag[] hashtags, UserMention[] userMentions) {
    this.hashtags = hashtags;
    this.userMentions = userMentions;
  }

  @JsonProperty("hashtags")
  public Hashtag[] getHashtags() {
    return hashtags;
  }

  @JsonProperty("hashtags")
  public void setHashtags(Hashtag[] hashtags) {
    this.hashtags = hashtags;
  }

  @JsonProperty("userMentions")
  public UserMention[] getUserMentions() {
    return userMentions;
  }

  @JsonProperty("userMentions")
  public void setUserMentions(UserMention[] userMentions) {
    this.userMentions = userMentions;
  }
}
