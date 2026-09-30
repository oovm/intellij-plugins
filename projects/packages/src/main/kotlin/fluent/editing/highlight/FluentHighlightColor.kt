package fluent.editing.highlight

import fluent.definition.FluentBundle
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.util.NlsContexts.AttributeDescriptor
import java.util.function.Supplier
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default

enum class FluentHighlightColor(humanName: Supplier<@AttributeDescriptor String>, default: TextAttributesKey? = null) {
    // 特殊关键词
    KEYWORD(FluentBundle.messagePointer("color.defaults.keyword"), Default.KEYWORD),
    IDIOM_SYMBOL(FluentBundle.messagePointer("color.token.symbol.idiom"), Default.METADATA),
    IDIOM_MARK(FluentBundle.messagePointer("color.token.idiom_mark"), IDIOM_SYMBOL.textAttributesKey),
    PROP_MARK(FluentBundle.messagePointer("color.token.properties_mark"), KEYWORD.textAttributesKey),

    // 字面量
    NULL(FluentBundle.messagePointer("color.token.null"), Default.KEYWORD),
    BOOLEAN(FluentBundle.messagePointer("color.token.boolean"), Default.KEYWORD),
    DECIMAL(FluentBundle.messagePointer("color.token.decimal"), Default.NUMBER),
    INTEGER(FluentBundle.messagePointer("color.token.integer"), Default.NUMBER),
    STRING(FluentBundle.messagePointer("color.token.string"), Default.STRING),
    TEXT(FluentBundle.messagePointer("color.token.text"), STRING.textAttributesKey),
    STRING_ESCAPED(FluentBundle.messagePointer("color.token.text"), Default.VALID_STRING_ESCAPE),
    STRING_BAD(FluentBundle.messagePointer("color.token.text"), Default.INVALID_STRING_ESCAPE),

    // 标识符
    IDENTIFIER(FluentBundle.messagePointer("color.defaults.identifier"), Default.IDENTIFIER),
    SYM_MESSAGE(FluentBundle.messagePointer("color.token.symbol.message"), Default.STATIC_FIELD),
    SYM_MESSAGE_REFERENCE(FluentBundle.messagePointer("color.token.symbol.message.reference"), Default.STATIC_FIELD),
    SYM_TERM(FluentBundle.messagePointer("color.token.symbol.term"), Default.CONSTANT),
    SYM_ATTRIBUTE(FluentBundle.messagePointer("color.token.symbol.attribute"), Default.STATIC_METHOD),
    SYM_ATTRIBUTE_REFERENCE(FluentBundle.messagePointer("color.token.symbol.attribute.reference"), Default.STATIC_METHOD),
    SYM_VARIABLE(FluentBundle.messagePointer("color.token.symbol.variable"), Default.REASSIGNED_PARAMETER),
    SYM_FUNCTION(FluentBundle.messagePointer("color.token.symbol.function"), Default.PREDEFINED_SYMBOL),

    TYPE_HINT(FluentBundle.messagePointer("color.token.symbol.type"), Default.CLASS_NAME),

    // 标点符号
    PARENTHESES(FluentBundle.messagePointer("color.defaults.parentheses"), Default.PARENTHESES),
    BRACKETS(FluentBundle.messagePointer("color.defaults.brackets"), Default.BRACKETS),
    BRACES(FluentBundle.messagePointer("color.defaults.braces"), Default.BRACES),
    DOT(FluentBundle.messagePointer("color.defaults.dot"), Default.DOT),
    STAR(FluentBundle.messagePointer("color.token.default"), Default.KEYWORD),
    COMMA(FluentBundle.messagePointer("color.defaults.comma"), Default.COMMA),
    SET(FluentBundle.messagePointer("color.token.set"), Default.OPERATION_SIGN),
    SEMICOLON(FluentBundle.messagePointer("color.defaults.semicolon"), Default.SEMICOLON),

    // 注释
    LINE_COMMENT(FluentBundle.messagePointer("color.defaults.line.comment"), Default.LINE_COMMENT),
    BLOCK_COMMENT(FluentBundle.messagePointer("color.defaults.block.comment"), Default.BLOCK_COMMENT),
    DOC_COMMENT(FluentBundle.messagePointer("color.defaults.doc.markup"), Default.DOC_COMMENT),

    // 错误
    BAD_CHARACTER(FluentBundle.messagePointer("color.defaults.bad.character"), HighlighterColors.BAD_CHARACTER),

    // 废弃
    EXTENSION(FluentBundle.messagePointer("color.defaults.metadata"), Default.METADATA),
    ;

    val textAttributesKey: TextAttributesKey = TextAttributesKey.createTextAttributesKey("fluent.$name", default)
    val attributesDescriptor: AttributesDescriptor = AttributesDescriptor(humanName, textAttributesKey)
    val testSeverity: HighlightSeverity = HighlightSeverity(name, HighlightSeverity.INFORMATION.myVal)
}
