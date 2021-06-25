package it.unito.billsplitter.controller.task_inteface

interface PaySplitListener {
    fun giveProgressPayment(progress: Int?)
    fun sendResultPayment(result: Boolean)
}