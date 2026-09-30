package yggdrasil.semantic.symbol

import yggdrasil.editing.highlight.HighlightColor
import yggdrasil.surface.psi.nodes.YggdrasilIdentifierNode

data class ValkyrieVariableKind(
    val identifier: YggdrasilIdentifierNode,
    val color: HighlightColor,
)