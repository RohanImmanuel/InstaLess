package com.rohanimmanuel.instaless

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private lateinit var webView: WebView
    private val handler = Handler(Looper.getMainLooper())
    private val closeDelay: Long = 15 * 60 * 1000 // 15 minutes
    private val coolOffPeriod: Long = 5 * 60 * 1000 // 2 minutes
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val lastClosedTime = sharedPreferences.getLong("last_closed_time", 0)
        val currentTime = System.currentTimeMillis()
        val timeLeft = currentTime - lastClosedTime

        if (timeLeft < coolOffPeriod) {
            // Cool-off period is active, do not launch
            finish() // Close the app immediately
            showToast(convertMillisToMinutesSeconds(timeLeft) + " Left")
            return
        }

        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webview)

        // Enable JavaScript
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.textZoom = 120

        // Set WebViewClient
        webView.webViewClient = WebViewClient()

        // Enable Cookie Persistence
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(webView, true) // Optional, but often needed

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.black)

        // Load your URL
        webView.loadUrl("https://www.instagram.com") // Replace with your URL

        // Schedule app closure
        scheduleAppClosure()
    }

    private fun scheduleAppClosure() {
        handler.postDelayed( {
            finishAffinity() // Close the app
            val editor = sharedPreferences.edit()
            editor.putLong("last_closed_time", System.currentTimeMillis())
            editor.apply()
            showToast("See you in 5min")
        }, closeDelay)
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun convertMillisToMinutesSeconds(millis: Long): String? {
        val seconds = millis / 1000 % 60
        val minutes = millis / (1000 * 60) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
