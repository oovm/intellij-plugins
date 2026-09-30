package witx.surface.psi

import com.intellij.psi.tree.IElementType

object WitxKeywords {
    private val KEYWORDS: Map<String, IElementType> = mapOf(
        "as" to WitxTypes.KW_AS,
        "async" to WitxTypes.KW_ASYNC,
        "bool" to WitxTypes.KW_BOOL,
        "borrow" to WitxTypes.KW_BORROW,
        "char" to WitxTypes.KW_CHAR,
        "constructor" to WitxTypes.KW_CONSTRUCTOR,
        "enum" to WitxTypes.KW_ENUM,
        "export" to WitxTypes.KW_EXPORT,
        "extend" to WitxTypes.KW_EXTEND,
        "f32" to WitxTypes.KW_F32,
        "f64" to WitxTypes.KW_F64,
        "flags" to WitxTypes.KW_FLAGS,
        "from" to WitxTypes.KW_FROM,
        "func" to WitxTypes.KW_FUNC,
        "future" to WitxTypes.KW_FUTURE,
        "import" to WitxTypes.KW_IMPORT,
        "include" to WitxTypes.KW_INCLUDE,
        "interface" to WitxTypes.KW_INTERFACE,
        "list" to WitxTypes.KW_LIST,
        "map" to WitxTypes.KW_MAP,
        "option" to WitxTypes.KW_OPTION,
        "own" to WitxTypes.KW_OWN,
        "package" to WitxTypes.KW_PACKAGE,
        "record" to WitxTypes.KW_RECORD,
        "resource" to WitxTypes.KW_RESOURCE,
        "result" to WitxTypes.KW_RESULT,
        "s16" to WitxTypes.KW_S16,
        "s32" to WitxTypes.KW_S32,
        "s64" to WitxTypes.KW_S64,
        "s8" to WitxTypes.KW_S8,
        "static" to WitxTypes.KW_STATIC,
        "stream" to WitxTypes.KW_STREAM,
        "string" to WitxTypes.KW_STRING,
        "tuple" to WitxTypes.KW_TUPLE,
        "type" to WitxTypes.KW_TYPE,
        "u16" to WitxTypes.KW_U16,
        "u32" to WitxTypes.KW_U32,
        "u64" to WitxTypes.KW_U64,
        "u8" to WitxTypes.KW_U8,
        "use" to WitxTypes.KW_USE,
        "variant" to WitxTypes.KW_VARIANT,
        "with" to WitxTypes.KW_WITH,
        "world" to WitxTypes.KW_WORLD,
    )

    fun lookup(text: String): IElementType? = KEYWORDS[text]
}
