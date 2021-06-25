package it.unito.billsplitter.controller.task_inteface

import com.parse.ParseObject


interface LoadPayPalListener {
    fun giveProgressLogin(progress: Int?)
    fun sendResult(result: Boolean)
}