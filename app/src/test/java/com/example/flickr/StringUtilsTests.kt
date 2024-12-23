package com.example.flickr

import com.example.flickr.utils.convertDate
import com.example.flickr.utils.nullToEmpty
import org.junit.Assert.assertEquals
import org.junit.Test

class StringUtilsTests {
    @Test
    fun `validate convertDate()`() {
        val dateToConvert = "2015-06-18T17:02:02Z"
        val convertedDate = "Jun 18, 2015 at 10:02"
        assertEquals(convertedDate, dateToConvert.convertDate())
    }

    @Test
    fun `validate nullToEmpty()`() {
        val nullString: String? = null
        assertEquals("", nullString.nullToEmpty())
    }
}