package com.example.btcontroller.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.btcontroller.activity.MainActivity
import com.example.btcontroller.R
import com.example.btcontroller.model.User
import org.litepal.LitePal.where

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment(), View.OnClickListener {
    private lateinit var btnLogin: Button
    private lateinit var etLoginPsw: EditText
    private lateinit var ivSwichPasswrod: ImageView
    private lateinit var etLoginUser: EditText
    private lateinit var loginFailed: TextView
    private lateinit var v: View
    private lateinit var user: User
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_login, container, false)
        initialize()
        btnLogin.setOnClickListener(this)
        return v
    }

    private fun initialize() {
        //获取界面控件实例
        btnLogin = v.findViewById(R.id.btn_login)
        etLoginPsw = v.findViewById(R.id.et_loginPsw)
        etLoginUser = v.findViewById(R.id.et_loginUser)
        loginFailed = v.findViewById(R.id.loginFailed)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_login ->
                /**
                 * 登录
                 */
                if (login()) {
                    //成功
                    loginFailed.visibility = View.VISIBLE
                    loginFailed.text = "登录成功"
                    val intent = Intent(activity, MainActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    sharedPreferences = requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    //步骤3：将获取过来的值放入文件
                    editor.putBoolean("isLogin", true)
                    editor.putString("username", user.username)
                    //步骤4：提交
                    editor.apply()
                    requireActivity().finish()
                } else {
                    //失败
                    loginFailed.visibility = View.VISIBLE
                }
        }
    }

    private fun login(): Boolean {
        /**
         * 查询数据库，匹配用户名密码
         */
        val username = etLoginUser.text.toString()
        if (username.isEmpty()) return false
        val password = etLoginPsw.text.toString()
        return if (password.isEmpty()) false else try {
            val list = where("(username like ? or phone like ?) and password like ?", username, username, password).find(User::class.java) //数据库查询
            if (list.size == 0) return false
            val user = list[0]
            if ((user.username?.compareTo(username) == 0 || user.phone?.compareTo(username) == 0) && user.password?.compareTo(password) == 0) {
                this.user = user
                true
            } else false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    companion object {
        var loginFragment: LoginFragment? = null
        fun newInstance(): LoginFragment? {
            if (loginFragment == null) {
                loginFragment = LoginFragment()
            }
            return loginFragment
        }
    }
}