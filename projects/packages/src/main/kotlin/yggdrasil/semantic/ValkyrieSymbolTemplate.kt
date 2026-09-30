package yggdrasil.semantic.symbol

import com.intellij.model.Pointer
import com.intellij.model.Symbol
import com.intellij.psi.PsiElement

@Suppress("UnstableApiUsage")
open class ValkyrieSymbolTemplate(open val target: PsiElement) : Symbol {
    override fun createPointer(): Pointer<out Symbol> {
        TODO("Not yet implemented")
    }
}

