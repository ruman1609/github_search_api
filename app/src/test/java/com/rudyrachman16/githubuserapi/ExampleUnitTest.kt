package com.rudyrachman16.githubuserapi

import org.junit.Test

import org.junit.Assert.*
import java.text.DecimalFormat
import java.text.NumberFormat

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private fun bindNumber(num: Int, decimalFormat: DecimalFormat): String {
        var length: Int = num.toString().length - 1
        var result: Double = num.toDouble()
        println(result)
        if (length > 3) {
            var index = 1
            while (length >= 3) {
                println("ASW")
                index *= 1000
                length -= 3
            }
            result = num.toDouble() / index
            println(result)
        }
        return decimalFormat.format(result)
    }

    private fun groupNum(num: Int, decimalFormat : DecimalFormat): String = when {
        num.toString().length - 1 >= 9 -> bindNumber(num, decimalFormat) + "B"
        num.toString().length - 1 in 6..8 -> bindNumber(num, decimalFormat) + "M"
        num.toString().length - 1 in 4..5 -> bindNumber(num, decimalFormat) + "K"
        else -> decimalFormat.format(num)
    }

    val numberFormat = NumberFormat.getNumberInstance()
    val decimalFormat = (numberFormat as DecimalFormat).apply {
        isGroupingUsed = true
        applyPattern("#,###.#")
    }

    @Test
    fun testOutput(){
//        val a = 1000000000
//        println(groupNum(a, decimalFormat))
        val a = booleanArrayOf(true,true,true)
        a.forEach{
            if(!it)
                return
        }
        println("BENAR SEMUA")
    }
}