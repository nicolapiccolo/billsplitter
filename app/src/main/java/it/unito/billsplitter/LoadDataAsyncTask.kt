package it.unito.billsplitter

import android.content.Context
import android.os.AsyncTask
import it.unito.billsplitter.model.Model
import it.unito.billsplitter.model.Split

class LoadDataAsyncTask(context: Context) : AsyncTask<Boolean, Int, ArrayList<Split>>() {

    //Boolean to force update

    private var listener: AsyncTaskListener? = null

    init {
        listener = context as AsyncTaskListener
    }

    /** The system calls this to perform work in a worker thread and delivers it the parameters given to AsyncTask.execute() */
    override fun doInBackground(vararg params: Boolean?): ArrayList<Split>? {
        if(params[0] == true) return Model.instance.getAllSplit()
        else{
            if(Model.instance.dataList.isEmpty()) return Model.instance.getAllSplit()
            else return Model.instance.dataList
        }
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

