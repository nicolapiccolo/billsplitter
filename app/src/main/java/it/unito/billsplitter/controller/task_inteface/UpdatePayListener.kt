package it.unito.billsplitter.controller.task_inteface

interface UpdatePayListener {
    fun giveProgress(progress: Int?)
    fun sendResult(result: Boolean)
}