package jss.editing.format.codestyle

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.psi.codeStyle.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import jss.surface.file.JssLanguage

class CodeStyleSettingProvider : LanguageCodeStyleSettingsProvider() {
    override fun getLanguage() = JssLanguage

    override fun getIndentOptionsEditor() = SmartIndentOptionsEditor()

    override fun createConfigurable(
        settings: CodeStyleSettings,
        modelSettings: CodeStyleSettings
    ): CodeStyleConfigurable {
        return object : CodeStyleAbstractConfigurable(
            settings,
            modelSettings,
            configurableDisplayName
        ) {
            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel {
                return CodeStyleMainPanel(currentSettings, settings)
            }
        }
    }

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        when (settingsType) {
            SettingsType.COMMENTER_SETTINGS -> {
                consumer.showStandardOptions(
                    CodeStyleSettingsCustomizable.CommenterOption.BLOCK_COMMENT_AT_FIRST_COLUMN.name,
                    CodeStyleSettingsCustomizable.CommenterOption.LINE_COMMENT_ADD_SPACE.name,
                    CodeStyleSettingsCustomizable.CommenterOption.LINE_COMMENT_AT_FIRST_COLUMN.name
                )
            }

            SettingsType.WRAPPING_AND_BRACES_SETTINGS -> {
                consumer.showStandardOptions(
                    CodeStyleSettingsCustomizable.WrappingOrBraceOption.RIGHT_MARGIN.name,
                    CodeStyleSettingsCustomizable.WrappingOrBraceOption.KEEP_LINE_BREAKS.name
                )
            }

            SettingsType.LANGUAGE_SPECIFIC -> {
                consumer.showStandardOptions()
            }

            else -> {}
        }
    }

    override fun customizeDefaults(
        commonSettings: CommonCodeStyleSettings,
        indentOptions: CommonCodeStyleSettings.IndentOptions
    ) {
        commonSettings.RIGHT_MARGIN = DEFAULT_RIGHT_MARGIN

        commonSettings.LINE_COMMENT_AT_FIRST_COLUMN = false
        commonSettings.LINE_COMMENT_ADD_SPACE = true
        commonSettings.BLOCK_COMMENT_AT_FIRST_COLUMN = false

        indentOptions.CONTINUATION_INDENT_SIZE = indentOptions.INDENT_SIZE
    }

    override fun getCodeSample(settingsType: SettingsType) = javaClass.getResource("/fileTemplates/demoCodeStyle.jss")!!.readText()

    companion object {
        const val DEFAULT_RIGHT_MARGIN = 100
    }
}
