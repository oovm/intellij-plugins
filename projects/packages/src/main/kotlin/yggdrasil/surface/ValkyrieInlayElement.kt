package yggdrasil.surface.psi

import yggdrasil.editing.inlay.ParameterInlayHint

@Suppress("FunctionName")
interface ValkyrieInlayElement {

    fun parameter_hint(inlay: ParameterInlayHint): Boolean {
        return false
    }
}