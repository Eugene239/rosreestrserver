package com.epavlov.rosreestr.firebase

import com.epavlov.rosreestr.fromProperty
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseCredentials
import com.google.firebase.database.FirebaseDatabase

class FbRepository {
    init {
        try {
            val serviceAccount = this.javaClass.classLoader.getResourceAsStream("service.json")
            val options = FirebaseOptions.Builder()
                    .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                    .setDatabaseUrl("firebase.db".fromProperty())
                    .build()
            FirebaseApp.initializeApp(options)
        } catch (e: Exception) {
            e.printStackTrace()
            Runtime.getRuntime().exit(-3)
        }
    }

    val db = FirebaseDatabase.getInstance()
}