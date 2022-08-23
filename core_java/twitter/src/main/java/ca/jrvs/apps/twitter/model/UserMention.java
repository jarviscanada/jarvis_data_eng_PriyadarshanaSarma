package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "name",
    "indices",
    "screen_name",
    "id",
    "id_str"
})
public class UserMention {

  @JsonProperty("name")
  private String name;

  @JsonProperty("indices")
  private int[] indices;

  @JsonProperty("screen_name")
  private String screen_name;

  @JsonProperty("id")
  private long id;
  @JsonProperty("id_str")
  private String id_str;

  public UserMention(String name, int[] indices, String screen_name, long id, String id_str) {
    this.name = name;
    this.indices = indices;
    this.screen_name = screen_name;
    this.id = id;
    this.id_str = id_str;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("indices")
  public int[] getIndices() {
    return indices;
  }

  @JsonProperty("indices")
  public void setIndices(int[] indices) {
    this.indices = indices;
  }

  @JsonProperty("screen_name")
  public String getScreen_name() {
    return screen_name;
  }

  @JsonProperty("screen_name")
  public void setScreen_name(String screen_name) {
    this.screen_name = screen_name;
  }

  @JsonProperty("id")
  public long getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(long id) {
    this.id = id;
  }

  @JsonProperty("id_str")
  public String getId_str() {
    return id_str;
  }

  @JsonProperty("id_str")
  public void setId_str(String id_str) {
    this.id_str = id_str;
  }
}
