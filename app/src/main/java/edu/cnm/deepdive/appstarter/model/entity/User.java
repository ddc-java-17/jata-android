/*
 *  Copyright 2024 CNM Ingenuity, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.cnm.deepdive.appstarter.model.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.time.Instant;

/**
 * Encapsulates the data elements maintained for a signed-in user in the app's Room/SQLite-based
 * persistent data store.
 */
@SuppressWarnings("unused")
@Entity(
    tableName = "user",
    indices = {
        @Index(value = "oauth_key", unique = true),
        @Index(value = "display_name", unique = true)
    }
)
public class User {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "user_id")
  private long id;

  @NonNull
  private Instant created = Instant.MIN;

  @ColumnInfo(name = "oauth_key")
  @NonNull
  private String oauthKey = "";

  @ColumnInfo(name = "display_name", collate = ColumnInfo.NOCASE)
  @NonNull
  private String displayName = "";

  // TODO Define additional fields as appropriate.

  /**
   * Returns the auto-generated unique identifier of this instance.
   */
  public long getId() {
    return id;
  }

  /**
   * Sets the unique identifier of this instance.
   *
   * @param id
   */
  @SuppressWarnings("JavadocDeclaration")
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Returns the timestamp of the INSERT of this user in the database.
   */
  @NonNull
  public Instant getCreated() {
    return created;
  }

  /**
   * Sets the INSERT timestamp of this user. This method should be invoked just prior to INSERT.
   *
   * @param created
   */
  @SuppressWarnings("JavadocDeclaration")
  public void setCreated(@NonNull Instant created) {
    this.created = created;
  }

  /**
   * Returns the fixed OAuth2.0 identifier (aka <em>subject</em>) of this user.
   */
  @NonNull
  public String getOauthKey() {
    return oauthKey;
  }

  /**
   * Sets the fixed OAuth2.0 identifier (aka <em>subject</em>) of this user.
   *
   * @param oauthKey
   */
  @SuppressWarnings("JavadocDeclaration")
  public void setOauthKey(@NonNull String oauthKey) {
    this.oauthKey = oauthKey;
  }

  /**
   * Returns unique display name of this user.
   */
  @NonNull
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Sets unique display name of this user.
   *
   * @param displayName
   */
  @SuppressWarnings("JavadocDeclaration")
  public void setDisplayName(@NonNull String displayName) {
    this.displayName = displayName;
  }

  // TODO Define additional getters and setters. These must be defined for any additional fields
  //  mapped to database columns.

  /**
   * Computes and returns the {@code int}-valued hash code of this instance. Currently, this
   * computation is based only on the primary key value.
   */
  @Override
  public int hashCode() {
    return Long.hashCode(id);
  }

  /**
   * Compares this instance to {@code obj} for equality and returns a {@code boolean} result.
   * Currently, this comparison is based only on the type of {@code obj}, and (if
   * {@code obj instanceof User}) the primary key values of {@code this} and {@code (User) obj}: two
   * instances of {@link User} are considered equal if and only if their {@code id} field values are
   * equal and non-zero.
   *
   * @param obj Object to compared to {@code this}.
   * @return {@code true} if {@code this} and {@code obj} should be considered equal; {@code false}
   * otherwise.
   */
  @Override
  public boolean equals(@Nullable Object obj) {
    boolean matched;
    if (obj == this) {
      matched = true;
    } else if (obj instanceof User) {
      User other = (User) obj;
      matched = (id != 0 && other.id != 0 && id == other.id);
    } else {
      matched = false;
    }
    return matched;
  }

}
