package it.unito.billsplitter

import android.content.Context
import android.os.AsyncTask
import com.parse.ParseObject
import it.unito.billsplitter.fragment.AsyncTaskFragmentListener
import it.unito.billsplitter.model.Model
import it.unito.billsplitter.model.MySplit
import it.unito.billsplitter.model.Split
import it.unito.billsplitter.model.User


class  LoadFragmentAsyncTask(context: Context) : AsyncTask<ParseObject, Int, MySplit>()
    {
        //params,progress, result
        private var listener: AsyncTaskFragmentListener? = null

        init {
            listener = context as AsyncTaskFragmentListener
        }


        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: ParseObject?): MySplit? {

            val split = params[0]

            if(split!=null){


                val owner = Model.instance.getOwnerSplit(split)
                val name = Model.instance.getNameSplit(split)
                val total = Model.instance.getTotalSplit(split)
                val date = Model.instance.getDateSplit(split)

                var share = ""
                var percentage = 0
                if(owner.objectId.equals(User.getCurrentUser()?.objectId)){
                    val paid = Model.instance.getTotalPaidMySplit(split)
                    val have = Model.instance.getTotalHaveMySplit(split)

                    share = Split.formatTotal(paid,false)

                    percentage = Model.instance.getPercentagePaid(have,paid)
                }
                else  share = Model.instance.getMyShare(split)

                val listMember = Model.instance.getListMember(split)

                return MySplit(name,total,share,date,owner,listMember,percentage)
            }
            else return null
        }

        override fun onProgressUpdate(vararg values: Int?) {
            listener?.giveProgress(values[0])
        }

        override fun onPostExecute(result: MySplit) {
            listener?.sendData(result)
        }

    }
