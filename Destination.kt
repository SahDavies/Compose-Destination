class Destination(
    routeId: String,
    private val count: Int = 0,
    val namedArgs: List<NamedNavArgument> = emptyList()
) {

    val route: String by lazy {
        buildString {
            append(routeId)
            if (namedArgs.isNotEmpty()) {
                append(namedArgs.joinToString(prefix = "?", separator = "&") { arg ->
                    "${arg.name}={${arg.name}}"
                }.trimEnd('&', '?'))
            }
        }
    }

    fun navRoute(block: Utility.() -> Unit = {}): String = Utility().apply(block).buildRoute()

    inner class Utility {
        private var requiredArgs: String = this@Destination.route.substringBefore('?')
        private var optionalArgs: String = ""
        fun requiredArgs(vararg args: String) = apply {
            if (this@Destination.count != args.size) return@apply
            requiredArgs = requiredArgs.substringBefore('/') + args.joinToString(prefix = "/", separator = "/").trimEnd('/')
        }

        fun optionalArgs(vararg args: String?) = apply {
            optionalArgs += this@Destination.namedArgs.zip(args).joinToString(prefix = "?", separator = "&") { (navArg, arg) ->
                "${navArg.name}=$arg"
            }.trimEnd('&', '?')
        }
        fun buildRoute(): String = requiredArgs + optionalArgs
    }
    class Builder(baseRoute: String) {
        private var count = 0   // holds tally for required argument
        private var requiredArgs: String = baseRoute
        private var namedNavArgs: List<NamedNavArgument> = emptyList()

        fun withRequiredArgs(vararg args: String) = apply {
            count = args.size
            requiredArgs += args.joinToString(prefix = "/", separator = "/") { arg ->
                "{${arg}}"
            }.trimEnd('/')
        }

        fun withOptionalArgs(vararg args: NamedNavArgument) = apply {
            namedNavArgs = args.toList()
        }

        fun build(): Destination = Destination(requiredArgs, count, namedNavArgs)
    }

    companion object {
        fun generateRoute(routeID: String, block: Builder.() -> Unit = {}): Destination =
            Builder(routeID).apply(block).build()
    }
}