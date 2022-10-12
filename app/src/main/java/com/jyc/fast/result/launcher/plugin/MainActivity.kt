package com.jyc.fast.result.launcher.plugin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.jyc.fast.result.launcher.annotations.FastLauncher
import com.jyc.fast.result.launcher.annotations.FastLauncherResult

class MainActivity : AppCompatActivity() {
    companion object {
        const val LAUNCHER_START_ACTIVITY_RESULT = "startActivityResult"
        const val LAUNCHER_START_PhOTO_RESULT = "startPhotoResult"
    }


    @FastLauncher
    lateinit var startActivityResult: ActivityResultLauncher<Intent>

    @FastLauncher
    lateinit var startPhotoResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivityResult.launch(Intent(this,UserActivity::class.java))
    }

    @FastLauncherResult(
        launcherKey = LAUNCHER_START_ACTIVITY_RESULT,
        launcherResultCodes = [RESULT_OK,RESULT_CANCELED,]
    )
    fun onStartActivityResult() {

    }

    @FastLauncherResult(
        launcherKey = LAUNCHER_START_ACTIVITY_RESULT,
        launcherResultCodes = [RESULT_FIRST_USER]
    )
    fun onStartActivityResultFirstUser() {

    }

    @FastLauncherResult(
        launcherKey = LAUNCHER_START_PhOTO_RESULT,
        launcherResultCodes = [RESULT_OK, RESULT_CANCELED, RESULT_FIRST_USER]
    )
    fun onStartActivityPhotoResult() {

    }


}