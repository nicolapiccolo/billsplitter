package it.unito.billsplitter.controller.task

import android.content.Context
import android.os.AsyncTask
import it.unito.billsplitter.controller.task_inteface.CreateDataListener
import it.unito.billsplitter.model.Model
import it.unito.billsplitter.model.MySplit
import it.unito.billsplitter.model.SplitMember

class CreateDataAsyncTask(context: Context): AsyncTask<Any, Int, Boolean>(){
    private var listener: CreateDataListener? = null

    init {
        listener = context as CreateDataListener
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

    override fun doInBackground(vararg params: Any?): Boolean {
        val split = params[0]
        val member = params[1]

        if(split!=null && member!=null) return Model.instance.createSplit(split as MySplit, member as ArrayList<SplitMember>)
        else return false
    }
}