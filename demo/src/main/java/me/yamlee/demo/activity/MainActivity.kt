package me.yamlee.demo.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import com.tbruyelle.rxpermissions.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import me.yamlee.demo.BaseActivity
import me.yamlee.demo.R
import me.yamlee.jsbridge.AndroidJsBridge
import me.yamlee.jsbridge.utils.ToastUtil


class MainActivity : BaseActivity() {
    private var etInput: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidJsBridge
                .setDebug(true)
        etInput = this.findViewById(R.id.et_url_input)
        RxPermissions(this)
                .request(Manifest.permission.READ_PHONE_STATE)
                .subscribe { granted ->
                    if (granted) { // Always true pre-M
                        btnJumpWeb.setOnClickListener {
                            val url = etInput?.text.toString()
                            if (!TextUtils.isEmpty(url)) {
                                val intent = WebActivity.getIntent(url, this)
                                startActivity(intent)
                            } else {
                                val intent = Intent(this, WebActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    } else {
                        ToastUtil.showShort(applicationContext, "请先赋予权限")
                    }
                }
    }
}
