# Function calls

# Function with no arguments
now = { NOW() }

# Function with positional arguments
format-date = { FORMAT_DATE($date, "yyyy-MM-dd") }

# Function with multiple positional arguments
format-number = { FORMAT_NUMBER($number, "en", "currency") }

# Function with named arguments
plural = { PLURAL($count, one: "one", other: "many") }

# Function with mixed arguments
mixed = { FUNC($arg1, $arg2, name: $arg3, value: "test") }