/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package jss.editing.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.SyntaxTraverser
import jss.surface.file.JssFileNode
import jss.surface.psi.VosClassStatementNode
import java.util.function.Consumer


class DocumentationProvider : DocumentationProvider {
    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        return when (element) {
            is VosClassStatementNode -> {
                "class ${element.name}"
            }
            else -> {
                "<h1>generateDoc Unknown</h1>"
            }
        }
    }

    override fun generateHoverDoc(element: PsiElement, originalElement: PsiElement?): String? {
        return super.generateDoc(element, originalElement);
        // return "<h1>generateHoverDoc</h1>"
    }

    override fun generateRenderedDoc(comment: PsiDocCommentBase): String? {
        return DocumentationMarkup.DEFINITION_ELEMENT.addRaw("generateRenderedDoc").toString()
    }

    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {

        return "<h1>getQuickNavigateInfo</h1>"
    }


    override fun findDocComment(file: PsiFile, range: TextRange): PsiDocCommentBase? {
        return super.findDocComment(file, range)
    }


    override fun getCustomDocumentationElement(
        editor: Editor,
        file: PsiFile,
        contextElement: PsiElement?,
        targetOffset: Int
    ): PsiElement? {
        return super.getCustomDocumentationElement(editor, file, contextElement, targetOffset)
    }


    override fun collectDocComments(file: PsiFile, sink: Consumer<in PsiDocCommentBase>) {
        if (file !is JssFileNode) return
        for (element in SyntaxTraverser.psiTraverser(file)) {
            when (element) {
                is PsiDocCommentBase -> sink.accept(element)
                // else -> break
            }
        }
    }
}