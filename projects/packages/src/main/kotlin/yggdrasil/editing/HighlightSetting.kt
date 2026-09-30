package yggdrasil.editing.highlight

import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import yggdrasil.surface.file.YggdrasilIconProvider

class HighlightSetting : ColorSettingsPage {
    private val annotatorTags = HighlightColor
        .values()
        .associateBy({ it.name }, { it.textAttributesKey })

    override fun getAttributeDescriptors() = HighlightColor
        .values()
        .map { it.attributesDescriptor }
        .toTypedArray()

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName() = yggdrasil.definition.YggdrasilBundle.message("filetype.name")

    override fun getIcon() = YggdrasilIconProvider.Instance.Yggdrasil

    override fun getHighlighter() = YggdrasilSyntaxHighlighter()

    override fun getAdditionalHighlightingTagToDescriptorMap() = annotatorTags

    override fun getDemoText(): String =
        javaClass.getResource("/fileTemplates/demoColor.yggdrasil")!!.readText()
}

