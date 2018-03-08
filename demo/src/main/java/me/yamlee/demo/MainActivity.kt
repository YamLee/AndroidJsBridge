package me.yamlee.demo

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tbruyelle.rxpermissions.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import me.yamlee.jsbridge.utils.ToastUtil


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RxPermissions(this)
                .request(Manifest.permission.READ_PHONE_STATE)
                .subscribe({ granted ->
                    if (granted) { // Always true pre-M
                        btnJumpWeb.setOnClickListener {
                            val intent = Intent(this, WebActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        ToastUtil.showShort(applicationContext, "请先赋予权限")
                    }
                })
    }
}
