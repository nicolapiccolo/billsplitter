package it.unito.billsplitter

interface AsyncHistoryTaskListener {
    fun giveProgress(progress: Int?)
    fun sendData(result: ArrayList<String>)
}