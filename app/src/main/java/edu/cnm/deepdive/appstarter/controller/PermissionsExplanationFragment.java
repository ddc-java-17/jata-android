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
package edu.cnm.deepdive.appstarter.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.appstarter.R;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Presents a modal dialog of rationales for permissions requested by this app. No permissions are
 * actually requested in this fragment; instead, rationales for previously declined permissions are
 * read from string resources and displayed to the user. On user acknowledgement, control is passed
 * back to the host activity, which <strong>must</strong> implement the
 * {@link OnAcknowledgeListener} interface nested in this class.
 * <p>This fragment is intended to be instantiated and displayed via the Navigation framework, with
 * two {@link String String[]} arguments provided&mdash;one containing permissions to explain, and
 * the other containing permissions to request. (The latter are simply passed back to the host
 * activity, for subsequent invocation of
 * {@link androidx.core.app.ActivityCompat#requestPermissions(Activity, String[], int)}.)</p>
 * <p>Rationales for permissions being explained are read from string resources, identified by
 * taking the last component of the permission name, converting it to lower case, appending
 * "_explanation", and using the result as the name of a string resource. If no such resource
 * corresponding to a given permission exists, no rationale for that permission will be
 * displayed.</p>
 */
@AndroidEntryPoint
public class PermissionsExplanationFragment extends DialogFragment {

  private static final String EXPLANATION_RESOURCE_SUFFIX = "_explanation";

  private Context context;
  private String[] permissionsToExplain;
  private String[] permissionsToRequest;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = getContext();
    PermissionsExplanationFragmentArgs args =
        PermissionsExplanationFragmentArgs.fromBundle(getArguments());
    permissionsToExplain = args.getPermissionsToExplain();
    permissionsToRequest = args.getPermissionsToRequest();
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    String message = buildMessage();
    OnAcknowledgeListener listener = Objects.requireNonNull((OnAcknowledgeListener) getActivity());
    return new AlertDialog.Builder(context)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(R.string.permissions_dialog_title)
        .setMessage(message)
        .setNeutralButton(android.R.string.ok,
            (d, w) -> listener.onAcknowledge(permissionsToRequest))
        .create();
  }

  @Nullable
  private String buildMessage() {
    String packageName = context.getPackageName();
    Resources res = getResources();
    return Arrays
        .stream(permissionsToExplain)
        .map((permission) -> {
          String[] parts = permission.split("\\.");
          String permissionKey =
              parts[parts.length - 1].toLowerCase() + EXPLANATION_RESOURCE_SUFFIX;
          @SuppressLint("DiscouragedApi")
          int id = res.getIdentifier(permissionKey, "string", packageName);
          return (id != 0) ? getString(id) : null;
        })
        .filter(Objects::nonNull)
        .distinct()
        .collect(Collectors.joining("\n"));
  }

  /**
   * Declares the {@link #onAcknowledge(String[])} method to be implemented by the host activity
   * (typically using a lambda), to resume the permissions flow after display of the permission
   * rationales/explanations.
   */
  public interface OnAcknowledgeListener {

    /**
     * Invoked after user acknowledgement of permission rationales, to resume permissions flow.
     *
     * @param permissions Permissions to be requested of the user.
     */
    void onAcknowledge(String[] permissions);

  }

}
