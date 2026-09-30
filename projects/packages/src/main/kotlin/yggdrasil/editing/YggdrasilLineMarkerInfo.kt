package yggdrasil.editing.gutter

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.editor.markup.GutterIconRenderer.Alignment
import com.intellij.openapi.editor.markup.MarkupEditorFilter
import com.intellij.psi.PsiElement
import javax.swing.Icon

@Suppress("DEPRECATION")
class YggdrasilLineMarkerInfo : LineMarkerInfo<PsiElement> {
    constructor(element: PsiElement, mark: Icon) : super(element, element.textRange, mark, null, null, Alignment.CENTER)

    override fun getLineMarkerTooltip(): String {
        return "GetLineMarkerTooltip()"
    }

    override fun getEditorFilter(): MarkupEditorFilter {
        return super.getEditorFilter()
    }

    override fun createGutterRenderer(): GutterIconRenderer {
        return LineMarkerGutterIconRenderer(this)
    }
}