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

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.appstarter.service.PermissionsRepository;
import java.util.Set;
import javax.inject.Inject;

/**
 * Provides access (for a UI controller or data-bound view) to the current permissions
 * state&mdash;that is, to the set of permissions which have been granted by the user. Additionally,
 * the {@link #checkPermissions(Activity, Set, Set)
 * checkPermissions(Activity, Set&lt;String&gt;, Set&lt;String&gt;)} and
 * {@link #handlePermissionsRequestResult(String[], int[])} methods are provided to offload those
 * portions of the permissions flow from the UI controller.
 */
@HiltViewModel
public class PermissionsViewModel extends ViewModel implements DefaultLifecycleObserver {

  private final PermissionsRepository repository;

  @Inject
  PermissionsViewModel(@SuppressWarnings("unused") @ApplicationContext Context context,
      PermissionsRepository repository) {
    this.repository = repository;
  }

  /**
   * Returns {@link LiveData} containing the permissions granted, presumably for observation by a UI
   * controller or bound {@link android.view.View}.
   */
  public LiveData<Set<String>> getPermissions() {
    return repository.getPermissions();
  }

  /**
   * Initiates the permissions flow on behalf of a UI controller. <strong>This method is not
   * thread-safe!</strong> As a rule, it should only be invoked from a single {@link Activity} in
   * the app, and only on the main (UI) thread.
   *
   * @param activity             {@link Activity} initiating the flow and providing the
   *                             {@link Activity#onRequestPermissionsResult(int, String[], int[])}
   *                             callback.
   * @param permissionsToRequest Empty {@link Set Set&lt;String&gt;} of permissions, populated by
   *                             this method.
   * @param permissionsToExplain Empty {@link Set Set&lt;String&gt;} of permissions, populated by
   *                             this method.
   */
  public void checkPermissions(Activity activity,
      Set<String> permissionsToRequest, Set<String> permissionsToExplain) {
    repository.checkPermissions(activity, permissionsToRequest, permissionsToExplain);
  }

  /**
   * Handles response passed (indirectly) from
   * {@link Activity#onRequestPermissionsResult(int, String[], int[])}, adding all granted
   * permissions to a {@link LiveData LiveData&lt;Set&lt;String&gt;&gt;}.
   *
   * @param permissionsRequested {@link String String[]} containing all permissions requested of the
   *                             user by last invocation of
   *                             {@link Activity#requestPermissions(String[], int)}}.
   * @param grantResults         {@code int[]} of grant/deny results, in the same order as
   *                             {@code permissionsRequested}.
   */
  public void handlePermissionsRequestResult(
      @NonNull String[] permissionsRequested, @NonNull int[] grantResults) {
    repository.handlePermissionsRequestResult(permissionsRequested, grantResults);
  }

}
