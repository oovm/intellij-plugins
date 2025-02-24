package vos.editing.highlight

import vos.surface.file.MessageBundle
import vos.surface.file.VosIcons
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage

class ColorSettings : ColorSettingsPage {
    private val annotatorTags = VosColor
        .values()
        .associateBy({ it.name }, { it.textAttributesKey })

    override fun getAttributeDescriptors() = VosColor
        .values()
        .map { it.attributesDescriptor }
        .toTypedArray()

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName() = MessageBundle.message("filetype.name")

    override fun getIcon() = VosIcons.FILE

    override fun getHighlighter() = HighlightToken()

    override fun getAdditionalHighlightingTagToDescriptorMap() = annotatorTags

    override fun getDemoText() = javaClass.getResource("/fileTemplates/demoColor.vos")!!.readText()
}
