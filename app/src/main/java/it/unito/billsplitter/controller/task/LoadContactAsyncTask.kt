package it.unito.billsplitter.controller.task

import android.content.Context
import android.os.AsyncTask
import com.parse.ParseUser
import it.unito.billsplitter.controller.task_inteface.LoadContactListener
import it.unito.billsplitter.model.Contact
import it.unito.billsplitter.model.Model

class LoadContactAsyncTask(context: Context) : AsyncTask<ArrayList<Contact>, Int, ArrayList<ParseUser>>() {

    //Boolean to force update

    private var listener: LoadContactListener? = null

    init {
        listener = context as LoadContactListener
    }

    /** The system calls this to perform work in a worker thread and delivers it the parameters given to AsyncTask.execute() */
    override fun doInBackground(vararg params: ArrayList<Contact>): ArrayList<ParseUser> {
        val contactList: ArrayList<Contact> = params[0]
        return Model.instance.joinContacts(contactList)
    }


    override fun onProgressUpdate(vararg values: Int?) {
        listener?.giveProgress(values[0])
        // This is called on the UI thread when you call
        // publishProgress() from doInBackground()

    }

    /** The system calls this to perform work in the UI thread and delivers the result from doInBackground() */
    override fun onPostExecute(result: ArrayList<ParseUser>) {
        listener?.sendData(result)
    }
}