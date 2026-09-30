package wit.editing.highlight

import wit.definition.WitBundle
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.util.NlsContexts.AttributeDescriptor
import java.util.function.Supplier
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default

enum class WitHighlightColor(humanName: Supplier<@AttributeDescriptor String>, default: TextAttributesKey? = null) {
    KEYWORD(WitBundle.messagePointer("color.defaults.keyword"), Default.KEYWORD),
    TYPE(WitBundle.messagePointer("color.token.type"), Default.CLASS_NAME),
    IDENTIFIER(WitBundle.messagePointer("color.defaults.identifier"), Default.IDENTIFIER),
    STRING(WitBundle.messagePointer("color.token.string"), Default.STRING),
    NUMBER(WitBundle.messagePointer("color.token.number"), Default.NUMBER),
    PARENTHESES(WitBundle.messagePointer("color.defaults.parentheses"), Default.PARENTHESES),
    BRACES(WitBundle.messagePointer("color.defaults.braces"), Default.BRACES),
    BRACKETS(WitBundle.messagePointer("color.defaults.brackets"), Default.BRACKETS),
    OPERATOR(WitBundle.messagePointer("color.token.operator"), Default.OPERATION_SIGN),
    LINE_COMMENT(WitBundle.messagePointer("color.defaults.line.comment"), Default.LINE_COMMENT),
    BLOCK_COMMENT(WitBundle.messagePointer("color.defaults.block.comment"), Default.BLOCK_COMMENT),
    BAD_CHARACTER(WitBundle.messagePointer("color.defaults.bad.character"), HighlighterColors.BAD_CHARACTER),
    ;

    val textAttributesKey: TextAttributesKey = TextAttributesKey.createTextAttributesKey("wit.$name", default)
    val attributesDescriptor: AttributesDescriptor = AttributesDescriptor(humanName, textAttributesKey)
    val testSeverity: HighlightSeverity = HighlightSeverity(name, HighlightSeverity.INFORMATION.myVal)
}
