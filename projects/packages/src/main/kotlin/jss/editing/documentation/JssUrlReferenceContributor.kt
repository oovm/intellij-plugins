/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package jss.editing.documentation


import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import jss.surface.psi.JssUrlMaybeValidNode


class JssUrlReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        val psiLiteralExpressionCapture: PsiElementPattern.Capture<JssUrlMaybeValidNode> = PlatformPatterns.psiElement(
            JssUrlMaybeValidNode::class.java,
        )
        registrar.registerReferenceProvider(psiLiteralExpressionCapture, JssUrlReferenceProvider())
    }
}