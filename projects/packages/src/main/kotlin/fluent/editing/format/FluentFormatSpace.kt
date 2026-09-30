package fluent.editing.format

import fluent.definition.FluentLanguage
import fluent.surface.psi.FluentTypes
import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.tree.TokenSet

data class FluentFormatSpace(
    val commonSettings: CommonCodeStyleSettings,
    val spacingBuilder: SpacingBuilder
) {
    companion object {
        fun create(settings: CodeStyleSettings): FluentFormatSpace {
            val commonSettings = settings.getCommonSettings(FluentLanguage)
            return FluentFormatSpace(commonSettings, createSpacingBuilder(commonSettings))
        }

        private val remove_space_before = TokenSet.create(
            FluentTypes.PARENTHESIS_R,
            FluentTypes.BRACKET_R,
            FluentTypes.BRACE_R,
            FluentTypes.COMMA,
            FluentTypes.SEMICOLON
        )
        private val remove_space_after = TokenSet.create(
            FluentTypes.PARENTHESIS_L,
            FluentTypes.BRACKET_L,
            FluentTypes.BRACE_L,
            FluentTypes.COLON,
        )
        private val remove_space_newline_after = TokenSet.create(
            FluentTypes.BRACKET_L,
            FluentTypes.DOT,
            FluentTypes.DOLLAR,
            FluentTypes.STAR,
            FluentTypes.HYPHEN
        )
        private val remove_space_newline_before = TokenSet.create(
            FluentTypes.BRACKET_R
        )
        private val newline_indent_after = TokenSet.create(FluentTypes.TO)

        private val separators = TokenSet.create(FluentTypes.COMMA, FluentTypes.SEMICOLON)

        private fun createSpacingBuilder(commonSettings: CommonCodeStyleSettings): SpacingBuilder {
            val custom = SpacingBuilder(commonSettings)
                // ,
                .after(separators).spacing(1, 1, 0, commonSettings.KEEP_LINE_BREAKS, 0)
                // k: v
                .after(FluentTypes.COLON).spacing(1, 1, 0, false, 0)
                // k = v
                .around(FluentTypes.EQ).spacing(1, 1, 0, commonSettings.KEEP_LINE_BREAKS, 0)
                // `$var ->` selector spacing (#5)
                .between(FluentTypes.VARIABLE_REFERENCE, FluentTypes.HYPHEN).spacing(1, 1, 0, false, 0)
                .between(FluentTypes.FUNCTION_REFERENCE, FluentTypes.HYPHEN).spacing(1, 1, 0, false, 0)
                // Select arms: one newline + indent from FormatBlock; ignore source line breaks (#5)
                .between(FluentTypes.ANGLE_R, FluentTypes.VARIANT).spacing(0, 0, 1, false, 1)
                .between(FluentTypes.ANGLE_R, FluentTypes.DEFAULT_VARIANT).spacing(0, 0, 1, false, 1)
                .between(FluentTypes.VARIANT, FluentTypes.VARIANT).spacing(0, 0, 1, false, 1)
                .between(FluentTypes.VARIANT, FluentTypes.DEFAULT_VARIANT).spacing(0, 0, 1, false, 1)
                .between(FluentTypes.DEFAULT_VARIANT, FluentTypes.VARIANT).spacing(0, 0, 1, false, 1)

            return custom
                .before(remove_space_before).spaceIf(false)
                .after(remove_space_after).spaceIf(false)
                .before(remove_space_newline_before).spacing(0, 0, 0, false, 0)
                .after(remove_space_newline_after).spacing(0, 0, 0, false, 0)
                .after(newline_indent_after).spacing(0, 0, 0, true, 1)
        }
    }
}