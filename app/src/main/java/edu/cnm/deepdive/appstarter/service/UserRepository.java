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
package edu.cnm.deepdive.appstarter.service;

import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.appstarter.model.dao.UserDao;
import edu.cnm.deepdive.appstarter.model.entity.User;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.DisposableContainer;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Provides high-level operations on a repository of {@link User} instances, backed by a SQLite
 * database, in turn accessed via {@link LocalDatabase}. This class has no mutable state, and its
 * methods are thread-safe; however, mutating operations (involving {@code INSERT}, {@code UPDATE},
 * or {@code DELETE} on the underlying database) attempted simultaneously could block or fail.
 */
@SuppressWarnings("unused")
@Singleton
public class UserRepository {

  private final UserDao userDao;
  private final GoogleSignInService signInService;

  /**
   * Initializes this instance by establishing a logical connection to the underlying database.
   */

  @Inject
  UserRepository(UserDao userDao, GoogleSignInService signInService) {
    this.userDao = userDao;
    this.signInService = signInService;
  }

  /**
   * Constructs and returns a {@link Single} that, when executed (subscribed to), retrieves the
   * {@link User} instance for the current signed-in user from the database, and passes it to the
   * subscribing {@link io.reactivex.rxjava3.functions.Consumer}.
   */
  public Single<User> getCurrent() {
    return getOrCreate().subscribeOn(Schedulers.io());
  }

  /**
   * Returns a {@link LiveData}-based query for the {@link User} entity instance identified by
   * {@code id}. The query executes when observed, or (if already being observed) whenever the
   * contents of the underlying {@code user} table are modified using Room data-access methods.
   *
   * @param id Unique identifier (primary key value) of a {@link User} entity instance.
   * @return {@link LiveData}-based query for the {@link User} identified by {@code id}.
   */
  public LiveData<User> get(long id) {
    return userDao.select(id);
  }

  /**
   * Returns a {@link LiveData}-based query of all {@link User} entity instances. The query executes
   * when observed, or (if already being observed) whenever the contents of the underlying
   * {@code user} table are modified using Room data-access methods.
   */
  public LiveData<List<User>> getAll() {
    return userDao.select();
  }

  /**
   * Constructs and returns a {@link Single} task that, when executed (subscribed to), will insert
   * or update the specified {@code user} in the database, and pass the updated {@link User}
   * instance to the subscribing {@link io.reactivex.rxjava3.functions.Consumer}. The specific
   * persistence operation is determined by examining the value returned by {@link User#getId()}: a
   * value of zero (0) indicates the {@code user} has not yet been {@code INSERT}ed and must be; a
   * non-zero value indicates that {@code user} is already in the database, and must thus be
   * {@code UPDATE}d.
   *
   * @param user Instance of {@link User} entity class to be persisted (inserted or updated) in the
   *             database.
   * @return {@link Single} task that will persist {@code user} to the database when executed
   * (subscribed to).
   */
  public Single<User> save(User user) {
    return (
        (user.getId() == 0)
            ? insert(user)
            : update(user)
    )
        .subscribeOn(Schedulers.io());
  }

  /**
   * Constructs and returns a {@link Completable} task that, when executed (subscribed to), will
   * remove the specified {@code user} from the database, if present, and invoke the subscribing
   * {@link io.reactivex.rxjava3.functions.Action} on completion. Whether the deletion is even
   * necessary is determined by examining the value returned by {@link User#getId()}: a value of
   * zero (0) indicates the {@code user} has not been inserted into the database, and thus need not
   * be deleted; a non-zero value indicates that {@code user} is in the database and must be
   * {@code DELETE}d.
   * <p>If an attempt is made to delete the {@link User} entity instance corresponding to the
   * current signed-in user, the {@link Completable} will fail, with an
   * {@link IllegalStateException} passed to the
   * {@link io.reactivex.rxjava3.functions.Consumer Consumer&lt;Throwable&gt;} specified in
   * {@link Completable#subscribe(Action, Consumer, DisposableContainer)}.</p>
   *
   * @param user Instance of {@link User} entity class to be deleted from the database.
   * @return {@link Completable} task that will delete {@code user} from the database when executed
   * (subscribed to).
   */
  public Completable delete(User user) {
    return (
        (user.getId() == 0)
            ? Completable.complete()
            : checkSafeDelete(user)
                .flatMap(userDao::delete)
                .ignoreElement()
    )
        .subscribeOn(Schedulers.io());
  }

  private Single<User> getOrCreate() {
    return signInService
        .refresh()
        .flatMap((account) -> userDao.select(account.getId())
            .switchIfEmpty(
                Single
                    .fromSupplier(() -> {
                      User user = new User();
                      user.setOauthKey(Objects.requireNonNull(account.getId()));
                      user.setDisplayName(Objects.requireNonNull(account.getDisplayName()));
                      return user;
                    })
                    .flatMap(this::insert)
            )
        );
  }

  private Single<User> insert(User user) {
    user.setCreated(Instant.now());
    return userDao
        .insert(user)
        .map((id) -> {
          user.setId(id);
          return user;
        });
  }

  private Single<User> update(User user) {
    return userDao
        .update(user)
        .map((count) -> user);
  }

  private Single<User> checkSafeDelete(User user) {
    return getOrCreate()
        .map((u) -> {
          if (u.equals(user)) {
            throw new IllegalStateException("");
          }
          return u;
        });
  }

}
