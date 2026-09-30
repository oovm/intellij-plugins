package yggdrasil.editing.gutter

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import javax.swing.Icon

fun yggdrasilLineMarker(element: PsiElement, mark: Icon): LineMarkerInfo<PsiElement> =
    LineMarkerInfo(
        element,
        element.textRange,
        mark,
        null,
        null,
        GutterIconRenderer.Alignment.CENTER,
        { "Yggdrasil" },
    )
