package it.unito.billsplitter

import android.content.Context
import android.os.AsyncTask
import it.unito.billsplitter.model.Model
import it.unito.billsplitter.model.Split

class LoadDataAsyncTask(context: Context) : AsyncTask<Void, Int, ArrayList<Split>>() {

    private var listener: AsyncTaskListener? = null

    init {
        listener = context as AsyncTaskListener
    }

    /** The system calls this to perform work in a worker thread and delivers it the parameters given to AsyncTask.execute() */
    override fun doInBackground(vararg params: Void?): ArrayList<Split>? {
        return Model.instance.getAllSplit()
    }


    override fun onProgressUpdate(vararg values: Int?) {
        listener?.giveProgress(values[0])
        // This is called on the UI thread when you call
        // publishProgress() from doInBackground()

    }

    /** The system calls this to perform work in the UI thread and delivers the result from doInBackground() */
    override fun onPostExecute(result: ArrayList<Split>) {
        listener?.sendData(result,Model.instance.getGive(),Model.instance.getHave())
    }
}

