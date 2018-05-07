package com.epavlov.rosreestr.model

import com.epavlov.rosreestr.entity.User
import com.epavlov.rosreestr.kodein
import com.epavlov.rosreestr.safe
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.lazy
import com.google.firebase.database.*
import org.apache.log4j.LogManager
import kotlin.coroutines.experimental.suspendCoroutine


class UserModel {
    private val log = LogManager.getLogger(UserModel::class.java)

    private val path = "USERS"
    private val db by kodein.lazy.instance<FirebaseDatabase>()
    val map = HashMap<String, ArrayList<User>>()

    init {
        db.getReference(path).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                safe {
                    p0?.let { snap ->
                        val t = object : GenericTypeIndicator<HashMap<String, ArrayList<User>>>() {}
                        val nMap = snap.getValue(t)
                        map.clear()
                        map.putAll(nMap)
                        log.debug("[USERS UPDATED] $map")

                    }
                }
            }

            override fun onCancelled(p0: DatabaseError?) {
                log.error(p0?.message)
            }
        })
    }


    suspend fun save(user: User) = suspendCoroutine<ArrayList<User>> {
        val list = map[user.group] ?: ArrayList()
        if (!list.contains(user)) {
            list.add(user)
            db.getReference("$path/${user.group}").setValue(list) { error, ref ->
                it.resume(list)
            }
        } else {
            it.resumeWithException(Exception("User already exists"))
            log.error("User already Exists")
        }
    }
}
