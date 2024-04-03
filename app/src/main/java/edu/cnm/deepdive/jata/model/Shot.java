package edu.cnm.deepdive.jata.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import edu.cnm.deepdive.jata.model.entity.Board;
import edu.cnm.deepdive.jata.model.entity.User;

public class Shot {

  @SerializedName("key")
  @Expose(serialize = false, deserialize = true)
  private final String id;

  // TODO: 4/3/2024 Field for toUser or who the Shot was fired against.

  private final int shotCoordX;

  private final int shotCoordY;


  public Shot(String id, int shotCoordX, int shotCoordY) {
    this.id = id;
    this.shotCoordX = shotCoordX;
    this.shotCoordY = shotCoordY;
  }
}
