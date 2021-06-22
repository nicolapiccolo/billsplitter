package it.unito.billsplitter.controller.task_inteface

import it.unito.billsplitter.model.MySplit

interface LoadFragmentListener {
        fun giveProgress(progress: Int?)
        fun sendData(mySplit: MySplit)

}