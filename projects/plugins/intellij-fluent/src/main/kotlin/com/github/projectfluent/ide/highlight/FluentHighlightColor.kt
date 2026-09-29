package com.github.projectfluent.ide.highlight

import com.github.projectfluent.FluentBundle
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.OptionsBundle
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.util.NlsContexts.AttributeDescriptor
import java.util.function.Supplier
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default

enum class FluentHighlightColor(humanName: Supplier<@AttributeDescriptor String>, default: TextAttributesKey? = null) {
    // 特殊关键词
    KEYWORD(OptionsBundle.messagePointer("options.language.defaults.keyword"), Default.KEYWORD),
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
    IDENTIFIER(OptionsBundle.messagePointer("options.language.defaults.identifier"), Default.IDENTIFIER),
    SYM_MESSAGE(FluentBundle.messagePointer("color.token.symbol.message"), Default.STATIC_FIELD),
    SYM_MESSAGE_REFERENCE(FluentBundle.messagePointer("color.token.symbol.message.reference"), Default.STATIC_FIELD),
    SYM_TERM(FluentBundle.messagePointer("color.token.symbol.term"), Default.CONSTANT),
    SYM_ATTRIBUTE(FluentBundle.messagePointer("color.token.symbol.attribute"), Default.STATIC_METHOD),
    SYM_ATTRIBUTE_REFERENCE(FluentBundle.messagePointer("color.token.symbol.attribute.reference"), Default.STATIC_METHOD),
    SYM_VARIABLE(FluentBundle.messagePointer("color.token.symbol.variable"), Default.REASSIGNED_PARAMETER),
    SYM_FUNCTION(FluentBundle.messagePointer("color.token.symbol.function"), Default.PREDEFINED_SYMBOL),

    //
    TYPE_HINT(FluentBundle.messagePointer("color.token.symbol.type"), Default.CLASS_NAME),

    // 标点符号
    PARENTHESES(OptionsBundle.messagePointer("options.language.defaults.parentheses"), Default.PARENTHESES),
    BRACKETS(OptionsBundle.messagePointer("options.language.defaults.brackets"), Default.BRACKETS),
    BRACES(OptionsBundle.messagePointer("options.language.defaults.braces"), Default.BRACES),
    DOT(OptionsBundle.messagePointer("options.language.defaults.dot"), Default.DOT),
    STAR(FluentBundle.messagePointer("color.token.default"), Default.KEYWORD),
    COMMA(OptionsBundle.messagePointer("options.language.defaults.comma"), Default.COMMA),
    SET(FluentBundle.messagePointer("color.token.set"), Default.OPERATION_SIGN),
    SEMICOLON(OptionsBundle.messagePointer("options.language.defaults.semicolon"), Default.SEMICOLON),

    // 注释
    LINE_COMMENT(OptionsBundle.messagePointer("options.language.defaults.line.comment"), Default.LINE_COMMENT),
    BLOCK_COMMENT(OptionsBundle.messagePointer("options.language.defaults.block.comment"), Default.BLOCK_COMMENT),
    DOC_COMMENT(OptionsBundle.messagePointer("options.language.defaults.doc.markup"), Default.DOC_COMMENT),

    // 错误
    BAD_CHARACTER(
        OptionsBundle.messagePointer("options.java.attribute.descriptor.bad.character"),
        HighlighterColors.BAD_CHARACTER
    ),

    // 废弃
    EXTENSION(OptionsBundle.messagePointer("options.language.defaults.metadata"), Default.METADATA),
    ;

    val textAttributesKey: TextAttributesKey = TextAttributesKey.createTextAttributesKey("fluent.$name", default)
    val attributesDescriptor: AttributesDescriptor = AttributesDescriptor(humanName, textAttributesKey)
    val testSeverity: HighlightSeverity = HighlightSeverity(name, HighlightSeverity.INFORMATION.myVal)
}
