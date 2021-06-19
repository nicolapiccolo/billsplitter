package it.unito.billsplitter

import com.parse.ParseObject


interface LoadPayPalListener {
    fun giveProgress(progress: Int?)
    fun sendResult(result: Boolean)
}