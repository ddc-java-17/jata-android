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
package edu.cnm.deepdive.appstarter.viewmodel;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.appstarter.model.entity.User;
import edu.cnm.deepdive.appstarter.service.UserRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.List;
import javax.inject.Inject;

/**
 * Provides access (for a UI controller or data-bound view) to the {@link User} entity instance
 * corresponding to the current signed-in user, a user specified by its unique identifier (primary
 * key value), and the full collection of users who have signed in to the app. This access includes
 * persistence operations on individual {@link User} entity instances.
 */
@SuppressWarnings("unused")
@HiltViewModel
public class UserViewModel extends ViewModel implements DefaultLifecycleObserver {

  private final UserRepository repository;
  private final MutableLiveData<Long> currentUserId;
  private final LiveData<User> currentUser;
  private final MutableLiveData<Long> userId;
  private final LiveData<User> user;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  @Inject
  UserViewModel(@ApplicationContext Context context, UserRepository repository) {
    this.repository = repository;
    currentUserId = new MutableLiveData<>();
    currentUser = Transformations.switchMap(currentUserId, repository::get);
    userId = new MutableLiveData<>();
    user = Transformations.switchMap(userId, repository::get);
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    fetchCurrentUser();
  }

  /**
   * Returns {@link LiveData} containing the {@link User} entity instance for the current signed-in
   * user.
   */
  public LiveData<User> getCurrentUser() {
    return currentUser;
  }

  /**
   * Sets the unique identifier (primary key value) of the {@link User} entity instance to be
   * retrieved by the {@link LiveData}-based query returned by {@link #getUser()}. Any
   * {@link androidx.lifecycle.Observer observers} of the latter will be invoked (asynchronously) as
   * a result of invoking this method.
   *
   * @param userId
   */
  @SuppressWarnings("JavadocDeclaration")
  public void setUserId(long userId) {
    this.userId.setValue(userId);
  }

  /**
   * Returns {@link LiveData} containing the {@link User} entity instance specified in the most
   * recent invocation of {@link #setUserId(long)}. If there are any
   * {@link androidx.lifecycle.Observer observers} of the result returned by this method, the
   * retrieval will be performed automatically on invocation of {@link #setUserId(long)}, and the
   * observers will be invoked (asynchronously) with the result.
   */
  public LiveData<User> getUser() {
    return user;
  }

  /**
   * Returns the {@link LiveData} containing the full collection of {@link User} entity instances
   * corresponding to all users who have signed in to the app (or at least, all users who have been
   * signed in when this class has been instantiated).
   */
  public LiveData<List<User>> getUsers() {
    return repository.getAll();
  }

  /**
   * Adds or updates (asynchronously) the {@link User} entity instance referenced by {@code user} in
   * the app's persistent data store. If any of the {@link LiveData} instances returned by
   * {@link #getCurrentUser()}, {@link #getUser()}, or {@link #getUsers()} are being observed, the
   * underlying retrievals will be re-executed (and the
   * {@link androidx.lifecycle.Observer observers} invoked) automatically as an asynchronous
   * consequence of invoking this method.
   *
   * @param user Instance of the {@link User} entity class to be added to or updated in the
   *             persistent data store.
   */
  public void save(User user) {
    repository
        .save(user)
        .subscribe(
            (u) -> {
            },
            this::postThrowable,
            pending
        );
  }

  /**
   * Removes (asynchronously) the {@link User} entity instance referenced by {@code user} from the
   * app's persistent data store. If any of the {@link LiveData} instances returned by
   * {@link #getCurrentUser()}, {@link #getUser()}, or {@link #getUsers()} are being observed, the
   * underlying retrievals will be re-executed (and the
   * {@link androidx.lifecycle.Observer observers} invoked) automatically as an asynchronous
   * consequence of invoking this method.
   *
   * @param user Instance of the {@link User} entity class to be removed from the persistent data
   *             store.
   */
  public void delete(User user) {
    repository
        .delete(user)
        .subscribe(
            () -> {
            },
            this::postThrowable,
            pending
        );
  }

  @Override
  public void onStop(@NonNull LifecycleOwner owner) {
    DefaultLifecycleObserver.super.onStop(owner);
    pending.clear();
  }

  private void fetchCurrentUser() {
    repository
        .getCurrent()
        .subscribe(
            (user) -> currentUserId.postValue(user.getId()),
            this::postThrowable,
            pending
        );
  }

  private void postThrowable(Throwable throwable) {
    Log.e(getClass().getSimpleName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

}
