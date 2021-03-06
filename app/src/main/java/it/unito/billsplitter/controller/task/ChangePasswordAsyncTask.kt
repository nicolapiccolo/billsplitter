package it.unito.billsplitter.controller.task

import android.content.Context
import android.os.AsyncTask
import it.unito.billsplitter.controller.task_inteface.UpdateDataListener
import it.unito.billsplitter.model.Model


class ChangePasswordAsyncTask(context: Context): AsyncTask<String, Int, Boolean>(){
    private var listener: UpdateDataListener? = null

    init {
        listener = context as UpdateDataListener
    }

    override fun onProgressUpdate(vararg values: Int?) {
        listener?.giveProgress(values[0])
        // This is called on the UI thread when you call
        // publishProgress() from doInBackground()
    }

    /** The system calls this to perform work in the UI thread and delivers the result from doInBackground() */
    override fun onPostExecute(result: Boolean) {
        listener?.sendData(result)
    }

    override fun doInBackground(vararg params: String?): Boolean {
        val newpassword = params[0]
        if (newpassword!=null){
            Model.instance.changePassword(newpassword)
            return true
        }
        else return false
    }
}