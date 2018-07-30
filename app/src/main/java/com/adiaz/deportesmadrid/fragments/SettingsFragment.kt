package com.adiaz.deportesmadrid.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.CheckBoxPreference
import android.support.v7.preference.EditTextPreference
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceScreen

import com.adiaz.deportesmadrid.R

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceScreen.sharedPreferences
                .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceScreen.sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
        addPreferencesFromResource(R.xml.pref_general)
        // Add visualizer preferences, defined in the XML file in res->xml->pref_visualizer

        val sharedPreferences = preferenceScreen.sharedPreferences
        val prefScreen = preferenceScreen
        val count = prefScreen.preferenceCount

        // Go through all of the preferences, and set up their preference summary.
        for (i in 0 until count) {
            val p = prefScreen.getPreference(i)
            // You don't need to set up preference summaries for checkbox preferences because
            // they are already set up in xml using summaryOff and summary On
            if (p !is CheckBoxPreference) {
                val value = sharedPreferences.getString(p.key, "")
                setPreferenceSummary(p, value)
            }
        }
    }

    /**
     * Updates the summary for the preference
     *
     * @param preference The preference to be updated
     * @param value      The value that the preference was updated to
     */
    private fun setPreferenceSummary(preference: Preference, value: String) {
        if (preference is ListPreference) {
            // For list preferences, figure out the label of the selected value
            val prefIndex = preference.findIndexOfValue(value)
            if (prefIndex >= 0) {
                // Set the summary to that label
                preference.summary = preference.entries[prefIndex]
            }
        } else if (preference is EditTextPreference) {
            // For EditTextPreferences, set the summary to the value's simple string representation.
            preference.setSummary(value)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        // Figure out which preference was changed
        val preference = findPreference(key)
        if (preference != null) {
            // Updates the summary for the preference
            if (preference !is CheckBoxPreference) {
                val value = sharedPreferences.getString(preference.key, "")
                setPreferenceSummary(preference, value)
            }
        }
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        return false
    }
}
