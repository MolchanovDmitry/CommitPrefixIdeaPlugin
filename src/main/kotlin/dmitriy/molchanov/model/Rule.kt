package dmitriy.molchanov.model

import dmitriy.molchanov.Strings
import java.io.Serializable

/**
 * Правило для сохранения.
 *
 * @property gitRepo репозиторий, для которого актуально правило
 * @property regexPrefix регулярное выражение
 * @property checkString строка для проверки
 * @property startWith строка, которая будет подставляться в начало нашего префикса
 * @property endWith строка, которая будет подставляться в конец нашего префикса
 * @property isUpperCase перевести префикс в:
 *           true -> верхний регистр, false - нижний регистр, null -> не переводить.
 */
class Rule : Serializable {
    var gitRepo: String
    var regexPrefix: String
    var checkString: String
    var startWith: String
    var endWith: String
    var isUpperCase: Boolean?

    @Suppress("unused") // serialization fix
    constructor() {
        gitRepo = Strings.EMPTY
        regexPrefix = Strings.EMPTY
        checkString = Strings.EMPTY
        startWith = Strings.EMPTY
        endWith = Strings.EMPTY
        isUpperCase = null
    }

    constructor(
        gitRepo: String,
        regexPrefix: String,
        checkString: String,
        startWith: String,
        endWith: String,
        isUpperCase: Boolean?,
    ) {
        this.gitRepo = gitRepo
        this.regexPrefix = regexPrefix
        this.checkString = checkString
        this.startWith = startWith
        this.endWith = endWith
        this.isUpperCase = isUpperCase
    }
}
