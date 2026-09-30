package witx.editing.highlight

import witx.definition.WitxBundle
import witx.surface.file.WitxIcons
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage

class WitxHighlightSetting : ColorSettingsPage {
    private val annotatorTags = WitxHighlightColor.entries.associate { it.name to it.textAttributesKey }

    override fun getAttributeDescriptors() =
        WitxHighlightColor.entries.map { it.attributesDescriptor }.toTypedArray()

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = WitxBundle.message("filetype.name")

    override fun getIcon() = WitxIcons.FILE

    override fun getHighlighter() = WitxSyntaxHighlighter()

    override fun getAdditionalHighlightingTagToDescriptorMap() = annotatorTags

    override fun getDemoText(): String =
        javaClass.getResource("/fileTemplates/colorDemo.witx")!!.readText()
}
