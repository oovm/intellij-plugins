package yggdrasil.editing.highlight

import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.OptionsBundle
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.util.NlsContexts.AttributeDescriptor
import yggdrasil.definition.YggdrasilBundle
import java.util.function.Supplier
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default


enum class HighlightColor(humanName: Supplier<@AttributeDescriptor String>, default: TextAttributesKey? = null) {
    // 特殊关键词
    KEYWORD(OptionsBundle.messagePointer("options.language.defaults.keyword"), Default.KEYWORD),
    MODIFIER(YggdrasilBundle.messagePointer("valkyrie.highlight.modifier"), Default.KEYWORD),

    // 字面量
    NULL(YggdrasilBundle.messagePointer("color.token.null"), Default.KEYWORD),
    BOOLEAN(YggdrasilBundle.messagePointer("color.token.boolean"), Default.KEYWORD),
    BYTE(YggdrasilBundle.messagePointer("color.token.byte"), Default.NUMBER),
    INTEGER(YggdrasilBundle.messagePointer("color.token.integer"), Default.NUMBER),
    DECIMAL(YggdrasilBundle.messagePointer("color.token.decimal"), Default.NUMBER),
    STRING(YggdrasilBundle.messagePointer("color.token.string"), Default.STRING),
    TEXT(YggdrasilBundle.messagePointer("color.token.text"), STRING.textAttributesKey),
    STRING_ESCAPED(YggdrasilBundle.messagePointer("color.token.text"), Default.VALID_STRING_ESCAPE),
    STRING_BAD(YggdrasilBundle.messagePointer("color.token.text"), Default.INVALID_STRING_ESCAPE),

    // 标识符
    IDENTIFIER(OptionsBundle.messagePointer("options.language.defaults.identifier"), Default.IDENTIFIER),
    SYM_TYPE(YggdrasilBundle.messagePointer("color.token.symbol.trait"), Default.CLASS_REFERENCE),
    RULE_CLASS(YggdrasilBundle.messagePointer("color.token.symbol.class"), Default.CLASS_NAME),
    RULE_UNION(YggdrasilBundle.messagePointer("color.token.symbol.variant"), Default.INTERFACE_NAME),
    SYM_MACRO(YggdrasilBundle.messagePointer("color.token.symbol.macro"), Default.METADATA),
    SYM_LANGUAGE(YggdrasilBundle.messagePointer("color.token.symbol.macro"), Default.METADATA),
    SYM_LOCAL(YggdrasilBundle.messagePointer("color.token.symbol.local"), Default.LOCAL_VARIABLE),
    SYM_LOCAL_MUT(YggdrasilBundle.messagePointer("color.token.symbol.local.mutable"), Default.REASSIGNED_LOCAL_VARIABLE),
    SYM_GLOBAL(YggdrasilBundle.messagePointer("color.token.symbol.global"), Default.GLOBAL_VARIABLE),
    SYM_GLOBAL_MUT(YggdrasilBundle.messagePointer("color.token.symbol.global.mut"), Default.GLOBAL_VARIABLE),
    TAG_NODE(YggdrasilBundle.messagePointer("color.token.symbol.parameter"), Default.PARAMETER),
    TAG_BRANCH(YggdrasilBundle.messagePointer("color.token.symbol.parameter"), Default.PARAMETER),
    SYM_FIELD(YggdrasilBundle.messagePointer("color.token.symbol.field"), Default.INSTANCE_FIELD),
    SYM_CONSTANT(YggdrasilBundle.messagePointer("color.token.symbol.constant"), Default.CONSTANT),
    SYM_BUILTIN(YggdrasilBundle.messagePointer("color.token.symbol.built.in"), Default.CONSTANT),
    SYM_FUNCTION(YggdrasilBundle.messagePointer("color.token.symbol.function.self"), Default.INSTANCE_METHOD),
    SYM_ARGUMENT(YggdrasilBundle.messagePointer("color.token.symbol.type"), Default.PARAMETER),

    // 标点符号
    ASSIGN(YggdrasilBundle.messagePointer("color.token.set"), Default.OPERATION_SIGN),
    OPERATION(OptionsBundle.messagePointer("options.language.defaults.operation"), Default.OPERATION_SIGN),
    OP_NUMBER(YggdrasilBundle.messagePointer("color.token.number.suffix"), Default.METADATA),
    OP_STRING(YggdrasilBundle.messagePointer("color.token.string.prefix"), Default.KEYWORD),
    COMMENT_LINE(OptionsBundle.messagePointer("options.language.defaults.line.comment"), Default.LINE_COMMENT),
    COMMENT_BLOCK(OptionsBundle.messagePointer("options.language.defaults.block.comment"), Default.BLOCK_COMMENT),
    ;

    val textAttributesKey: TextAttributesKey = TextAttributesKey.createTextAttributesKey("voml.lang.$name", default)
    val attributesDescriptor: AttributesDescriptor = AttributesDescriptor(humanName, textAttributesKey)
    val testSeverity: HighlightSeverity = HighlightSeverity(name, HighlightSeverity.INFORMATION.myVal)
}
