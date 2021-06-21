package it.unito.billsplitter.controller

import com.parse.ParseObject


interface LoadPayPalListener {
    fun giveProgress(progress: Int?)
    fun sendResult(result: Boolean)
}