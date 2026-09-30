package jss.editing.highlight

import jss.surface.file.MessageBundle
import jss.surface.file.JssIcons
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage

class ColorSettings : ColorSettingsPage {
    private val annotatorTags = JssColor
        .values()
        .associateBy({ it.name }, { it.textAttributesKey })

    override fun getAttributeDescriptors() = JssColor
        .values()
        .map { it.attributesDescriptor }
        .toTypedArray()

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName() = MessageBundle.message("filetype.name")

    override fun getIcon() = JssIcons.FILE

    override fun getHighlighter() = HighlightToken()

    override fun getAdditionalHighlightingTagToDescriptorMap() = annotatorTags

    override fun getDemoText() = javaClass.getResource("/fileTemplates/demoColor.jss")!!.readText()
}
