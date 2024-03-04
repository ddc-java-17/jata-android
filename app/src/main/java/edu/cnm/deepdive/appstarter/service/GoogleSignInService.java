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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Provides ReactiveX-integrated Google Sign In client flows for signing in, refreshing the bearer
 * token, and signing out. This class is implemented as a singleton, and the implementation is
 * thread-safe; however to minimize the risk of concurrency issues in the underlying Google Sign In
 * client library, this class should be consumed by a single viewmodel (e.g.
 * {@link edu.cnm.deepdive.appstarter.viewmodel.LoginViewModel}).
 */
@SuppressWarnings("unused")
@Singleton
public class GoogleSignInService {

  private static final String BEARER_TOKEN_FORMAT = "Bearer %s";

  private final GoogleSignInClient client;

  @Inject
  GoogleSignInService(@ApplicationContext Context context) {
    Resources res = context.getResources();
    @SuppressLint("DiscouragedApi")
    int identifier = res.getIdentifier("client_id", "string", context.getPackageName());
    String clientId = (identifier != 0) ? res.getString(identifier) : null;
    GoogleSignInOptions.Builder builder = new GoogleSignInOptions.Builder()
        .requestEmail()
        .requestId()
        .requestProfile();
    if (clientId != null && !clientId.isEmpty()) {
      builder.requestIdToken(clientId);
    }
    client = GoogleSignIn.getClient(context, builder.build());
  }

  /**
   * Constructs and returns a {@link Single Single&lt;GoogleSignInAccount&gt;} that, when executed
   * (subscribed to) refreshes the Google Sign In account credentials. This is of most use when a
   * bearer token is needed to access a web service application; however, in most cases, the
   * {@link #refreshBearerToken()} method is more convenient for that purpose.
   *
   * @return {@link Single Single&lt;GoogleSignInAccount&gt;} configured for downstream execution in
   * a thread pool managed by the {@link Schedulers#io()} scheduler.
   */
  public Single<GoogleSignInAccount> refresh() {
    return Single
        .create((SingleEmitter<GoogleSignInAccount> emitter) ->
            client
                .silentSignIn()
                .addOnSuccessListener(emitter::onSuccess)
                .addOnFailureListener(emitter::onError)
        )
        .observeOn(Schedulers.io());
  }

  /**
   * Constructs and returns a {@link Single Single&lt;String&gt;} that, when executed (subscribed
   * to) refreshes the Google Sign In account credentials and bearer token. This is primarily of use
   * when such a bearer token is needed to access a web service application. In fact, the result
   * delivered to the subscribed
   * {@link io.reactivex.rxjava3.functions.Consumer Consumer&lt;String&gt;} is suitable for use as
   * the value of the {@code Authorization} header of an HTTP request.
   *
   * @return {@link Single Single&lt;String&gt;} configured for downstream execution in a thread
   * pool managed by the {@link Schedulers#io()} scheduler.
   */
  public Single<String> refreshBearerToken() {
    return refresh()
        .map(this::getBearerToken);
  }

  /**
   * Sets the Google Sign In process flow in motion, using the specified
   * {@link ActivityResultLauncher ActivityResultLauncher&lt;Intent&gt;} as a callback.
   *
   * @param launcher Callback provided (indirectly) by the requesting {@link android.app.Activity}.
   */
  public void startSignIn(ActivityResultLauncher<Intent> launcher) {
    launcher.launch(client.getSignInIntent());
  }

  /**
   * Constructs and returns a {@link Single Single&lt;GoogleSignInAccount&gt;} that, when executed
   * (subscribed to), completes the Google Sign In process flow. If sign-in is successful, the
   * active {@link GoogleSignInAccount} is extracted from the provided {@link ActivityResult} and
   * delivered to the downstream
   * {@link io.reactivex.rxjava3.functions.Consumer Consumer&lt;GoogleSignInAccount&gt;}
   *
   * @param result {@link ActivityResult} passed to the
   *               {@link ActivityResultLauncher ActivityResultLauncher&lt;Intent&gt;} specified in
   *               the invocation of {@link #startSignIn(ActivityResultLauncher)}.
   * @return {@link Single Single&lt;GoogleSignInAccount&gt;} configured for downstream execution in
   * a thread pool managed by the {@link Schedulers#io()} scheduler.
   */
  public Single<GoogleSignInAccount> completeSignIn(ActivityResult result) {
    return Single
        .create((SingleEmitter<GoogleSignInAccount> emitter) -> {
          try {
            Task<GoogleSignInAccount> task =
                GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            GoogleSignInAccount account = task.getResult(ApiException.class);
            emitter.onSuccess(account);
          } catch (ApiException e) {
            emitter.onError(e);
          }
        })
        .observeOn(Schedulers.io());
  }

  /**
   * Constructs and returns a {@link Completable} that, when executed (subscribed to), signs the
   * current Google Sign In user from this app.
   *
   * @return {@link Completable} configured for downstream execution in a thread pool managed by the
   * {@link Schedulers#io()} scheduler.
   */
  public Completable signOut() {
    return Completable
        .create((emitter) ->
            client
                .signOut()
                .addOnSuccessListener((ignored) -> emitter.onComplete())
                .addOnFailureListener(emitter::onError)
        )
        .subscribeOn(Schedulers.io());
  }

  @NonNull
  private String getBearerToken(GoogleSignInAccount account) {
    return String.format(BEARER_TOKEN_FORMAT, account.getIdToken());
  }

}
