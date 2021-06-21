package it.unito.billsplitter.controller

import android.content.Context
import android.os.AsyncTask
import com.parse.ParseObject
import com.parse.ParseUser
import it.unito.billsplitter.model.Model

class UpdatePayAsyncTask(context: Context, value: Boolean): AsyncTask<ParseObject, Int, Boolean>(){
    private var listener: UpdatePayListener? = null
    private var value: Boolean = true

    init {
        this.listener = context as UpdatePayListener
        this.value = value
    }

    override fun onProgressUpdate(vararg values: Int?) {
        listener?.giveProgress(values[0])
        // This is called on the UI thread when you call
        // publishProgress() from doInBackground()
    }
    /** The system calls this to perform work in the UI thread and delivers the result from doInBackground() */
    override fun onPostExecute(result: Boolean) {
        listener?.sendResult(result)
    }

    override fun doInBackground(vararg params: ParseObject?): Boolean {
        val split = params[0]
        val user = params[1]

        if(split!=null && user!=null){
            Model.instance.setPaid(split, user as ParseUser,this.value)
            return true
        }
        else return false
    }
}