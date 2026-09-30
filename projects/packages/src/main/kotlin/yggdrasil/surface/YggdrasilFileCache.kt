package yggdrasil.surface.file

import yggdrasil.surface.psi.YggdrasilDeclaration
import yggdrasil.surface.psi.nodes.YggdrasilDefineClassNode
import yggdrasil.surface.psi.nodes.YggdrasilDefineUnionNode
import yggdrasil.surface.psi.nodes.YggdrasilGroupNode

class YggdrasilFileCache {
    private val root: YggdrasilFileNode


    constructor(root: YggdrasilFileNode) {
        this.root = root
    }

    fun getDefinitions(): Sequence<YggdrasilDeclaration> {
        return sequence {
            for (child in root.children) {
                when (child) {
                    is YggdrasilDefineClassNode -> {
                        yield(child)
                    }

                    is YggdrasilDefineUnionNode -> {
                        yield(child)
                    }

                    is YggdrasilGroupNode -> {
                        for (item in child.tokenList) {
                            yield(item)
                        }
                    }
                }
            }
        }
    }
}
