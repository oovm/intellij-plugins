# Select expressions

# Simple select expression
count = { $count ->
    [one] One item
    [other] { $count } items
}

# Select with default variant
gender = { $gender ->
    [male] He
    [female] She
    *[other] They
}

# Select with number variants
age = { $age ->
    [0] Zero
    [1] One
    [2] Two
    [other] Many
}

# Select with string variants
status = { $status ->
    [active] Active
    [inactive] Inactive
    [pending] Pending
    *[other] Unknown
}