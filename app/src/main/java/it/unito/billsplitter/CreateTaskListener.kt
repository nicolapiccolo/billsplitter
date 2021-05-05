package it.unito.billsplitter

interface CreateTaskListener {
    fun giveProgress(progress: Int?)
    fun sendData(result: Boolean)
}