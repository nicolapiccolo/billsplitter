package it.unito.billsplitter.controller.task_inteface

interface LoadHistListener {
    fun giveProgress(progress: Int?)
    fun sendData(result: ArrayList<String>)
}