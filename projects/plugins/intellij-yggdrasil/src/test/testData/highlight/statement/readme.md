```vk
using {
    ~ wow!
    `python`.torch as tf
}


let mut a, mut b:(int,int)

(a,b,c) = f(x)
a.b = c

@path("../lib/mod")
using {
    a as b
    c as d
    e.f.{g as h}
}
```

```scala
class IntegerSet {

}





class IndexersExample {
	_size: f64;
    name: String,
}

extends IndexersExample {
	set Size(value) {
		self._size = value as float
	}
	view index(i: int) {
		return self._size
	}
	set view index(i: int) {
		self._size = value as float
	}
	set view index(c: char, i: int) {
		self._size = value as float
	}
}
```

```scala
tagged UnionB {
     VariantA
     VariantB,
     VariantC;
     VariantD {
         fieldA,
         fieldB: Type,
     }
}

@hidden
enumerate UnionBTag {
    VariantA = 1,
    VariantB = 2,
    VariantC = 3,
    VariantD = 4,
}

@hidden
union UnionBData {
    variant_a: VariantA,
    variant_b: VariantB,
    variant_c: VariantC,
    variant_d: VariantD,
}

violate {
    x.variant_a
}
```

```vk
let x = call/cc {
    10 * 3
}
print(5 + x)

let x = call/cc {
    10 * exit 3
}
print(5 + x)

let x = call/cc {
    exit 10;
    3;
}
print(5 + x)


console.log(5 + callcc(exit => { exit(10); return 3 }))
console.log(5 + callcc(exit => { exit(10); throw Error("test failed") }))

try {
  console.log(5 + callcc(exit => { throw Error("test passed!") }))
}
catch (e) {
  console.error(e)
}

🚪
let z = y * 2;
console.log(z + 1);
```

```shell
ying = with_cc
print '@'
yang = with_cc
print '*'
ying yang
```

```vk
阴 = call/cc {
    曰 '0'
    阳 = call/cc {
        曰 '1'
        阴 阳
    }
}
阳 = call/cc {
    曰 '1'
    阴 阳
}
曰 '0'
曰 '1'
阴 阳
```

```vk
曰 '1'
阴 阳
```