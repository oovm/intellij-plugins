package vos.editing.highlight

import vos.surface.file.MessageBundle
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.OptionsBundle
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.util.NlsContexts
import java.util.function.Supplier
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default


// TODO: 分类
enum class VosColor(humanName: Supplier<@NlsContexts.AttributeDescriptor String>, default: TextAttributesKey? = null) {
    // 特殊关键词
    KEYWORD(OptionsBundle.messagePointer("options.language.defaults.keyword"), Default.KEYWORD),
    IDIOM_SYMBOL(MessageBundle.messagePointer("color.token.symbol.idiom"), Default.METADATA),
    IDIOM_MARK(MessageBundle.messagePointer("color.token.idiom_mark"), IDIOM_SYMBOL.textAttributesKey),
    PROP_MARK(MessageBundle.messagePointer("color.token.properties_mark"), KEYWORD.textAttributesKey),

    // 字面量
    NULL(MessageBundle.messagePointer("color.token.null"), Default.KEYWORD),
    BOOLEAN(MessageBundle.messagePointer("color.token.boolean"), Default.KEYWORD),
    DECIMAL(MessageBundle.messagePointer("color.token.decimal"), Default.NUMBER),
    INTEGER(MessageBundle.messagePointer("color.token.integer"), Default.NUMBER),
    STRING(MessageBundle.messagePointer("color.token.string"), Default.STRING),
    URL(MessageBundle.messagePointer("color.token.url"), STRING.textAttributesKey),

    // 标识符
    IDENTIFIER(OptionsBundle.messagePointer("options.language.defaults.identifier"), Default.IDENTIFIER),
    MODIFIER(MessageBundle.messagePointer("color.token.symbol.annotation"), Default.METADATA),
    SYM_ANNO(MessageBundle.messagePointer("color.token.symbol.annotation"), Default.STATIC_METHOD),
    SYM_FIELD(MessageBundle.messagePointer("color.token.symbol.property"), Default.STATIC_FIELD),
    SYM_SCHEMA(MessageBundle.messagePointer("color.token.symbol.schema"), Default.PREDEFINED_SYMBOL),
    SYM_CLASS(MessageBundle.messagePointer("color.token.class"), Default.CLASS_NAME),
    //
    TYPE_HINT(MessageBundle.messagePointer("color.token.symbol.type"), Default.CLASS_NAME),

    // 标点符号
    OPERATOR(MessageBundle.messagePointer("color.token.operation"), Default.OPERATION_SIGN),
    PARENTHESES(OptionsBundle.messagePointer("options.language.defaults.parentheses"), Default.PARENTHESES),
    BRACKETS(OptionsBundle.messagePointer("options.language.defaults.brackets"), Default.BRACKETS),
    BRACES(OptionsBundle.messagePointer("options.language.defaults.braces"), Default.BRACES),
    DOT(OptionsBundle.messagePointer("options.language.defaults.dot"), Default.DOT),
    COMMA(OptionsBundle.messagePointer("options.language.defaults.comma"), Default.COMMA),
    SET(MessageBundle.messagePointer("color.token.set"), Default.OPERATION_SIGN),
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

    val textAttributesKey: TextAttributesKey = TextAttributesKey.createTextAttributesKey("voml.lang.$name", default)
    val attributesDescriptor: AttributesDescriptor = AttributesDescriptor(humanName, textAttributesKey)
    val testSeverity: HighlightSeverity = HighlightSeverity(name, HighlightSeverity.INFORMATION.myVal)
}
