package com.epavlov.rosreestr.exception

import org.apache.http.HttpStatus

class NoClientException : Exception(), ApiException {
    override val text = "Клиент не найден"
    override val code = HttpStatus.SC_NOT_ACCEPTABLE
}