package com.mrsworkshop.recipeapp.activity

import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mrsworkshop.recipeapp.component.LoadingDialog

open class BaseActivity : AppCompatActivity() {

    private lateinit var loadingViewDialog: LoadingDialog

    fun setStatusBarColor(color : Int) {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, color)
    }

    fun showLoadingViewDialog() {
        loadingViewDialog = LoadingDialog.show(supportFragmentManager)
    }

    fun dismissLoadingViewDialog() {
        loadingViewDialog.dismiss()
    }
}