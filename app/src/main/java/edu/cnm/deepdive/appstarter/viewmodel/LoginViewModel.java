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
import android.content.Intent;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.appstarter.service.GoogleSignInService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javax.inject.Inject;

/**
 * Provides access (for a UI controller or data-bound view) to the current signed-in state and
 * {@link GoogleSignInAccount}. Additionally, the {@link #refresh()},
 * {@link #startSignIn(ActivityResultLauncher)}, {@link #completeSignIn(ActivityResult)}, and
 * {@link #signOut()} methods are provided to offload the sign-in flow from the UI controller.
 */
@HiltViewModel
public class LoginViewModel extends ViewModel implements DefaultLifecycleObserver {

  private final GoogleSignInService signInService;
  private final MutableLiveData<GoogleSignInAccount> account;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  @Inject
  LoginViewModel(@SuppressWarnings("unused") @ApplicationContext Context context,
      GoogleSignInService signInService) {
    this.signInService = signInService;
    account = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    refresh();
  }

  /**
   * Returns a reference to {@link LiveData LiveData&lt;GoogleSignInAccount&gt;} containing the
   * active {@link GoogleSignInAccount}, for observation by a UI controller (or a data-bound
   * {@link android.view.View}).
   *
   * @return
   */
  @SuppressWarnings("JavadocDeclaration")
  public LiveData<GoogleSignInAccount> getAccount() {
    return account;
  }

  /**
   * Returns a reference to {@link LiveData LiveData&lt;Throwable&gt;} containing the most recent
   * exception thrown by a ReactiveX task, for observation by a UI controller (or a data-bound
   * {@link android.view.View}). The contents of the {@link LiveData} are reset at the start of each
   * new ReactiveX task subscribed to by methods in this class.
   *
   * @return
   */
  @SuppressWarnings("JavadocDeclaration")
  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  /**
   * Starts the silent refresh/sign-in flow of Google Sign In. If an exception is thrown in this
   * phase of the Google Sign In flow, it is not treated as an error; instead, it simply indicates
   * that the silent refresh failed, and a Google Sign In screen must be presented to the user.
   */
  public void refresh() {
    throwable.setValue(null);
    signInService
        .refresh()
        .subscribe(
            account::postValue,
            (throwable) -> account.postValue(null),
            pending
        );
  }

  /**
   * Starts the visible Google Sign In flow, in which the standard Google Sign In form is presented
   * to the user.
   *
   * @param launcher Callback that will receive the result of the Google Sign In process.
   */
  public void startSignIn(ActivityResultLauncher<Intent> launcher) {
    signInService.startSignIn(launcher);
  }

  /**
   * Completes the Google Sign In flow, using the provided {@link ActivityResult} (passed to the
   * {@link ActivityResultLauncher} callback provided in the invocation of
   * {@link #startSignIn(ActivityResultLauncher)}).
   *
   * @param result Google Sign In {@link ActivityResult} passed to the
   *               {@link ActivityResultLauncher} callback.
   */
  public void completeSignIn(ActivityResult result) {
    throwable.setValue(null);
    signInService
        .completeSignIn(result)
        .subscribe(
            account::postValue,
            this::postThrowable,
            pending
        );
  }

  /**
   * Starts the sign-out process of the Google Sign In flow. Note that even if this process throws
   * an exception, the {@link GoogleSignInAccount} contained in the {@link LiveData} returned by
   * {@link #getAccount()} is cleared.
   */
  public void signOut() {
    throwable.setValue(null);
    signInService
        .signOut()
        .doFinally(() -> account.postValue(null))
        .subscribe(
            () -> {},
            this::postThrowable,
            pending
        );
  }

  @Override
  public void onStop(@NonNull LifecycleOwner owner) {
    DefaultLifecycleObserver.super.onStop(owner);
    pending.clear();
  }

  private void postThrowable(Throwable throwable) {
    Log.e(getClass().getSimpleName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

}
