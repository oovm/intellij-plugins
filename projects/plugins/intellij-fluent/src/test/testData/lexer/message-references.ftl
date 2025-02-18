# Message references

# Simple message
hello = Hello, world!

# Message with attributes
user = User
    .name = Name
    .email = Email
    .age = Age

# Reference to simple message
greeting = { hello }

# Reference to message attribute
user-name = { user.name }
user-email = { user.email }

# Nested attribute reference
user-full = { user.name } <{ user.email }>