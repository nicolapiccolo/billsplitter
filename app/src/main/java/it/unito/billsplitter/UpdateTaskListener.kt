package it.unito.billsplitter

interface UpdateTaskListener {
    fun giveProgress(progress: Int?)
    fun sendData(result: Boolean)
}