package jss.editing.highlight

import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.util.NlsContexts
import java.util.function.Supplier
import jss.surface.file.MessageBundle
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default

enum class JssColor(humanName: Supplier<@NlsContexts.AttributeDescriptor String>, default: TextAttributesKey? = null) {
    KEYWORD(MessageBundle.messagePointer("color.defaults.keyword"), Default.KEYWORD),
    IDIOM_SYMBOL(MessageBundle.messagePointer("color.token.symbol.idiom"), Default.METADATA),
    IDIOM_MARK(MessageBundle.messagePointer("color.token.idiom_mark"), IDIOM_SYMBOL.textAttributesKey),
    PROP_MARK(MessageBundle.messagePointer("color.token.properties_mark"), KEYWORD.textAttributesKey),

    NULL(MessageBundle.messagePointer("color.token.null"), Default.KEYWORD),
    BOOLEAN(MessageBundle.messagePointer("color.token.boolean"), Default.KEYWORD),
    DECIMAL(MessageBundle.messagePointer("color.token.decimal"), Default.NUMBER),
    INTEGER(MessageBundle.messagePointer("color.token.integer"), Default.NUMBER),
    STRING(MessageBundle.messagePointer("color.token.string"), Default.STRING),
    URL(MessageBundle.messagePointer("color.token.url"), STRING.textAttributesKey),

    IDENTIFIER(MessageBundle.messagePointer("color.defaults.identifier"), Default.IDENTIFIER),
    MODIFIER(MessageBundle.messagePointer("color.token.symbol.annotation"), Default.METADATA),
    SYM_ANNO(MessageBundle.messagePointer("color.token.symbol.annotation"), Default.STATIC_METHOD),
    SYM_FIELD(MessageBundle.messagePointer("color.token.symbol.property"), Default.STATIC_FIELD),
    SYM_SCHEMA(MessageBundle.messagePointer("color.token.symbol.schema"), Default.PREDEFINED_SYMBOL),
    SYM_CLASS(MessageBundle.messagePointer("color.token.class"), Default.CLASS_NAME),
    TYPE_HINT(MessageBundle.messagePointer("color.token.symbol.type"), Default.CLASS_NAME),

    OPERATOR(MessageBundle.messagePointer("color.token.operation"), Default.OPERATION_SIGN),
    PARENTHESES(MessageBundle.messagePointer("color.defaults.parentheses"), Default.PARENTHESES),
    BRACKETS(MessageBundle.messagePointer("color.defaults.brackets"), Default.BRACKETS),
    BRACES(MessageBundle.messagePointer("color.defaults.braces"), Default.BRACES),
    DOT(MessageBundle.messagePointer("color.defaults.dot"), Default.DOT),
    COMMA(MessageBundle.messagePointer("color.defaults.comma"), Default.COMMA),
    SET(MessageBundle.messagePointer("color.token.set"), Default.OPERATION_SIGN),
    SEMICOLON(MessageBundle.messagePointer("color.defaults.semicolon"), Default.SEMICOLON),

    LINE_COMMENT(MessageBundle.messagePointer("color.defaults.line.comment"), Default.LINE_COMMENT),
    BLOCK_COMMENT(MessageBundle.messagePointer("color.defaults.block.comment"), Default.BLOCK_COMMENT),
    DOC_COMMENT(MessageBundle.messagePointer("color.defaults.doc.markup"), Default.DOC_COMMENT),

    BAD_CHARACTER(MessageBundle.messagePointer("color.defaults.bad.character"), HighlighterColors.BAD_CHARACTER),

    EXTENSION(MessageBundle.messagePointer("color.defaults.metadata"), Default.METADATA),
    ;

    val textAttributesKey: TextAttributesKey = TextAttributesKey.createTextAttributesKey("voml.lang.$name", default)
    val attributesDescriptor: AttributesDescriptor = AttributesDescriptor(humanName, textAttributesKey)
    val testSeverity: HighlightSeverity = HighlightSeverity(name, HighlightSeverity.INFORMATION.myVal)
}
