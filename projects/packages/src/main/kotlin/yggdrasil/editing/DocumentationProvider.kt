package yggdrasil.editing.documentation

import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import java.net.ConnectException
import java.util.function.Consumer


class DocumentationProvider : DocumentationProvider {
    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        if (element == null) return null
        try {
            return DocumentationRenderer(element, originalElement).onHover()
        } catch (e: ConnectException) {
            // do nothing
        }
        return null
    }


    override fun findDocComment(file: PsiFile, range: TextRange): PsiDocCommentBase? {
        println("findDocComment $file $range")
        return super.findDocComment(file, range)
    }

    override fun collectDocComments(file: PsiFile, sink: Consumer<in PsiDocCommentBase>) {
//        if (file !is ValkyrieFileNode) return
        for (leaf in PsiTreeUtil.findChildrenOfType(file, PsiComment::class.java)) {
//            val text = ValkyrieCommenter.extractDocumentText(leaf)
//            if (text != null) {
//                sink.accept(DocumentNode(leaf, text))
//            }
        }
    }

    override fun generateRenderedDoc(comment: PsiDocCommentBase): String? {
//        if (comment is ValkyrieCommentDocument) {
//            return comment.render()
//        }
        return null
    }

    // 按住 Ctrl 后悬浮
    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {
        return originalElement?.let { this.generateHoverDoc(it, originalElement) }
    }

    // 悬浮
    override fun generateHoverDoc(element: PsiElement, originalElement: PsiElement?): String? {
        try {
            return DocumentationRenderer(element, originalElement).onHover()
        } catch (_: ConnectException) {
            // skip
        }
        return null
    }

    override fun getCustomDocumentationElement(editor: Editor, file: PsiFile, contextElement: PsiElement?, targetOffset: Int): PsiElement? {
        return when {
//            ParserExtension.Keywords.contains(contextElement.elementType) -> contextElement
//            ParserExtension.Operators.contains(contextElement.elementType) -> contextElement
            contextElement.elementType == TokenType.WHITE_SPACE -> null
            else -> null
        }
    }

    override fun getDocumentationElementForLink(psiManager: PsiManager?, link: String?, context: PsiElement?): PsiElement? {
        return super.getDocumentationElementForLink(psiManager, link, context)
    }

    override fun getDocumentationElementForLookupItem(psiManager: PsiManager?, `object`: Any?, element: PsiElement?): PsiElement? {
        return super.getDocumentationElementForLookupItem(psiManager, `object`, element)
    }

    override fun getUrlFor(element: PsiElement?, originalElement: PsiElement?): MutableList<String>? {
        return super.getUrlFor(element, originalElement)
    }
}