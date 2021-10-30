package dmitriy.molchanov

object Strings {
    const val EMPTY = ""
    const val DISABLE_CLEAR_INITIAL_OPTION =
        "For the CommitMessage plugin to work, you must disable the \"Clear initial commit message\" option <a href=\"enable\"> Disable </a>"
    const val SUCCESS_DISABLE = "Option has been disabled"
    const val FILL_FIELDS = "Fill in the fields"
    const val ADD_RULE = "Add rule"
    const val GIT_REPO = "Git repo:"
    const val REGEX_PREFIX = "Regex prefix:"
    const val CHECK_BRANCH = "Check branch:"
    const val REGISTER = "Register:"
    const val MESSAGE_PREFIX = "Message prefix:"
    const val MESSAGE_SUFFIX = "Message suffix:"
    const val GIT_REPO_WARNING = "Git repo is empty"
    const val REGEX_PREFIX_WARNING = "Regex prefix is empty"
    const val CHECK_BRANCH_WARNING = "Check string is empty"
    const val NO_MATCHES_FOUND = "No matches found"
    const val REGEX_RULE = "Regex prefix"
    const val REPOSITORY_TITLE = "Git repository"
    const val CHECK_STRING = "Check string"
    const val COMMIT_PLUGIN_SETTINGS = "Commit prefix plugin settings"
    const val LIKE_THIS_PLUGIN = "Like this plugin? Please "
    const val STAR_ON_GITHUB = "star on github."
    const val GITHUB_URL = "https://github.com/MolchanovDmitry/CommitPrefixIdeaPlugin"

    private const val REGISTER_NONE = "None"
    private const val REGISTER_LOWER = "Lover case"
    private const val REGISTER_UPPER = "Upper case"

    val registers by lazy(LazyThreadSafetyMode.NONE) { arrayOf(REGISTER_NONE, REGISTER_LOWER, REGISTER_UPPER) }
}