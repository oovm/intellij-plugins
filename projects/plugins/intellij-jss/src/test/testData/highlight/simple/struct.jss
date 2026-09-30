class tuple dense Point {
    x: f32,
    y: f32,
    z: f32 = 1.0,
}

class Color {
    r: u8[<=9] = 8,
    // g: u8[1..=2] = 7;
    // b: u8[1<b<2] = 3,
    a: u8 = 1.0;
}

object a = 1;
#dense

class Colors {
    colors: List[u8, u8] = [1, 2, 3]
    ^a = a
}