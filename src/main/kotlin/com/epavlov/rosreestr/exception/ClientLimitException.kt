package com.epavlov.rosreestr.exception

import org.apache.http.HttpStatus

class ClientLimitException :Exception(), ApiException{
    override val text = "Закнчились регистрационные ключи"
    override val code = HttpStatus.SC_FORBIDDEN
}