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
import android.content.SharedPreferences;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.appstarter.R;
import edu.cnm.deepdive.appstarter.service.PreferencesRepository;
import javax.inject.Inject;
import kotlin.jvm.functions.Function1;

/**
 * Provides access (for a UI controller or data-bound view) to some subset of the current
 * preferences state. While {@link PreferencesRepository} provides access to the full
 * {@link android.content.SharedPreferences} of the app, this class takes the approach of using
 * {@link Transformations#map(LiveData, Function1)} to provide {@link LiveData}-based read-only
 * access to individual preference values. This approach is of most use when an instance of a
 * {@link androidx.preference.PreferenceFragmentCompat} subclass is used to modify
 * {@link android.content.SharedPreferences}, and the preference values need to be used (but not
 * modified) in other UI controllers.
 */
@HiltViewModel
public class PreferencesViewModel extends ViewModel implements DefaultLifecycleObserver {

  private final LiveData<Boolean> selectableTextPreference;

  // TODO Declare additional LiveData fields for individual preferences as necessary.

  @Inject
  PreferencesViewModel(@ApplicationContext Context context, PreferencesRepository repository) {
    LiveData<SharedPreferences> prefs = repository.getPreferences();
    selectableTextPreference = selectableTextPreferenceLiveData(context, prefs);
    // TODO Initialize additional LiveData fields (as needed) for other individual preferences.
  }

  /**
   * Returns {@link LiveData} containing the current value of the selectable text preference,
   * presumably for observation by a UI controller or bound {@link android.view.View}.
   */
  public LiveData<Boolean> getSelectableTextPreference() {
    return selectableTextPreference;
  }

  private LiveData<Boolean> selectableTextPreferenceLiveData(Context context,
      LiveData<SharedPreferences> preferences) {
    String selectableTextPrefKey = context.getString(R.string.selectable_text_pref_key);
    boolean selectableTextPrefDefault = context
        .getResources()
        .getBoolean(R.bool.selectable_text_pref_default);
    return Transformations.map(preferences,
        (prefs) -> prefs.getBoolean(selectableTextPrefKey, selectableTextPrefDefault));
  }

  // TODO Following the example of the selectableTextPreferenceLiveData method, define additional
  //  methods (as needed) to initialize LiveData fields for other individual preferences.

}
