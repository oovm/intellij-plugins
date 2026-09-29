package com.github.projectfluent.ide.view

import com.github.projectfluent.language.psi.nodes.FluentAttributeNode
import com.github.projectfluent.language.psi.nodes.FluentFileNode
import com.github.projectfluent.language.psi.nodes.FluentMessageNode
import com.github.projectfluent.language.psi.nodes.FluentTermNode
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.icons.AllIcons

class FluentStructureViewElement(private val node: NavigatablePsiElement) :
    StructureViewTreeElement,
    SortableTreeElement {
    override fun getValue(): Any {
        return node
    }

    override fun navigate(requestFocus: Boolean) {
        node.navigate(requestFocus)
    }

    override fun canNavigate(): Boolean {
        return node.canNavigate()
    }

    override fun canNavigateToSource(): Boolean {
        return node.canNavigateToSource()
    }

    override fun getAlphaSortKey(): String {
        val name = node.name
        return name ?: ""
    }

    override fun getPresentation(): ItemPresentation {
        val name = node.name ?: ""
        return FluentItemPresentation(null, name)
    }

    override fun getChildren(): Array<out TreeElement> = when (node) {
        is FluentFileNode -> getChildOfType(FluentMessageNode::class.java, FluentTermNode::class.java)
        is FluentMessageNode, is FluentTermNode -> getChildOfType(FluentAttributeNode::class.java)
        is FluentAttributeNode -> arrayOf()
        else -> getChildOfType(NavigatablePsiElement::class.java)
    }

    private fun getChildOfType(vararg classes: Class<*>): Array<FluentStructureViewElement> {
        val children = mutableListOf<FluentStructureViewElement>()
        for (child in node.children) {
            for (clazz in classes) {
                if (clazz.isInstance(child) && child is NavigatablePsiElement) {
                    children.add(FluentStructureViewElement(child))
                    break
                }
            }
        }
        return children.toTypedArray()
    }
}