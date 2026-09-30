package yggdrasil.surface.ast


import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.util.PsiTreeUtil

val PsiElement.ancestors: Sequence<PsiElement>
    get() = generateSequence(this) {
        if (it is PsiFile) null else it.parent
    }

val PsiElement.childrenWithLeaves: Sequence<PsiElement>
    get() = generateSequence(this.firstChild) { it.nextSibling }

inline fun <reified T : PsiElement> PsiElement.ancestorStrict(): T? =
    PsiTreeUtil.getParentOfType(this, T::class.java, /* strict */ true)

inline fun <reified T : PsiElement> PsiElement.ancestorStrict(stopAt: Class<out PsiElement>): T? =
    PsiTreeUtil.getParentOfType(this, T::class.java, /* strict */ true, stopAt)

inline fun <reified T : PsiElement> PsiElement.ancestorOrSelf(): T? =
    PsiTreeUtil.getParentOfType(this, T::class.java, /* strict */ false)

inline fun <reified T : PsiElement> PsiElement.ancestorOrSelf(stopAt: Class<out PsiElement>): T? =
    PsiTreeUtil.getParentOfType(this, T::class.java, /* strict */ false, stopAt)

/**
 * Same as [ancestorOrSelf], but with "fake" parent links. See [org.rust.lang.core.macros.RsExpandedElement].
 */
inline fun <reified T : PsiElement> PsiElement.contextOrSelf(): T? =
    PsiTreeUtil.getContextOfType(this, T::class.java, /* strict */ false)


/** Finds first sibling that is neither comment, nor whitespace before given element */
fun PsiElement?.getPrevNonCommentSibling(): PsiElement? =
    PsiTreeUtil.skipWhitespacesAndCommentsBackward(this)


/** Finds first sibling that is not whitespace before given element */
fun PsiElement?.getPrevNonWhitespaceSibling(): PsiElement? =
    PsiTreeUtil.skipWhitespacesBackward(this)

/** Finds first sibling that is not whitespace after given element */
fun PsiElement?.getNextNonWhitespaceSibling(): PsiElement? =
    PsiTreeUtil.skipWhitespacesForward(this)

fun PsiElement.isAncestorOf(child: PsiElement): Boolean =
    child.ancestors.contains(this)

val PsiElement.endOffsetInParent: Int
    get() = startOffsetInParent + textLength

fun PsiElement.rangeWithPrevSpace(prev: PsiElement?): TextRange = when (prev) {
    is PsiWhiteSpace -> textRange.union(prev.textRange)
    else -> textRange
}

val PsiElement.rangeWithPrevSpace: TextRange
    get() = rangeWithPrevSpace(prevSibling)

val PsiElement.rangeWithSurroundingLineBreaks: TextRange
    get() {
        val startOffset = textRange.startOffset
        val endOffset = textRange.endOffset
        val text = containingFile.text
        val newLineBefore = text.lastIndexOf('\n', startOffset).takeIf { it >= 0 }?.let { it + 1 } ?: startOffset
        val newLineAfter = text.indexOf('\n', endOffset).takeIf { it >= 0 }?.let { it + 1 } ?: endOffset
        return TextRange(newLineBefore, newLineAfter)
    }

private fun PsiElement.getLineCount(): Int {
    val doc = PsiDocumentManager.getInstance(project).getDocument(containingFile)
    if (doc != null) {
        val spaceRange = textRange ?: TextRange.EMPTY_RANGE

        if (spaceRange.endOffset <= doc.textLength) {
            val startLine = doc.getLineNumber(spaceRange.startOffset)
            val endLine = doc.getLineNumber(spaceRange.endOffset)

            return endLine - startLine
        }
    }

    return (text ?: "").count { it == '\n' } + 1
}

fun PsiWhiteSpace.isMultiLine(): Boolean = getLineCount() > 1

@Suppress("UNCHECKED_CAST")
inline val <T : StubElement<*>> StubBasedPsiElement<T>.greenStub: T?
    get() = (this as? StubBasedPsiElementBase<T>)?.greenStub

/** true = continue, false = break */
fun PsiElement.traversal(filter: (PsiElement) -> Boolean) {
    var needSearch = children.toList();
    while (needSearch.isNotEmpty()) {
        val nextSearch = mutableListOf<PsiElement>();
        for (node in needSearch) {
            if (filter(node)) {
                nextSearch.addAll(node.children);
            }
        }
        needSearch = nextSearch
    }
}

fun PsiFile?.caretElement(editor: Editor?): PsiElement? {
    val offset = editor?.caretModel?.offset ?: return null
    return this?.findElementAt(offset)
}
