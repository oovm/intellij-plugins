package vos.editing.format

import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.tree.TokenSet
import vos.surface.file.VosLanguage
import vos.surface.psi.VosTypes

data class FormatContext(val commonSettings: CommonCodeStyleSettings, val spacingBuilder: SpacingBuilder) {
    companion object {
        fun create(settings: CodeStyleSettings): FormatContext {
            val commonSettings = settings.getCommonSettings(VosLanguage)
            return FormatContext(commonSettings, createSpacingBuilder(commonSettings))
        }

        private val remove_space_before = TokenSet.create(
            VosTypes.PARENTHESIS_R,
            VosTypes.BRACKET_R,
            VosTypes.COMMA,
            VosTypes.SEMICOLON
        )
        private val remove_space_after = TokenSet.create(
            VosTypes.PARENTHESIS_L,
            VosTypes.BRACKET_L,
            VosTypes.COLON,
        )
        private val remove_space_newline_after = TokenSet.create(VosTypes.DOT)

        private val separators = TokenSet.create(VosTypes.COMMA, VosTypes.SEMICOLON)

        private fun createSpacingBuilder(commonSettings: CommonCodeStyleSettings): SpacingBuilder {
            val custom = SpacingBuilder(commonSettings)
                // ,
                .after(separators).spacing(1, 1, 0, commonSettings.KEEP_LINE_BREAKS, 0)
                // k: v
                .after(VosTypes.COLON).spacing(1, 1, 0, false, 0)
                // k = v
                .around(VosTypes.EQ).spacing(1, 1, 0, commonSettings.KEEP_LINE_BREAKS, 0)
                // SpacingBuilder { }
                .before(VosTypes.BRACE_BLOCK).spacing(1, 1, 0, commonSettings.KEEP_LINE_BREAKS, 0)

            return custom
                .before(remove_space_before).spaceIf(false)
                .after(remove_space_after).spaceIf(false)
                .after(remove_space_newline_after).spacing(0, 0, 0, false, 0)
        }
    }
}