namespace std.protobuf;
/// `i64` with [Variable Length Quantity](https://en.wikipedia.org/wiki/Variable-length_quantity)
primitive i64v: i64 {
    ^encoding: 1
    ^byte_width: [1, 5]
}

/// `i64` with [Variable Length Quantity](https://en.wikipedia.org/wiki/Variable-length_quantity)
primitive i64z: i64 {
    ^encoding: 1
    ^byte_width: [1, 5]
    ^encoder: {
        rust: std.enc
    }
    ^decoder: {
        rust: std.dec
    }
}