package it.unito.billsplitter.controller.task_inteface

interface CreateDataListener {
    fun giveProgress(progress: Int?)
    fun sendData(result: Boolean)
}