package com.epavlov.rosreestr.exception

interface ApiException {
    val text: String
    val code: Int
}