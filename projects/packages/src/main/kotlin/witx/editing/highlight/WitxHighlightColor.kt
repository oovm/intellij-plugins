package witx.editing.highlight

import witx.definition.WitxBundle
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.util.NlsContexts.AttributeDescriptor
import java.util.function.Supplier
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default

enum class WitxHighlightColor(humanName: Supplier<@AttributeDescriptor String>, default: TextAttributesKey? = null) {
    KEYWORD(WitxBundle.messagePointer("color.defaults.keyword"), Default.KEYWORD),
    TYPE(WitxBundle.messagePointer("color.token.type"), Default.CLASS_NAME),
    IDENTIFIER(WitxBundle.messagePointer("color.defaults.identifier"), Default.IDENTIFIER),
    STRING(WitxBundle.messagePointer("color.token.string"), Default.STRING),
    NUMBER(WitxBundle.messagePointer("color.token.number"), Default.NUMBER),
    PARENTHESES(WitxBundle.messagePointer("color.defaults.parentheses"), Default.PARENTHESES),
    BRACES(WitxBundle.messagePointer("color.defaults.braces"), Default.BRACES),
    BRACKETS(WitxBundle.messagePointer("color.defaults.brackets"), Default.BRACKETS),
    OPERATOR(WitxBundle.messagePointer("color.token.operator"), Default.OPERATION_SIGN),
    LINE_COMMENT(WitxBundle.messagePointer("color.defaults.line.comment"), Default.LINE_COMMENT),
    BLOCK_COMMENT(WitxBundle.messagePointer("color.defaults.block.comment"), Default.BLOCK_COMMENT),
    BAD_CHARACTER(WitxBundle.messagePointer("color.defaults.bad.character"), HighlighterColors.BAD_CHARACTER),
    ;

    val textAttributesKey: TextAttributesKey = TextAttributesKey.createTextAttributesKey("wit.$name", default)
    val attributesDescriptor: AttributesDescriptor = AttributesDescriptor(humanName, textAttributesKey)
    val testSeverity: HighlightSeverity = HighlightSeverity(name, HighlightSeverity.INFORMATION.myVal)
}
