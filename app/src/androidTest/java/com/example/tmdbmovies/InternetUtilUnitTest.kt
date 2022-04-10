package com.example.tmdbmovies

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.tmdbmovies.tmdbutils.InternetUtil
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InternetUtilUnitTest {


    @Test
    fun check_InternetON() {
        assertTrue(InternetUtil.isInternetOn())
    }

}