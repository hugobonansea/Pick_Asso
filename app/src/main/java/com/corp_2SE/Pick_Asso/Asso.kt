package com.corp_2SE.Pick_Asso

class Asso {
    private var uid :String = ""
    private var name :String = ""
    private var description :String = ""
    private var bureau = emptyArray<Role>()
}

enum class Role {
    Président,
    Vice_Président,
    Trésorier,
    Secrétaire
}