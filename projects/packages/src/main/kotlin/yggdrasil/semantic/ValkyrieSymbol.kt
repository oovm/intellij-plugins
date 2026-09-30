package yggdrasil.semantic.symbol

import com.intellij.model.Pointer
import com.intellij.model.Symbol

@Suppress("UnstableApiUsage")
class ValkyrieSymbol(val packageName: String = "", val namespace: String = "", val name: String = "") : Pointer<ValkyrieSymbol>, Symbol {
    override fun dereference(): ValkyrieSymbol? {
        return this
    }

    override fun createPointer(): Pointer<out Symbol> {
        return this
    }
}
