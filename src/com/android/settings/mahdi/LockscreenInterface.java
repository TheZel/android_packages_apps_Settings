/*
 * Copyright (C) 2013 Mahdi-Rom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.mahdi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.view.Display;
import android.view.Window;
import android.widget.Toast;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import java.io.File;
import java.io.IOException;

public class LockscreenInterface extends SettingsPreferenceFragment implements
OnPreferenceChangeListener {

    private static final String TAG = "LockscreenInterface";

    private static final String KEY_ADDITIONAL_OPTIONS = "options_group";
    private static final String BATTERY_AROUND_LOCKSCREEN_RING = "battery_around_lockscreen_ring";
        
    private PreferenceCategory mAdditionalOptions;
    private CheckBoxPreference mLockRingBattery;

    private boolean mCheckPreferences;

    private Activity mActivity;
    private ContentResolver mResolver;
    private File wallpaperImage;
    private File wallpaperTemporary;

    public boolean hasButtons() {
        return !getResources().getBoolean(com.android.internal.R.bool.config_showNavigationBar);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        mResolver = mActivity.getContentResolver();              

        createCustomLockscreenView();
    }

    private PreferenceScreen createCustomLockscreenView() {
        mCheckPreferences = false;
        PreferenceScreen prefs = getPreferenceScreen();
        if (prefs != null) {
            prefs.removeAll();
        }

        addPreferencesFromResource(R.xml.lockscreen_interface_settings);
        prefs = getPreferenceScreen();

        mAdditionalOptions = (PreferenceCategory) prefs.findPreference(KEY_ADDITIONAL_OPTIONS);                

        mLockRingBattery = (CheckBoxPreference)findPreference(BATTERY_AROUND_LOCKSCREEN_RING);
        mLockRingBattery.setChecked(Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                Settings.System.BATTERY_AROUND_LOCKSCREEN_RING, 0) == 1);
                
        final int unsecureUnlockMethod = Settings.Secure.getInt(getActivity().getContentResolver(),
                Settings.Secure.LOCKSCREEN_UNSECURE_USED, 1);
        final int lockBeforeUnlock = Settings.Secure.getInt(getActivity().getContentResolver(),
                Settings.Secure.LOCK_BEFORE_UNLOCK, 0);

        //setup custom lockscreen customize view
        if ((unsecureUnlockMethod != 1 && lockBeforeUnlock == 0)
                 || unsecureUnlockMethod == -1) {             
        }
                        
        mCheckPreferences = true;
        return prefs;
    }

    @Override
    public void onResume() {
        super.onResume();
        createCustomLockscreenView();
    }

    @Override
    public void onPause() {
        super.onPause();        
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {        
        if (preference == mLockRingBattery) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.BATTERY_AROUND_LOCKSCREEN_RING, mLockRingBattery.isChecked() ? 1 : 0);    
        }
       return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (!mCheckPreferences) {
            return false;
        }

     return true;
                       
    }            
}
