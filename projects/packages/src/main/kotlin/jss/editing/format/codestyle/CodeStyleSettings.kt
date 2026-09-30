package jss.editing.format.codestyle

import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

class CodeStyleSettings(settings: CodeStyleSettings?) : CustomCodeStyleSettings(
    "VosCodeStyleSettings",
    settings!!
)
