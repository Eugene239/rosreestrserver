package com.epavlov.rosreestr.model

import com.epavlov.rosreestr.entity.Client
import com.epavlov.rosreestr.kodein
import com.epavlov.rosreestr.safe
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.lazy
import com.google.firebase.database.*
import org.apache.log4j.LogManager

class ClientModel {
    private val log = LogManager.getLogger(UserModel::class.java)
    private val path = "CLIENTS"

    private val db by kodein.lazy.instance<FirebaseDatabase>()
    val map = HashMap<String, Client>()

    init {
        db.getReference(path).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                safe {
                    p0?.let { snap ->
                        val t = object : GenericTypeIndicator<HashMap<String, Client>>() {}
                        val nMap = snap.getValue(t)
                        map.clear()
                        map.putAll(nMap)
                        log.debug("[CLIENTS UPDATED] $map")
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError?) {
                log.error(p0?.message)
            }
        })
    }
}
