package com.jyc.fast.result.launcher.plugin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.jyc.fast.result.launcher.annotations.FastLauncher
import com.jyc.fast.result.launcher.annotations.FastLauncherResult

class MainActivity : AppCompatActivity() {

    @FastLauncher
    lateinit var startActivityResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    @FastLauncherResult(
        launcherKey = "startActivityResult",
        launcherResultCodes = [RESULT_OK, RESULT_CANCELED, RESULT_FIRST_USER]
    )
    fun onStartActivityResult() {

    }
}