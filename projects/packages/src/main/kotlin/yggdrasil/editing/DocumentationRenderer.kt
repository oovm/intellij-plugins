package yggdrasil.editing.documentation


import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import com.intellij.psi.PsiElement
import com.intellij.ui.ColorUtil
import yggdrasil.editing.highlight.HighlightColor


class DocumentationRenderer(var element: PsiElement, private var original: PsiElement?) {
    private val doc = StringBuilder()
    fun onHover(): String {
        return doc.toString()
    }

    /// get the path relative to the project root
    /// FIXME: get real declare module
    private fun appendNamespace() {
        val file = element.containingFile;
        // fake module path
        val path = file.virtualFile.path.substringAfter("src/").replace("/", "::").replace(".vk", "")
        append(path)
    }

    fun append(text: String) {
        doc.append("<span>${text}</span>")
    }

    fun append(key: HighlightColor, text: String) {
        // HtmlSyntaxInfoUtil.getStyledSpan(key.textAttributesKey, text, 1.0f)
        val attr = EditorColorsManager.getInstance().globalScheme.getAttributes(key.textAttributesKey)
        val color = ColorUtil.toHtmlColor(attr.foregroundColor)
        doc.append("<span style='color:${color}'>${text}</span>")
    }

    private fun appendHighlight(code: String) {
        HtmlSyntaxInfoUtil.appendHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
            doc,
            element.project,
            yggdrasil.definition.YggdrasilLanguage,
            code.trimIndent(),
            1.0f,
        )
    }

    private fun appendAdd() {
        doc.append("<span>+</span>")
    }
}


