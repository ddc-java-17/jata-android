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

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.appstarter.R;
import edu.cnm.deepdive.appstarter.databinding.FragmentDemoBinding;
import edu.cnm.deepdive.appstarter.model.entity.User;
import edu.cnm.deepdive.appstarter.viewmodel.LoginViewModel;
import edu.cnm.deepdive.appstarter.viewmodel.PermissionsViewModel;
import edu.cnm.deepdive.appstarter.viewmodel.PreferencesViewModel;
import edu.cnm.deepdive.appstarter.viewmodel.UserViewModel;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Demonstrates access to and observation of {@link androidx.lifecycle.LiveData} elements in
 * {@link LoginViewModel}, {@link UserViewModel}, {@link PermissionsViewModel}, and
 * {@link PreferencesViewModel}, as well as acting as a navigation placeholder. This fragment can be
 * used as an example for creating other navigable fragments that access these core viewmodels; it
 * can then be evolved to provide more application-specific utility, or removed/replaced
 * altogether.
 */
@AndroidEntryPoint
public class DemoFragment extends Fragment {

  private static final Pattern PERMISSION_NAME_PATTERN = Pattern.compile("(?<=\\.)[^.]*$");

  private FragmentDemoBinding binding;
  private UserViewModel userViewModel;
  private User user;
  @ColorInt
  private int saveColor;
  @ColorInt
  private int cancelColor;
  @ColorInt
  private int disabledColor;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    Context context = getContext();
    //noinspection DataFlowIssue
    loadColors(context);
    binding = FragmentDemoBinding.inflate(inflater, container, false);
    setupUI();
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ViewModelProvider provider = new ViewModelProvider(requireActivity());
    LifecycleOwner owner = getViewLifecycleOwner();
    setupLoginViewModel(provider, owner);
    setupUserViewModel(provider, owner);
    setupPermissionsViewModel(provider, owner);
    setupPreferencesViewModel(provider, owner);
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  private void loadColors(Context context) {
    saveColor = context.getColor(R.color.save);
    cancelColor = context.getColor(R.color.cancel);
    disabledColor = context.getColor(R.color.disabled);
  }

  private void setupUI() {
    binding.localDisplayName.addTextChangedListener(new DisplayNameWatcher());
    binding.save.setOnClickListener((v) -> saveChanges());
    binding.cancel.setOnClickListener((v) -> cancelChanges());
  }

  private void setupLoginViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    provider
        .get(LoginViewModel.class)
        .getAccount()
        .observe(owner, this::handleAccount);
  }

  private void setupUserViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    userViewModel = provider.get(UserViewModel.class);
    userViewModel
        .getCurrentUser()
        .observe(owner, this::handleUser);
  }

  private void setupPermissionsViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    provider
        .get(PermissionsViewModel.class)
        .getPermissions()
        .observe(owner, this::handlePermissions);
  }

  private void setupPreferencesViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    provider
        .get(PreferencesViewModel.class)
        .getSelectableTextPreference()
        .observe(owner, this::handleSelectableTextPreference);
  }

  private void handleAccount(GoogleSignInAccount account) {
    if (account != null) {
      binding.googleDisplayName.setText(account.getDisplayName());
      if (account.getIdToken() != null && !account.getIdToken().isEmpty()) {
        binding.bearerToken.setText(account.getIdToken());
        binding.bearerTokenLayout.setVisibility(View.VISIBLE);
      } else {
        binding.bearerTokenLayout.setVisibility(View.GONE);
      }
    }
  }

  private void handleUser(User user) {
    this.user = user;
    binding.localDisplayName.setText(user.getDisplayName());
    checkSaveConditions();
  }

  private void handlePermissions(Set<String> permissions) {
    binding.permissions.setText(
        permissions
            .stream()
            .map(PERMISSION_NAME_PATTERN::matcher)
            .filter(Matcher::find)
            .map(Matcher::group)
            .collect(Collectors.joining(", "))
    );
  }

  private void handleSelectableTextPreference(Boolean selectable) {
    if (selectable) {
      binding.googleDisplayName.setEnabled(true);
      binding.googleDisplayName.setTextIsSelectable(true);
      binding.bearerToken.setEnabled(true);
      binding.googleDisplayName.setTextIsSelectable(true);
      binding.permissions.setEnabled(true);
      binding.permissions.setTextIsSelectable(true);
    } else {
      binding.googleDisplayName.setEnabled(false);
      binding.googleDisplayName.setTextIsSelectable(false);
      binding.bearerToken.setEnabled(false);
      binding.googleDisplayName.setTextIsSelectable(false);
      binding.permissions.setEnabled(false);
      binding.permissions.setTextIsSelectable(false);
    }
  }

  private void checkSaveConditions() {
    String input = Objects.requireNonNull(binding.localDisplayName.getText())
        .toString()
        .trim();
    if (user != null
        && !input.isEmpty()
        && !input.equals(user.getDisplayName())
    ) {
      binding.save.setColorFilter(saveColor);
      binding.save.setEnabled(true);
      binding.cancel.setColorFilter(cancelColor);
      binding.cancel.setEnabled(true);
    } else {
      binding.save.setColorFilter(disabledColor);
      binding.save.setEnabled(false);
      binding.cancel.setColorFilter(disabledColor);
      binding.cancel.setEnabled(false);
    }
  }

  private void saveChanges() {
    user.setDisplayName(
        Objects.requireNonNull(binding.localDisplayName.getText()).toString().trim());
    userViewModel.save(user);
  }

  private void cancelChanges() {
    binding.localDisplayName.setText(user.getDisplayName());
  }

  private class DisplayNameWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
      // Intentionally left empty; no action required.
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
      // Intentionally left empty; no action required.
    }

    @Override
    public void afterTextChanged(Editable editable) {
      checkSaveConditions();
    }

  }

}
