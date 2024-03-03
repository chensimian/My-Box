package com.example.btcontroller.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.btcontroller.R
import com.example.btcontroller.activity.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class MoreFragment : Fragment() {
    private lateinit var phoneNumberText: TextView
    private lateinit var userNameText: TextView
    private lateinit var rootView: View
    private lateinit var genderImage: ImageView
    var sharedPreferences: SharedPreferences? = null
    private var username: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_more, container, false)
        phoneNumberText = rootView.findViewById(R.id.phone_number)
        userNameText = rootView.findViewById(R.id.username)
        genderImage = rootView.findViewById(R.id.gender)
        sharedPreferences = requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE)  //获取注册的名称
        phoneNumberText.text = MainActivity.user?.phone
        userNameText.text = MainActivity.user?.username
        //genderImage.setImageResource(R.mipmap.male_sign_128)
        //ImageViewCompat.setImageTintList(genderImage, ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.male_sign_color)))

        childFragmentManager.beginTransaction().replace(R.id.setting_container, SettingFragment()).commit()

        return rootView
    }

    companion object {
        var moreFragment: MoreFragment? = null
        fun newInstance(): MoreFragment? {
            // Required empty public constructor
            if (moreFragment == null) {
                moreFragment = MoreFragment()
            }
            return moreFragment
        }
    }
}