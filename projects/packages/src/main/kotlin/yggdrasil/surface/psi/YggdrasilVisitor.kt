package yggdrasil.surface.psi.nodes

import com.intellij.psi.PsiElementVisitor

open class YggdrasilVisitor : PsiElementVisitor() {
    open fun visitDefineClass(node: YggdrasilDefineClass) {}
    open fun visitDefineUnion(node: YggdrasilDefineUnion) {}
    open fun visitDefineFunction(node: YggdrasilDefineFunction) {}
}
