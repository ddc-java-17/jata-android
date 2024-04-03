package edu.cnm.deepdive.jata.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shot {

  @SerializedName("key")
  @Expose(serialize = false, deserialize = true)
  private final String id;


  public Shot(String id) {
    this.id = id;
  }
}
