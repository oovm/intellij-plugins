package yggdrasil.semantic.symbol

enum class ValkyrieSymbolKind {
    ValkyrieClass {

    },
    ValkyrieTrait {
        override fun isTrait(): Boolean {
            return true
        }
    };

    open fun isTrait(): Boolean {
        return false
    }
}