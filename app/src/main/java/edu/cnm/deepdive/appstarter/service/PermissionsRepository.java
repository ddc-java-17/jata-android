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

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import dagger.hilt.android.qualifiers.ApplicationContext;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implements a {@link LiveData}-based repository of permissions granted by the user, and handles
 * the low-level details of the permissions flow. Note that while this class is declared (using
 * {@link Singleton}) as a singleton, the {@link #checkPermissions(Activity, Set, Set)
 * checkPermissions(Activity, Set&lt;String&gt;, Set&lt;String&gt;)} and
 * {@link #handlePermissionsRequestResult(String[], int[])} methods are <em>not</em> thread-safe;
 * as a rule, those should be invoked only by an instance of
 * {@link edu.cnm.deepdive.appstarter.viewmodel.PermissionsViewModel}, which should in turn be
 * scoped to the main activity of the app.
 */
@Singleton
public class PermissionsRepository {

  private final Context context;
  private final MutableLiveData<Set<String>> permissions;

  @Inject
  PermissionsRepository(@SuppressWarnings("unused") @ApplicationContext Context context) {
    this.context = context;
    permissions = new MutableLiveData<>(new HashSet<>());
  }

  /**
   * Returns {@link LiveData} of all permissions declared in {@code AndroidManifest.xml} that have
   * been granted (implicitly or explicitly) to the app.
   */
  public LiveData<Set<String>> getPermissions() {
    return permissions;
  }

  public void checkPermissions(
      Activity activity, Set<String> permissionsToRequest, Set<String> permissionsToExplain) {
    try {
      Set<String> permissionsNeeded = getManifestPermissions();
      Set<String> permissionsGranted = permissions.getValue();
      queryPermissionGrants(activity,
          permissionsNeeded, permissionsGranted, permissionsToRequest, permissionsToExplain);
      permissions.postValue(permissionsGranted);
    } catch (NameNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Handles response passed (indirectly) from
   * {@link Activity#onRequestPermissionsResult(int, String[], int[])}, adding all granted
   * permissions to a {@link LiveData LiveData&lt;Set&lt;String&gt;&gt;}.
   *
   * @param permissionsRequested {@link String String[]} containing all permissions requested of the
   *                             user by last invocation of
   *                             {@link Activity#requestPermissions(String[], int)}.
   * @param grantResults         {@code int[]} of grant/deny results.
   * @noinspection DataFlowIssue
   */
  public void handlePermissionsRequestResult(
      @NonNull String[] permissionsRequested, @NonNull int[] grantResults) {
    Set<String> permissions = this.permissions.getValue();
    for (int i = 0; i < permissionsRequested.length; i++) {
      String permission = permissionsRequested[i];
      int result = grantResults[i];
      if (result == PackageManager.PERMISSION_GRANTED) {
        permissions.add(permission);
      } else {
        permissions.remove(permission);
      }
    }
    this.permissions.postValue(permissions);
  }

  private Set<String> getManifestPermissions() throws NameNotFoundException {
    PackageInfo info = context
        .getPackageManager()
        .getPackageInfo(context.getPackageName(),
            PackageManager.GET_META_DATA | PackageManager.GET_PERMISSIONS);
    return Stream.of(info.requestedPermissions)
        .collect(Collectors.toSet());
  }

  private void queryPermissionGrants(Activity activity,
      Set<String> permissionsNeeded, Set<String> permissionsGranted,
      Set<String> permissionsToRequest, Set<String> permissionsToExplain) {
    for (String permission : permissionsNeeded) {
      if (ContextCompat.checkSelfPermission(activity, permission)
          != PackageManager.PERMISSION_GRANTED) {
        permissionsToRequest.add(permission);
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
          permissionsToExplain.add(permission);
        }
      } else {
        permissionsGranted.add(permission);
      }
    }
  }

}
