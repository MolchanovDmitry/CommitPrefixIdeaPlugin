package main.kotlin.dmitriy.molchanov.model

import java.io.Serializable

class Rule : Serializable {
    var gitRepo: String
    var regexPrefix: String
    var checkString: String

    @Suppress("unused") //serialization fix
    constructor() {
        gitRepo = ""
        regexPrefix = ""
        checkString = ""
    }

    constructor(gitRepo: String,
                regexPrefix: String,
                checkString: String) {
        this.gitRepo = gitRepo
        this.regexPrefix = regexPrefix
        this.checkString = checkString
    }
}