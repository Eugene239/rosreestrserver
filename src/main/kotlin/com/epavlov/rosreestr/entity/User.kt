package com.epavlov.rosreestr.entity

data class User(val name: String? = "", val key: String? = "", val group: String? = "") {

    override fun toString(): String {
        return "User(name=$name, key=$key, group=$group)"
    }
}