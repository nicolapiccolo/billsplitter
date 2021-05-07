package it.unito.billsplitter

import android.content.Context
import android.os.AsyncTask
import com.parse.ParseObject
import it.unito.billsplitter.model.Model

class DeleteDataAsyncTask (context: Context): AsyncTask<ParseObject, Int, Boolean>() {
    private var listener: UpdateTaskListener? = null

    init {
        listener = context as UpdateTaskListener
    }

    override fun onProgressUpdate(vararg values: kotlin.Int?) {
        listener?.giveProgress(values[0])
        // This is called on the UI thread when you call
        // publishProgress() from doInBackground()
    }

    /** The system calls this to perform work in the UI thread and delivers the result from doInBackground() */
    override fun onPostExecute(result: kotlin.Boolean) {
        listener?.sendData(result)
    }

    override fun doInBackground(vararg params: com.parse.ParseObject?): kotlin.Boolean {
        val split = params[0]

        if (split != null) {
            Model.instance.deleteSplit(split)
            Model.instance.deleteTransaction(split)
            return true
        } else return false
    }
}