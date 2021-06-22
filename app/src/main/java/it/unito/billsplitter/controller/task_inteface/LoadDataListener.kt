package it.unito.billsplitter.controller.task_inteface

import it.unito.billsplitter.model.Split

interface LoadDataListener {
    fun giveProgress(progress: Int?)
    fun sendData(list: ArrayList<Split>, give: String, have: String)
}