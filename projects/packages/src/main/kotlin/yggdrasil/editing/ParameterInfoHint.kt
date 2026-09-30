package yggdrasil.editing.inlay

import com.intellij.lang.parameterInfo.CreateParameterInfoContext
import com.intellij.lang.parameterInfo.ParameterInfoHandler
import com.intellij.lang.parameterInfo.ParameterInfoUIContext
import com.intellij.lang.parameterInfo.UpdateParameterInfoContext
import com.intellij.psi.PsiElement

class ParameterInfoHint : ParameterInfoHandler<PsiElement, PsiElement> {
    override fun findElementForParameterInfo(context: CreateParameterInfoContext): PsiElement? {
        return context.highlightedElement
    }

    override fun findElementForUpdatingParameterInfo(context: UpdateParameterInfoContext): PsiElement? {
        TODO("Not yet implemented")
    }

    override fun updateUI(p: PsiElement?, context: ParameterInfoUIContext) {
        TODO("Not yet implemented")
    }

    override fun updateParameterInfo(parameterOwner: PsiElement, context: UpdateParameterInfoContext) {
        TODO("Not yet implemented")
    }

    override fun showParameterInfo(element: PsiElement, context: CreateParameterInfoContext) {
        TODO("Not yet implemented")
    }
}

