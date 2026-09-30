package yggdrasil.editing.inlay

import com.intellij.codeInsight.hints.InlayInfo

@Suppress("UnstableApiUsage")
class ParameterInlayHint {
    var info: MutableList<InlayInfo> = mutableListOf()

    fun inline(start: Int, text: String) {
        info.add(InlayInfo(text, start))
    }
}