# References
hello   = Hello, world!
welcome = Welcome, { $user }!
time-elapsed = Time elapsed: { NUMBER($duration, maximumFractionDigits: 0) }s.
-brand-name = Firefox
    .placeholder = email@example.com
    .aria-label = Login input value
    .title = Type your login email
installing = Installing { -brand-name }.

# Selectors
your-score = You scored {
    NUMBER($score, minimumFractionDigits: 1) ->
        [0.0]   zero points. What happened?
       *[other] { NUMBER($score, minimumFractionDigits: 1)} points.
    }

# Attributes
login-input = Predefined value
    .placeholder = email@example.com
    .aria-label = Login input value
    .title = Type your login email

# Quoted TextQuoted Text
opening-brace = This message features an opening curly brace: {"{"}.
closing-brace = This message features a closing curly brace: {"}"}.
blank-is-removed =     This message starts with no blanks.
blank-is-preserved = {"    "}This message starts with 4 spaces.
leading-bracket =
    This message has an opening square bracket
    at the beginning of the third line:
    {"["}.
attribute-how-to =
    To add an attribute to this messages, write
    {".attr = Value"} on a new line.
    .attr = An actual attribute (not part of the text value above)

# Escape Sequences
literal-quote1 = Text in {"\""}double quotes{"\""}.
literal-quote2 = Text in "double quotes".
privacy-label = Privacy{"\u00A0"}Policy
which-dash1 = It's a dash—or is it?
which-dash2 = It's a dash{"\u2014"}or is it?
tears-of-joy1 = {"\U01F602"}
tears-of-joy2 = 😂

# Multiline Text
multi = Text can also span multiple lines
    as long as each new line is indented
    by at least one space.

block =
    Sometimes it's more readable to format
    multiline text as a "block", which means
    starting it on a new line. All lines must
    be indented by at least one space.
blank-lines =

    The blank line above this line is ignored.
    This is a second line of the value.

    The blank line above this line is preserved.