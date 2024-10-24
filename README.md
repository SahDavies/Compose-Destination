# Compose-Destination
Destination is a very simple route management model with a descriptive and friendly usage. Destination is a class that reliably creates string routes with the correct pattern easing the burden from the developer as well as mitigating errors. To learn more on how to navigate using this class and how to enforce type safety for arguments check here
### API
| Properties    | Type                     | Description                                                                                                             |
| :------------ | ------------------------ | :---------------------------------------------------------------------------------------------------------------------- |
| **route**     | `String`                 | This is the base string route for the destination. It's a unique identifier for each screen in the navigation graph.    |
| **namedArgs** | `List<NamedNavArgument>` | A list of `NamedNavArguments` for the route. These are used to pass optional parameter details to the navigation graph. |

| Return Type   | Function                                                             | Description                                                                                                                                                                                                                                                                    |
| ------------- | -------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `String`      | **`navRoute(block: Builder.() -> Unit = {})`**                       | Constructs route that will be passed to the `navController`. This function is used to build a navigation route string, appending actual argument values.                                                         |
| `Destination` | **`generateRoute(routeID: String, block: Builder.() -> Unit = {})`** | This is a static helper function used to instantiate a `Destination` object. It constructs the route property of the destination class with a unique route name - `routeID`. The builder pattern is applied here to allow modifications of the route (e.g., adding arguments). |

#### **Member Function**
---
**`navRoute(block: Builder.() -> Unit = {}): String`**
- **Return value:** A string representing the full route, including the actual arguments if provided (required or optional).
- **Parameters:**
	- `block: Builder.() -> Unit` — a lambda block with access to utility functions used to modify the route during construction.

##### **Companion Object Functions**

**`generateRoute(routeID: String, block: Builder.() -> Unit = {}) : Destination`** 
- **Return value:**  A `Destination` instance representing the specified route.
- **Parameters:**
	- `routeID: String` — the unique ID of the route.
	- `block: Builder.() -> Unit` — an optional lambda block with access to utility functions used to  customise the route during construction.

### Usage
```kotlin
val FORM_ROUTE = generateRoute("form") // takes no argument

// takes are required argument and two optional argument
val PROFILE_ROUTE = generateRoute("profile") {  
    withRequiredArgs("email")
      
    // takes a vararg of type NamedNavArgument
    withOptionalArgs( 
	    navArgument("phone_number") {  
	        type = NavType.LongType  
	        defaultValue = 123456789  
	    },
	    navArgument("url") {  
	        type = NavType.StringType  
	        nullable = true  
	    }
	) 
}

// Test
fun main() {
	println("NavHost receives this route:\n")
	println(destination.route)
	println()
	println("navController.navigate() is given this route:\n")
	println(destination.navRoute {
		requiredArgs("anon@email.com")
		optionalArgs("01478219", "http://www.mysociallink.com/profile")
	})
}
```

#### Output
```shell
NavHost receives this route:
signup/{email}?phone_number={phone_number}&url={url}

navController.navigate() is given this route:
signup/anon@email.com?phone_number={01478219}&url=http://www.mysociallink.com/profile
```
