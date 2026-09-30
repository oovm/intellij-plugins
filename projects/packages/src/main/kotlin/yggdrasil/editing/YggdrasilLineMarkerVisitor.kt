package yggdrasil.editing.gutter

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import yggdrasil.surface.psi.nodes.YggdrasilDefineClass
import yggdrasil.surface.psi.nodes.YggdrasilDefineFunction
import yggdrasil.surface.psi.nodes.YggdrasilDefineUnion
import yggdrasil.surface.psi.nodes.YggdrasilVisitor
import javax.swing.Icon

class YggdrasilLineMarkerVisitor : YggdrasilVisitor {
    private val marks: MutableCollection<in LineMarkerInfo<*>>

    constructor(result: MutableCollection<in LineMarkerInfo<*>>) : super() {
        this.marks = result
    }

    override fun visitDefineClass(o: YggdrasilDefineClass) {
        mark(o.nameIdentifier, AllIcons.Nodes.Class)
    }

    override fun visitDefineUnion(o: YggdrasilDefineUnion) {
        mark(o.nameIdentifier, AllIcons.Nodes.Interface)
    }

    override fun visitDefineFunction(o: YggdrasilDefineFunction) {
        super.visitDefineFunction(o)
    }

    private fun mark(element: PsiElement?, icon: Icon) {
        if (element != null) {
            marks.add(yggdrasilLineMarker(element, icon))
        }
    }
}