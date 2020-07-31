package main.kotlin.dmitriy.molchanov.model

import main.kotlin.dmitriy.molchanov.Strings
import java.io.Serializable

class Rule : Serializable {
    var gitRepo: String
    var regexPrefix: String
    var checkString: String

    @Suppress("unused") //serialization fix
    constructor() {
        gitRepo = Strings.EMPTY
        regexPrefix = Strings.EMPTY
        checkString = Strings.EMPTY
    }

    constructor(gitRepo: String,
                regexPrefix: String,
                checkString: String) {
        this.gitRepo = gitRepo
        this.regexPrefix = regexPrefix
        this.checkString = checkString
    }
}