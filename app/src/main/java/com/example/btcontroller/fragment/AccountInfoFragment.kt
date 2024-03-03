package com.example.btcontroller.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.btcontroller.R


class AccountInfoFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.account_info, rootKey)   //把account_info放进来
    }

}