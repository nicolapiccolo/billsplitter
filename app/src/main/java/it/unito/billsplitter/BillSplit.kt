package it.unito.billsplitter

import android.app.Application
import com.parse.Parse
import com.parse.ParseInstallation
import com.parse.ParseUser


class BillSplit: Application() {
    override fun onCreate() {
        super.onCreate()

        Parse.initialize(
                Parse.Configuration.Builder(this)
                        .applicationId("EO3Ee2rVBwYsL6PZnKdxA3IwXiPLb2vC4G9NyFMU")
                        .clientKey("YeWP1YMKaaIWO6e8auUMW9JI9txlxw6nJ7GJgZOZ")
                        .server("https://parseapi.back4app.com")
                        .build()
        )
        ParseUser.enableRevocableSessionInBackground()

        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);

        ParseInstallation.getCurrentInstallation().save()
    }
}