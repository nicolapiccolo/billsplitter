package it.unito.billsplitter

import android.app.Application
import com.parse.Parse
import com.parse.ParseInstallation
import com.parse.ParseUser
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction


class BillSplit: Application() {
    override fun onCreate() {
        super.onCreate()

        println("BILL SPLID")

        val config = CheckoutConfig(
            application = this,
            clientId = "AQRiH0Rfqavb0Pt8vyfN3RSbqVMnbden0nDOELTKegmkqRYK_1V0LOBCYD7-MK09fzo_OhAHf05nGU_v",
            environment = Environment.SANDBOX,
            returnUrl = "${BuildConfig.APPLICATION_ID}://paypalpay",
            currencyCode = CurrencyCode.EUR,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true
            )
        )
        PayPalCheckout.setConfig(config)

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