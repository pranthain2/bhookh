package com.example.aahaarapp

class model {
    var name: String? = null
    var type: String? = null
    var description: String? = null
    var userid: String? = null

    constructor() {}
    constructor(name: String?, type: String?, description: String?, userid: String?) {
        this.name = name
        this.type = type
        this.description = description
        this.userid = userid
    }
}