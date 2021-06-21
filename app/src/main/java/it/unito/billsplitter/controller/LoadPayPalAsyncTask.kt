package it.unito.billsplitter.controller

import android.content.Context
import android.os.AsyncTask
import it.unito.billsplitter.model.Model
import it.unito.billsplitter.model.PayPalAccount

class LoadPayPalAsyncTask(context: Context) : AsyncTask<PayPalAccount, Int, Boolean>() {

    //Boolean to force update

    private var listener: LoadPayPalListener? = null

    init {
        listener = context as LoadPayPalListener
    }

    override fun doInBackground(vararg params: PayPalAccount?): Boolean {
        val account : PayPalAccount? = params[0]
        if(account != null) {

            if(Model.instance.getPayPalAccount(account.email,account.password)!=null) return true
            else return false
        }
        else return false
    }

    override fun onProgressUpdate(vararg values: Int?) {
        listener?.giveProgress(values[0])
    }

    override fun onPostExecute(result: Boolean) {
        listener?.sendResult(result)
    }
}