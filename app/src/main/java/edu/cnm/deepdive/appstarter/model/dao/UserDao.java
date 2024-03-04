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
package edu.cnm.deepdive.appstarter.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.appstarter.model.entity.User;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import java.util.Collection;
import java.util.List;

/**
 * Provides CRUD operations on {@link User} entity instances. {@code INSERT}, {@code UPDATE}, and
 * {@code DELETE} operations are implemented as ReactiveX {@link Single} tasks, which execute on
 * subscription; some {@code SELECT} tasks are implemented using {@link LiveData} queries, which
 * execute on observation, or (if already being observed) on Room-based updates to the underlying
 * tables; one {@code SELECT} ({@link #select(String)}) is implemented as a ReactiveX {@link Maybe}
 * task, to allow for the appropriate handling of an empty query result.
 */
@SuppressWarnings("unused")
@Dao
public interface UserDao {

  /**
   * Constructs and returns a {@link Single} that, when executed (subscribed to), inserts
   * {@code user} into the database and invokes the subscribing
   * {@link io.reactivex.rxjava3.functions.Consumer} with the auto-generated primary key value of
   * the inserted record.
   *
   * @param user {@link User} instance to be inserted.
   * @return {@link Single} that will (when subscribed to) insert {@code user} into the database.
   */
  @Insert
  Single<Long> insert(User user);

  /**
   * Constructs and returns a {@link Single} that, when executed (subscribed to), inserts
   * {@code users} into the database and invokes the subscribing
   * {@link io.reactivex.rxjava3.functions.Consumer} with a corresponding list of auto-generated
   * primary key values of the inserted records.
   *
   * @param users {@link User} instances to be inserted.
   * @return {@link Single} that will (when subscribed to) insert {@code users} into the database.
   */
  @Insert
  Single<List<Long>> insert(User... users);

  /**
   * Constructs and returns a {@link Single} that, when executed (subscribed to), inserts
   * {@code users} into the database and invokes the subscribing
   * {@link io.reactivex.rxjava3.functions.Consumer} with a corresponding list of auto-generated
   * primary key values of the inserted records.
   *
   * @param users {@link User} instances to be inserted.
   * @return {@link Single} that will (when subscribed to) insert {@code users} into the database.
   */
  @Insert
  Single<List<Long>> insert(Collection<User> users);

  /**
   * Constructs and returns a {@link Single} that, when executed (subscribed to), updates
   * {@code user} in the database and invokes the subscribing
   * {@link io.reactivex.rxjava3.functions.Consumer} with the number of records modified.
   *
   * @param user {@link User} instance to be updated.
   * @return {@link Single} that will (when subscribed to) update {@code user} in the database.
   */
  @Update
  Single<Integer> update(User user);

  /**
   * Constructs and returns a {@link Single} that, when executed (subscribed to), updates
   * {@code users} in the database and invokes the subscribing
   * {@link io.reactivex.rxjava3.functions.Consumer} with the number of records modified.
   *
   * @param users {@link User} instances to be updated.
   * @return {@link Single} that will (when subscribed to) update {@code users} in the database.
   */
  @Update
  Single<Integer> update(User... users);

  /**
   * Constructs and returns a {@link Single} that, when executed (subscribed to), updates
   * {@code users} in the database and invokes the subscribing
   * {@link io.reactivex.rxjava3.functions.Consumer} with the number of records modified.
   *
   * @param users {@link User} instances to be updated.
   * @return {@link Single} that will (when subscribed to) update {@code users} in the database.
   */
  @Update
  Single<Integer> update(Collection<User> users);

  /**
   * Constructs and returns a {@link Single} that, when executed (subscribed to), deletes
   * {@code user} from the database and invokes the subscribing
   * {@link io.reactivex.rxjava3.functions.Consumer} with the number of records modified.
   *
   * @param user {@link User} instance to be deleted.
   * @return {@link Single} that will (when subscribed to) delete {@code user} from the database.
   */
  @Delete
  Single<Integer> delete(User user);

  /**
   * Constructs and returns a {@link Single} that, when executed (subscribed to), deletes
   * {@code users} from the database and invokes the subscribing
   * {@link io.reactivex.rxjava3.functions.Consumer} with the number of records modified.
   *
   * @param users {@link User} instances to be deleted.
   * @return {@link Single} that will (when subscribed to) delete {@code users} in the database.
   */
  @Delete
  Single<Integer> delete(User... users);

  /**
   * Constructs and returns a {@link Single} that, when executed (subscribed to), deletes
   * {@code users} from the database and invokes the subscribing
   * {@link io.reactivex.rxjava3.functions.Consumer} with the number of records modified.
   *
   * @param users {@link User} instances to be deleted.
   * @return {@link Single} that will (when subscribed to) delete {@code users} in the database.
   */
  @Delete
  Single<Integer> delete(Collection<User> users);

  /**
   * Constructs and returns a {@link LiveData}-based query of a single user, using the primary key
   * value. When observed (or when the contents of the {@code user} table are modified using Room
   * methods), the query is executed.
   *
   * @param id Unique identifier (primary key value) of the {@link User} instance of interest.
   * @return {@link LiveData} that can be observed for the {@link User} instance of interest.
   */
  @Query("SELECT * FROM user WHERE user_id = :id")
  LiveData<User> select(long id);

  /**
   * Constructs and returns a ReactiveX-based query of a single user, using the fixed OAuth2.0
   * identifier (subject) of the {@link User} of interest. When executed (subscribed to), the query
   * is executed, and the result (if any) is passed to the subscribing
   * {@link io.reactivex.rxjava3.functions.Consumer}; if the query does not return a result, then
   * the subscribing {@link io.reactivex.rxjava3.functions.Action}.
   *
   * @param oauthKey OAuth2.0 identifier (subject) of the {@link User} instance of interest.
   * @return {@link Maybe} that can be executed (subscribed to) to obtain the {@link User} instance
   * of interest.
   */
  @Query("SELECT * FROM user WHERE oauth_key = :oauthKey")
  Maybe<User> select(String oauthKey);

  /**
   * Constructs and returns a {@link LiveData}-based query of all {@link User} instances, sorted by
   * display name. When observed (or when the contents of the {@code user} table are modified using
   * Room methods), the query is executed.
   */
  @Query("SELECT * FROM user ORDER BY display_name ASC")
  LiveData<List<User>> select();

}
