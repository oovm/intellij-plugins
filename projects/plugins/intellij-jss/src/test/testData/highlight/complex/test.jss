/// A product from Acme's catalog
schema Product: object {
    $schema: https://json-schema.org/draft/2020-12/schema
    $id: https://example.com/product.schema.json
    "required": ["tags", "dimensions"]
}

/// Tags for the product
property tags: array {
    minItems: 1,
    uniqueItems: true
    "items": {
        "type": "string"
    }
}

/// Dimensions for the product
property dimensions: object {
    .length: number
    .width: number
    .height: number

    required: ["length", "width", "height"]
}
