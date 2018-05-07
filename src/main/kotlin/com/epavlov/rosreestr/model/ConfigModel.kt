package com.epavlov.rosreestr.model

import com.epavlov.rosreestr.entity.Config
import com.epavlov.rosreestr.kodein
import com.epavlov.rosreestr.safe
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.lazy
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.apache.log4j.LogManager

class ConfigModel {

    private val log = LogManager.getLogger(ConfigModel::class.java)
    private val db by kodein.lazy.instance<FirebaseDatabase>()
    private var config: Config? = null

    init {
        db.getReference(Config.PATH).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                log.error(p0?.toException())
            }

            override fun onDataChange(p0: DataSnapshot?) {
                p0?.let { snap ->
                    safe {
                        config = snap.getValue(Config::class.java)
                        log.debug("[CONFIG UPDATED] $config")
                    }
                }
            }
        })
    }

    fun getConfig(): Config? {
        return config
    }
}