package org.wit.hillfort.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import org.wit.hillfort.R
import org.wit.hillfort.views.hillfortlist.HillfortListView

class SplashScreenActivity : AppCompatActivity()
{

    private var delayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 1500

    internal val runnable: Runnable = Runnable {
        if(!isFinishing)
        {
            val intent = Intent(applicationContext, HillfortListView::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        delayHandler = Handler()
        delayHandler!!.postDelayed(runnable, SPLASH_DELAY)
    }

    public override fun onDestroy()
    {
        if(delayHandler != null)
        {
            delayHandler!!.removeCallbacks(runnable)
        }
        super.onDestroy()
    }
}
