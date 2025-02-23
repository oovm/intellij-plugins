package com.github.bytecodealliance.wit.language.psi

import com.intellij.psi.tree.IElementType

object WitKeywords {
    private val KEYWORDS: Map<String, IElementType> = mapOf(
        "as" to WitTypes.KW_AS,
        "async" to WitTypes.KW_ASYNC,
        "bool" to WitTypes.KW_BOOL,
        "borrow" to WitTypes.KW_BORROW,
        "char" to WitTypes.KW_CHAR,
        "constructor" to WitTypes.KW_CONSTRUCTOR,
        "enum" to WitTypes.KW_ENUM,
        "export" to WitTypes.KW_EXPORT,
        "extend" to WitTypes.KW_EXTEND,
        "f32" to WitTypes.KW_F32,
        "f64" to WitTypes.KW_F64,
        "flags" to WitTypes.KW_FLAGS,
        "from" to WitTypes.KW_FROM,
        "func" to WitTypes.KW_FUNC,
        "future" to WitTypes.KW_FUTURE,
        "import" to WitTypes.KW_IMPORT,
        "include" to WitTypes.KW_INCLUDE,
        "interface" to WitTypes.KW_INTERFACE,
        "list" to WitTypes.KW_LIST,
        "map" to WitTypes.KW_MAP,
        "option" to WitTypes.KW_OPTION,
        "own" to WitTypes.KW_OWN,
        "package" to WitTypes.KW_PACKAGE,
        "record" to WitTypes.KW_RECORD,
        "resource" to WitTypes.KW_RESOURCE,
        "result" to WitTypes.KW_RESULT,
        "s16" to WitTypes.KW_S16,
        "s32" to WitTypes.KW_S32,
        "s64" to WitTypes.KW_S64,
        "s8" to WitTypes.KW_S8,
        "static" to WitTypes.KW_STATIC,
        "stream" to WitTypes.KW_STREAM,
        "string" to WitTypes.KW_STRING,
        "tuple" to WitTypes.KW_TUPLE,
        "type" to WitTypes.KW_TYPE,
        "u16" to WitTypes.KW_U16,
        "u32" to WitTypes.KW_U32,
        "u64" to WitTypes.KW_U64,
        "u8" to WitTypes.KW_U8,
        "use" to WitTypes.KW_USE,
        "variant" to WitTypes.KW_VARIANT,
        "with" to WitTypes.KW_WITH,
        "world" to WitTypes.KW_WORLD,
    )

    fun lookup(text: String): IElementType? = KEYWORDS[text]
}
