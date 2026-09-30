package yggdrasil.editing.inlay

import com.intellij.codeInsight.codeVision.settings.CodeVisionGroupDefaultSettingModel
import com.intellij.codeInsight.hints.settings.InlayProviderSettingsModel
import com.intellij.codeInsight.hints.settings.InlaySettingsProvider
import com.intellij.lang.Language
import com.intellij.openapi.project.Project

class ValkyrieInlaySettingsProvider : InlaySettingsProvider {
    override fun createModels(project: Project, language: Language): List<InlayProviderSettingsModel> {
        return listOf(
            CodeVisionGroupDefaultSettingModel(
                "ValkyrieInlaySettingsProvider",
                "ValkyrieInlaySettingsProvider",
                "ValkyrieInlaySettingsProvider",
                true,
                listOf()
            )
        )
    }

    override fun getSupportedLanguages(project: Project): Collection<Language> {
        return listOf(yggdrasil.definition.YggdrasilLanguage)
    }
}