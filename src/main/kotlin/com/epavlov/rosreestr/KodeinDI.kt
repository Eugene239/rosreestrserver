package com.epavlov.rosreestr

import com.epavlov.rosreestr.firebase.FbRepository
import com.epavlov.rosreestr.model.ClientModel
import com.epavlov.rosreestr.model.ConfigModel
import com.epavlov.rosreestr.model.UserModel
import com.epavlov.rosreestr.service.UserService
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import com.google.firebase.database.FirebaseDatabase

val kodein = Kodein {
    // model block
    bind<FirebaseDatabase>() with singleton { FbRepository().db }
    bind<UserModel>() with singleton { UserModel() }
    bind<ConfigModel>() with singleton { ConfigModel() }
    bind<ClientModel>() with singleton { ClientModel() }

    bind<UserService>() with singleton { UserService() }
}