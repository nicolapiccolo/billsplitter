package it.unito.billsplitter

import it.unito.billsplitter.model.Contact
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito


class ContactUnitTest {

    private val contact = Contact(
            name = "Mark",
            number = "+(333)4445556"
    )


    @Test
    fun converFormatPhone() {
        val result = Contact.converFormatPhone(contact.number)
        assertEquals(
                "3334445556",
                result
        )
    }
}