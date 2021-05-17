package it.unito.billsplitter

import com.paypal.core.PayPalEnvironment
import com.paypal.core.PayPalHttpClient

class PayPalClient {
    private val environment: PayPalEnvironment = PayPalEnvironment.Sandbox(Config.PAYPAL_CLIENT_ID, Config.SECRET)

    /**
     * Returns PayPal HTTP client instance in an environment with access
     * credentials. Use this instance to invoke PayPal APIs, provided the
     * credentials have access.
     */

    var client: PayPalHttpClient = PayPalHttpClient(environment)

    /**
     * Method to get client object
     *
     * @return PayPalHttpClient client
     */
    fun client(): PayPalHttpClient? {
        return client
    }
}