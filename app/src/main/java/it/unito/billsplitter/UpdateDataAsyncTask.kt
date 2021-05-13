package it.unito.billsplitter

import android.content.Context
import android.os.AsyncTask
import com.parse.ParseObject
import it.unito.billsplitter.model.Model
import it.unito.billsplitter.model.Split

class UpdateDataAsyncTask(context: Context): AsyncTask<ParseObject, Int, Boolean>(){
    private var listener: UpdateTaskListener? = null

    init {
        listener = context as UpdateTaskListener
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

    override fun doInBackground(vararg params: ParseObject?): Boolean {
        val split = params[0]

        if(split!=null){
            Model.instance.closeSplit(split)
            return true
        }
        else return false
    }
}