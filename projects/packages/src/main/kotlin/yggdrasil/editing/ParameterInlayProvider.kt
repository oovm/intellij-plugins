package yggdrasil.editing.inlay

import com.intellij.codeInsight.hints.HintInfo
import com.intellij.codeInsight.hints.InlayInfo
import com.intellij.codeInsight.hints.InlayParameterHintsProvider
import com.intellij.codeInsight.hints.Option
import com.intellij.psi.PsiElement
import yggdrasil.surface.psi.ValkyrieInlayElement


@Suppress("UnstableApiUsage")
class ParameterInlayProvider : InlayParameterHintsProvider {
    var context = ""

    override fun getHintInfo(element: PsiElement): HintInfo? {
        return HintInfo.MethodInfo("aaa", listOf("bbb"))
    }


    /// 函数里面的东西
    override fun getParameterHints(element: PsiElement): MutableList<InlayInfo> {
        val visitor = ParameterInlayHint();
        if (element is ValkyrieInlayElement) {
            element.parameter_hint(visitor)
        }
        return visitor.info;
    }


    override fun getInlayPresentation(inlayText: String): String {
        // 取消 inlay 的后处理
        return inlayText
    }

    override fun getProperty(key: String?): String? {
        return super.getProperty(key)
    }

    override fun getMainCheckboxText(): String {
        return "getMainCheckboxText"
    }

    /// 显示在
    /// Editor > Inlay Hints > Parameter Names
    override fun getDescription(): String {
        return "Shows parameter names at function/macro call sites."
    }

    /// 显示在
    /// Editor > Inlay Hints > Parameter Names
    override fun getDefaultBlackList(): Set<String> = setOf(
        "derive", "matches", "Some",
    )

    /// 显示在
    /// Editor > Inlay Hints > Parameter Names > Valkyrie
    override fun getSupportedOptions(): MutableList<Option> {
        return mutableListOf(
            Option("getSupportedOptions1", yggdrasil.definition.YggdrasilBundle.messagePointer("color.token.null"), true),
            Option("getSupportedOptions2", yggdrasil.definition.YggdrasilBundle.messagePointer("color.token.boolean"), true)
        )
    }
}
