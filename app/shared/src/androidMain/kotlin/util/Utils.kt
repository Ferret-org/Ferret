package util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle


private lateinit var appContext: Context

var currentActivity: Activity? = null
    private set

val applicationContext: Context
    get() = if (::appContext.isInitialized) appContext
    else throw IllegalStateException("applicationContext not initialized. Call initApplicationContext() in Application.onCreate()")

fun isApplicationContextInitialized(): Boolean = ::appContext.isInitialized

fun initApplicationContext(context: Context) {
    if (!::appContext.isInitialized) {
        appContext = context.applicationContext
        (appContext as? Application)?.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })

    }
}

