#~~ 泛型语法


class A<G=()>() {}

```js
A::<int>(0)
A(0, G: int)
A⦇int⦈(0)
A⦅int⦆(0)
A⦓int⦔(0)
A⟅tuple⟆(30)
```

array -> Vec
ArrayView -> []
List -> VecDeque

String -> String
StringView -> str

Avoiding The Fencepost Problem

```
a[1, 1:-1]
```

```scala
let a;

vow T 
{
    @inline
    function
    return String
}
fun function(
    arg1: T,
    mut arg2: T,
    **arg3: T,
):  R
{

    return "a"
}
if a 
{
}
else
{
}
```
