class Color1 {
    r: u8[<=9] = 8,
    g: u8[1..=2] = 7;
    b: u8[1<b<2] = 3,
    a: f32 = 1.0;
}

table Color2 {
    color: Color = Red;
    color: Color = Red;
    ^require: [1, [2]]
    ^require: {a: 1}
    ^example: [a]
}

enum Color3 {
	Red,
	Blue,
}

union Color4 {
	Red = 1
}

object a = 1;