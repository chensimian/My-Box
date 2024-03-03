package com.example.btcontroller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.btcontroller.R
import com.example.btcontroller.model.User
import com.google.android.material.textfield.TextInputEditText
import org.litepal.LitePal.beginTransaction
import org.litepal.LitePal.endTransaction
import org.litepal.LitePal.setTransactionSuccessful

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment(), View.OnClickListener {
    private var loginFragment: LoginFragment? = null
    private lateinit var btnRegist: Button
    private lateinit var etRegistUser: TextInputEditText
    private lateinit var etRegistPhone: TextInputEditText
    private lateinit var etRegistPsw: TextInputEditText
    private lateinit var etRegistPsw2: TextInputEditText
    private lateinit var registerMessage: TextView

    private lateinit var rootView: View
    private var currentFragment: Fragment? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_regist, container, false)
        initialize()
        btnRegist.setOnClickListener(this)
        return rootView
    }

    private fun initialize() {
        //获取界面控件实例
        btnRegist = rootView.findViewById(R.id.btn_regist)
        etRegistPsw = rootView.findViewById(R.id.et_registPsw)
        etRegistPsw2 = rootView.findViewById(R.id.et_registPsw2)
        etRegistUser = rootView.findViewById(R.id.et_registUser)
        etRegistPhone = rootView.findViewById(R.id.et_registPhone)
        registerMessage = rootView.findViewById(R.id.registerMessage)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_regist ->
                /**
                 * 注册
                 */
                if (register()) {
                    //注册成功
                    switchFragment(registerFragment, loginFragment)
                    registerMessage.text = "注册成功"
                    registerMessage.visibility = View.VISIBLE
                } else {
                    //注册失败
                    registerMessage.visibility = View.VISIBLE
                }
        }
    }

    fun switchFragment(from: Fragment?, to: Fragment?) {
        if (currentFragment !== to) {
            currentFragment = to
            if (!to!!.isAdded) {
                fragmentTransaction.hide(from!!).add(to, null).commit()
            } else {
                fragmentTransaction.hide(from!!).show(to).commit()
            }
        }
    }

    val fragmentTransaction: FragmentTransaction
        get() {
            val fragmentManager = requireActivity().supportFragmentManager
            return fragmentManager.beginTransaction()
        }

    private fun register(): Boolean {
        val username = etRegistUser.text.toString()
        if (username.isEmpty()) return false
        val phone = etRegistPhone.text.toString()
        if (phone.isEmpty()) return false
        val password = etRegistPsw.text.toString()
        if (password.isEmpty()) return false
        val confirm = etRegistPsw2.text.toString()
        if (confirm.isEmpty()) return false
        return if (password.compareTo(confirm) != 0) false else try {
            /**
             * 将用户名和密码写入数据库
             */
            val user = User()
            user.phone = phone
            user.username = username
            user.password = password
            beginTransaction()
            val res = user.save()
            if (res) setTransactionSuccessful()
            endTransaction()
            res
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    companion object {
        var registerFragment: RegisterFragment? = null
        fun newInstance(): RegisterFragment? {
            if (registerFragment == null) {
                registerFragment = RegisterFragment()
            }
            return registerFragment
        }
    }
}