package it.unito.billsplitter.controller.task_inteface

interface UpdateDataListener {
    fun giveProgress(progress: Int?)
    fun sendData(result: Boolean)
}