package yggdrasil.editing.completion

import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateBuilderImpl
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.codeInsight.template.impl.VariableNode
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement

private val TemplateCache: MutableMap<String, Template> = mutableMapOf()

class TemplateBuilder(val element: PsiElement, val editor: Editor) : TemplateBuilderImpl(element) {
    private val manager: TemplateManager get() = TemplateManager.getInstance(element.project)
    fun runFromText(key: String, text: String, discard: Map<String, String>) {
        if (!TemplateCache.containsKey(key)) {
            createFromText(key, text, discard)
        }
        manager.startTemplate(editor, TemplateCache[key]!!)
    }

    private fun createFromText(key: String, text: String, discard: Map<String, String>) {
        val template = manager.createTemplate(key, "valkyrie", text)
        val token = "[$]([a-zA-Z0-9]+)[$]".toRegex()
        val defined = mutableSetOf<String>()
        for (variable in token.findAll(text)) {
            defined.add(variable.groups[1]!!.value)
        }
        for (name in discard.keys) {
            if (defined.contains(name)) {
                defined.remove(name)
                template.addVariable(name, ConstantNode(discard[name]!!), false)
            }
        }
        for (name in defined) {
            template.addVariable(name, VariableNode(name, ConstantNode(name)), true)
        }
        TemplateCache[key] = template
    }
}

