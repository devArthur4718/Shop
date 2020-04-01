package com.stetter.escambo

import com.stetter.escambo.ui.register.RegisterViewModel
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private lateinit var viewModel : RegisterViewModel
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

//    @Test
//    fun testEmail(){
//        val valid = viewModel.isFormValid("devarthur4718@google.com")
//        assertTrue(valid)
//
//        val invalid = viewModel.isFormValid("not an email")
//        assertFalse(invalid)
//
//    }
}
