package com.epavlov.rosreestr.service

import com.epavlov.rosreestr.entity.User
import com.epavlov.rosreestr.exception.ClientLimitException
import com.epavlov.rosreestr.exception.NoClientException
import com.epavlov.rosreestr.kodein
import com.epavlov.rosreestr.model.ClientModel
import com.epavlov.rosreestr.model.UserModel
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.lazy
import kotlinx.coroutines.experimental.runBlocking
import org.apache.log4j.LogManager

class UserService {
    private val log = LogManager.getLogger(UserService::class.java)
    private val userModel = kodein.lazy.instance<UserModel>().value
    private val clientModel = kodein.lazy.instance<ClientModel>().value

    private fun getList(): List<User> {
        return userModel.map.map { it -> it.value }.flatten()
    }

    fun isRegistered(key: String): Boolean {
        return getList().map { user -> user.key }.contains(key)
    }

    @Throws(NoClientException::class, ClientLimitException::class)
    fun register(user: User) {
        //no such group
        if (!clientModel.map.keys.contains(user.group)) throw NoClientException()
        val client = clientModel.map[user.group]
        val users = userModel.map[user.group]?:ArrayList()
        if (client != null && client.maxSize!! > users.size) {
            runBlocking {
                userModel.save(user)
            }
        } else{
            log.error("client=$client, users=$users, user=$user")
            throw ClientLimitException()
        }
    }
}