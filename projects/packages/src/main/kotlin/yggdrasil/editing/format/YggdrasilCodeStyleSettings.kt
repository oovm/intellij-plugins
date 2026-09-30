package yggdrasil.editing.format.codestyle

import com.intellij.lang.Language
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import yggdrasil.definition.YggdrasilLanguage

class YggdrasilCodeStyleSettings : LanguageCodeStyleSettingsProvider() {
    override fun getLanguage(): Language {
        return YggdrasilLanguage
    }

    override fun getCodeSample(p0: SettingsType): String? {
        return null
    }
}