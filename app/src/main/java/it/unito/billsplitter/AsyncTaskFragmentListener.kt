package it.unito.billsplitter

import it.unito.billsplitter.model.MySplit

interface AsyncTaskFragmentListener {
        fun giveProgress(progress: Int?)
        fun sendData(mySplit: MySplit)

}