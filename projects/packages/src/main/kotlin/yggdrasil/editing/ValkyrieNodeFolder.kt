package yggdrasil.editing.assist

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement


class ValkyrieNodeFolder {

    private val descriptors: MutableList<FoldingDescriptor>

    constructor(descriptors: MutableList<FoldingDescriptor>) : super() {
        this.descriptors = descriptors
    }

    fun fold(node: PsiElement, range: TextRange) {
        fold(node, range.startOffset, range.endOffset)
    }

    fun fold(node: PsiElement, start: Int, end: Int) {
        if (end > start) {
            descriptors += FoldingDescriptor(node, TextRange(start, end))
        }
    }
}


