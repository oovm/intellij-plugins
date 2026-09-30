package yggdrasil.surface.psi

import yggdrasil.editing.assist.ValkyrieNodeFolder

interface YggdrasilFoldableElement {
    @Suppress("FunctionName")
    fun on_fold(e: ValkyrieNodeFolder);
}