package it.unito.billsplitter.controller.task

import android.content.Context
import android.os.AsyncTask
import it.unito.billsplitter.controller.task_inteface.PaySplitListener
import it.unito.billsplitter.model.Model
import it.unito.billsplitter.model.User
import it.unito.billsplitter.view.fragment.Payment

class PaySplitAsyncTask () : AsyncTask<Payment, Int, Boolean>() {

    private var listener: PaySplitListener? = null


    override fun doInBackground(vararg params: Payment?): Boolean {
        val payment : Payment? = params[0]


        if(payment != null){
            //check se saldo disponibile
            if(Model.instance.checkBalancePayPal(payment.share, payment.account)){
                val split = Model.instance.getSplit(payment.id_split)
                return Model.instance.payWithPayPalTo(payment.share, payment.owner, User.getCurrentUser()!!, split!!)
            }
            else return false
        }
        else return false


    }

    fun setAsyncListener(listener: PaySplitListener?) {
        this.listener = listener
    }

    override fun onProgressUpdate(vararg values: Int?) {
        listener?.giveProgressPayment(values[0])
    }

    override fun onPostExecute(result: Boolean) {
        listener?.sendResultPayment(result)
    }
}