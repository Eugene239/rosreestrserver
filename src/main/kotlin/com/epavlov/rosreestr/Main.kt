package com.epavlov.rosreestr

import com.epavlov.rosreestr.entity.User
import com.epavlov.rosreestr.exception.ClientLimitException
import com.epavlov.rosreestr.exception.NoClientException
import com.epavlov.rosreestr.model.ConfigModel
import com.epavlov.rosreestr.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.salomonbrys.kodein.*
import io.javalin.ApiBuilder.*
import io.javalin.Javalin
import io.javalin.translator.json.JavalinJacksonPlugin
import org.apache.http.HttpStatus
import org.slf4j.LoggerFactory


class Server {
    private val log = LoggerFactory.getLogger("API")
    private val app = Javalin.create().port(8090)

    private val userService = kodein.lazy.instance<UserService>().value
    private val configModel = kodein.lazy.instance<ConfigModel>().value

    init {
        JavalinJacksonPlugin.configure(ObjectMapper())
        app.routes {
            path("users") {
                post { context ->
                    log.info("[POST] ${context.body()}")
                    safe {
                        val user = context.bodyAsClass(User::class.java)
                        log.info(user.toString())
                        try {
                            userService.register(user)
                            context.status(200)
                        } catch (e: ClientLimitException) {
                            log.error("${e.text} $user")
                            context.status(e.code)
                        } catch (e: NoClientException) {
                            log.error("${e.text} $user")
                            context.status(e.code)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            context.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                        }
                    }
                    //context.status(HttpStatus.SC_NOT_ACCEPTABLE)
                }
                get { context ->
                    context.queryParam("key")?.let { key ->
                        if (userService.isRegistered(key)) {
                            context.json(key)
                            context.status(HttpStatus.SC_OK)
                            return@get
                        } else {
                            log.info("UNAUTHORIZED ${context.request().remoteAddr}")
                            context.status(HttpStatus.SC_UNAUTHORIZED)
                            return@get
                        }
                    }
                    log.info("ERROR REQUEST ${context.request().remoteAddr}")
                    context.status(HttpStatus.SC_NOT_FOUND)
                }

            }
            path("config") {
                get { it.json(configModel.getConfig()!!) }
            }
        }
    }

    fun start() {

        app.start()
    }

}

fun main(args: Array<String>) {
    Server().start()
}

