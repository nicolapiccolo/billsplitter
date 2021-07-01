package it.unito.billsplitter


import it.unito.billsplitter.model.Split
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class SplitUnitTest {

    private val split = Split("test_split","70","","",null)

    @Test
    fun formatDate(){
        val date = Date(121,5,22)
        val result = Split.formatDate(date)
        assertEquals(
            "mar, 22 giu 2021",
            result
        )
    }

    @Test
    fun formatSingTotal(){
        val result = Split.formatTotal(split.total.toFloat(), true)
        assertEquals(
            "+€70,00",
            result
        )
    }

    @Test
    fun formatTotal(){
        val result = Split.formatTotal(split.total.toFloat(), false)
        assertEquals(
            "€70,00",
            result
        )
    }

    @Test
    fun getFormatFloat(){
        val result = Split.getFormatFloat(split.total)
        assertEquals(
            70.0F,
            result
        )
    }
}