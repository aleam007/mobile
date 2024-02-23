package com.mongodb.tasktracker.model

import io.realm.RealmList
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey
import java.util.UUID

open class User() : RealmObject() { // Primary constructor trống
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString() // Primary key
    var memberOf: RealmList<Project> = RealmList()

    var username: String = ""
    var email: String = ""

    // Secondary constructor
    constructor(username: String, email: String) : this() {
        this.username = username
        this.email = email
    }
}