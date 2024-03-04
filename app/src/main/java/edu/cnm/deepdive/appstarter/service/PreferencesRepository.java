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

import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;
import dagger.hilt.android.qualifiers.ApplicationContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import kotlin.jvm.functions.Function1;

/**
 * Implements a {@link LiveData}-based repository of preferences. It does not provide any preference
 * mutation methods, and is of most use when all preferences are being modified through user
 * interaction with an instance of a {@link androidx.preference.PreferenceFragmentCompat} subclass
 * (such as {@link edu.cnm.deepdive.appstarter.controller.SettingsActivity.SettingsFragment}).
 */
@Singleton
public class PreferencesRepository {

  private final MutableLiveData<SharedPreferences> preferences;
  private final SharedPreferences prefs;

  @Inject
  PreferencesRepository(@ApplicationContext Context context) {
    prefs = PreferenceManager.getDefaultSharedPreferences(context);
    preferences = new MutableLiveData<>(prefs);
    prefs.registerOnSharedPreferenceChangeListener((prefs, key) -> preferences.postValue(prefs));
  }

  /**
   * Returns {@link LiveData} of all shared preferences that have been set in application code or by
   * the user in a {@link androidx.preference.PreferenceFragmentCompat} subclass instance. A
   * viewmodel should then use {@link androidx.lifecycle.Transformations#map(LiveData, Function1)}
   * (or a similar mechanism) to map the entire {@link SharedPreferences} instance in the
   * {@link LiveData} returned by this method to {@link LiveData} fields for individual
   * preferences.
   */
  public LiveData<SharedPreferences> getPreferences() {
    return preferences;
  }

  /**
   * Utility method to allow pass-through read access to the underlying {@link SharedPreferences},
   * by key. The compiler will infer the type parameter {@code T} by examining the
   * {@code defaultValue} and the reference type of the assignment target (if any); if the inferred
   * type is not one supported as a Shared
   * @param key
   * @param defaultValue
   * @return
   * @param <T>
   */
  public <T> T get(String key, T defaultValue) {
    //noinspection unchecked
    T result = (T) prefs.getAll().get(key);
    return (result != null) ? result : defaultValue;
  }

}
