package com.theblissprogrammer.kotlin.common.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.theblissprogrammer.kotlin.common.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by ahmed.saad on 2019-03-26.
 * Copyright © 2019. All rights reserved.
 */
abstract class BaseActivity : AppCompatActivity(), CoroutineScope {

    lateinit var job: Job
    override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_base)

        job = Job()
    }

    override fun onBackPressed() {
        supportFragmentManager.popBackStack()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item?.itemId


        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Cancel job on activity destroy. After destroy all children jobs will be cancelled automatically
        job.cancel()
    }
}
