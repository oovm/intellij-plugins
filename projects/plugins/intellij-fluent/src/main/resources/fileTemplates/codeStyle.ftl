# References
<SYM_MESSAGE>hello</SYM_MESSAGE>   = Hello, world!
<SYM_MESSAGE>welcome</SYM_MESSAGE> = Welcome, { <SYM_VARIABLE>$user</SYM_VARIABLE> }!
<SYM_MESSAGE>time-elapsed</SYM_MESSAGE> = Time elapsed: { <SYM_FUNCTION>NUMBER</SYM_FUNCTION>(<SYM_VARIABLE>$duration</SYM_VARIABLE>, <SYM_VARIABLE>maximumFractionDigits</SYM_VARIABLE>: 0) }s.
<SYM_TERM>-brand-name</SYM_TERM> = Firefox
<SYM_MESSAGE>installing</SYM_MESSAGE> = Installing { <SYM_TERM>-brand-name</SYM_TERM> }.

# Selectors
<SYM_MESSAGE>your-score</SYM_MESSAGE> = You scored {
    <SYM_FUNCTION>NUMBER</SYM_FUNCTION>(<SYM_VARIABLE>$score</SYM_VARIABLE>, <SYM_VARIABLE>minimumFractionDigits</SYM_VARIABLE>: 1) ->
        [0.0]   zero points. What happened?
       *[<SYM_TERM>other</SYM_TERM>] { <SYM_FUNCTION>NUMBER</SYM_FUNCTION>(<SYM_VARIABLE>$score</SYM_VARIABLE>, <SYM_VARIABLE>minimumFractionDigits</SYM_VARIABLE>: 1)} points.
    }

# Attributes
<SYM_MESSAGE>login-input</SYM_MESSAGE> = Predefined value
    <SYM_ATTRIBUTE>.placeholder</SYM_ATTRIBUTE> = email@example.com
    <SYM_ATTRIBUTE>.aria-label</SYM_ATTRIBUTE> = Login input value
    <SYM_ATTRIBUTE>.title</SYM_ATTRIBUTE> = Type your login email

# Escape Sequences
<SYM_MESSAGE>literal-quote1</SYM_MESSAGE> = Text in {"\""}double quotes{"\""}.
<SYM_MESSAGE>literal-quote2</SYM_MESSAGE> = Text in "double quotes".
<SYM_MESSAGE>privacy-label</SYM_MESSAGE> = Privacy{"\u00A0"}Policy
<SYM_MESSAGE>which-dash1</SYM_MESSAGE> = It's a dash—or is it?
<SYM_MESSAGE>which-dash2</SYM_MESSAGE> = It's a dash{"\u2014"}or is it?
<SYM_MESSAGE>tears-of-joy1</SYM_MESSAGE> = {"\U01F602"}
<SYM_MESSAGE>tears-of-joy2</SYM_MESSAGE> = 😂