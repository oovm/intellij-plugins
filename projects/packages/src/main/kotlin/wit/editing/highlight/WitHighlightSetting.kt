package wit.editing.highlight

import wit.definition.WitBundle
import wit.surface.file.WitIcons
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage

class WitHighlightSetting : ColorSettingsPage {
    private val annotatorTags = WitHighlightColor.entries.associate { it.name to it.textAttributesKey }

    override fun getAttributeDescriptors() =
        WitHighlightColor.entries.map { it.attributesDescriptor }.toTypedArray()

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = WitBundle.message("filetype.name")

    override fun getIcon() = WitIcons.FILE

    override fun getHighlighter() = WitSyntaxHighlighter()

    override fun getAdditionalHighlightingTagToDescriptorMap() = annotatorTags

    override fun getDemoText(): String =
        javaClass.getResource("/fileTemplates/colorDemo.wit")!!.readText()
}
