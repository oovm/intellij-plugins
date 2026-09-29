# Inline placeables

# Simple variable reference
hello = Hello, { $name }!

# Message reference in placeable
greeting = { hello }

# Function call in placeable
today = Today is { NOW() }

# Nested placeables
nested = { $count ->
    [one] One { $item }
    [other] { $count } { $item }s
}

# Complex nested placeables
complex = { $user.name } has { PLURAL($count, one: "one", other: "{ $count }") } message{s}
