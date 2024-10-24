private class Destinations(
    routeId: String,
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

    fun navRoute(block: Builder.() -> Unit = {}): String =
        Builder(baseRoute = route.substringBefore('?'), namedNavArgs = namedArgs)
            .apply(block)
            .buildRoute()

    class Builder(
        baseRoute: String,
        private var namedNavArgs: List<NamedNavArgument> = emptyList()
    ) {

        private var requiredArgs: String = baseRoute
        private var optionalArgs: String = ""

        fun requiredArgs(vararg args: String) = apply {
            /**
             * TODO: Add safety if no required arg is passed or is incomplete
             * TODO: also if function is called but route wasn't instantiated with required arg
             * */
            requiredArgs = requiredArgs.substringBefore('/') + args.joinToString(prefix = "/", separator = "/") { arg ->
                "{$arg}"
            }.trimEnd('/')
        }

        fun withRequiredArgs(vararg args: String) = apply {
            requiredArgs += args.joinToString(prefix = "/", separator = "/") { arg ->
                "{${arg}}"
            }.trimEnd('/')
        }

        fun withOptionalArgs(vararg args: NamedNavArgument) = apply {
            namedNavArgs = args.toList()
        }

        fun optionalArgs(vararg args: String?) = apply {
            optionalArgs += namedNavArgs.zip(args).joinToString(prefix = "?", separator = "&") { (navArg, arg) ->
                "${navArg.name}=$arg"
            }.trimEnd('&', '?')
        }

        fun build(): Destinations = Destinations(requiredArgs, namedNavArgs)

        fun buildRoute(): String = requiredArgs + optionalArgs
    }

    companion object {
        fun generateRoute(routeID: String, block: Builder.() -> Unit = {}): Destinations =
            Builder(routeID).apply(block).build()
    }
}
