package com.example

import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun playApplicationIdIsRegisteredPackage() {
    assertEquals("sa.sbtech.todo", BuildConfig.APPLICATION_ID)
    assertFalse(BuildConfig::class.java.declaredFields.any { it.name == "GEMINI_API_KEY" })
  }
}
