# Unformatted Fluent file
hello=Hello, world!
hello-user=Hello,{$userName}!
welcome=
.title=Welcome
.message=Welcome to our website,{$userName}!
-brand-name=
.short=MyApp
.long=My Application
shared-photos=
{$userName}{$photoCount->
[1]added one photo
*[other]added{$photoCount}new photos
}to{$userGender->
[male]his stream
[female]her stream
*[other]their stream
}.
