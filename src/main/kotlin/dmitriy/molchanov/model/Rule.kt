package dmitriy.molchanov.model

import dmitriy.molchanov.Strings
import java.io.Serializable

class Rule : Serializable {
    var gitRepo: String
    var regexPrefix: String
    var checkString: String
    var startWith: String
    var endWith: String
    var register: String

    @Suppress("unused") //serialization fix
    constructor() {
        gitRepo = Strings.EMPTY
        regexPrefix = Strings.EMPTY
        checkString = Strings.EMPTY
        startWith = Strings.EMPTY
        endWith = Strings.EMPTY
        register = Strings.EMPTY
    }

    constructor(
        gitRepo: String,
        regexPrefix: String,
        checkString: String,
        startWith: String,
        endWith: String,
        register: String
    ) {
        this.gitRepo = gitRepo
        this.regexPrefix = regexPrefix
        this.checkString = checkString
        this.startWith = startWith
        this.endWith = endWith
        this.register = register
    }
}