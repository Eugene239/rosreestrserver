package com.epavlov.rosreestr

import com.google.gson.GsonBuilder
import org.apache.log4j.LogManager
import java.util.*

val gson = GsonBuilder().create()

fun safe(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        LogManager.getLogger(block.javaClass.toGenericString()).error(e)
    }
}

object Property {
    private val prop = Properties()

    init {
        this.javaClass.classLoader.getResourceAsStream("application.properties").use {
            prop.load(it)
        }
    }

    fun get(key: String): String? {
        return prop.getProperty(key)
    }
}

fun String.fromProperty(): String {
    return Property.get(this) ?: throw Exception("Property $this not found")
}
