package it.unito.billsplitter.controller

import android.content.Context
import android.os.AsyncTask
import it.unito.billsplitter.model.Model

class LoadHistAsyncTask(context: Context) : AsyncTask<Boolean, Int, ArrayList<String>>() {

    //Boolean to force update

    private var listener: AsyncHistoryTaskListener? = null

    init {
        listener = context as AsyncHistoryTaskListener
    }

    /** The system calls this to perform work in a worker thread and delivers it the parameters given to AsyncTask.execute() */
    override fun doInBackground(vararg params: Boolean?): ArrayList<String>? {
        if(params[0] == true) return Model.instance.getMyHistory()
        else return Model.instance.getMyHistory()
    }

    override fun onProgressUpdate(vararg values: Int?) {
        listener?.giveProgress(values[0])
        // This is called on the UI thread when you call
        // publishProgress() from doInBackground()

    }

    /** The system calls this to perform work in the UI thread and delivers the result from doInBackground() */
    override fun onPostExecute(result: ArrayList<String>) {
        listener?.sendData(result)
    }
}
