package yggdrasil.semantic.symbol


interface ContextProvider {
    fun resolveSymbols(symbols: List<ValkyrieSymbolTemplate>): List<ValkyrieSymbolTemplate>
}