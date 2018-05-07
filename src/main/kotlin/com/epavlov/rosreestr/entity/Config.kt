package com.epavlov.rosreestr.entity

data class Config(val email: String? = "email") {

    companion object {
        const val PATH = "CONFIG"
    }
}