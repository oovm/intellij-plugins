package com.github.projectfluent.ide.view

import com.github.projectfluent.language.psi.nodes.FluentAttributeNode
import com.github.projectfluent.language.psi.nodes.FluentFileNode
import com.github.projectfluent.language.psi.nodes.FluentMessageNode
import com.github.projectfluent.language.psi.nodes.FluentTermNode
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class FluentStructureViewTest : BasePlatformTestCase() {
    override fun getTestDataPath(): String = "src/test/testData"

    fun testMessagesTermsAndAttributes() {
        val file = myFixture.configureByText(
            "sample.ftl",
            """
            hello = Hello
                .title = Title
            -brand = Brand
                .gender = masculine
            """.trimIndent(),
        ) as FluentFileNode

        val root = FluentStructureViewElement(file)
        val top = root.children.map { (it as FluentStructureViewElement).value }
        assertEquals(2, top.size)
        assertTrue(top[0] is FluentMessageNode)
        assertTrue(top[1] is FluentTermNode)

        val messageChildren = FluentStructureViewElement(top[0] as FluentMessageNode).children
        assertEquals(1, messageChildren.size)
        assertTrue((messageChildren[0] as FluentStructureViewElement).value is FluentAttributeNode)

        val termChildren = FluentStructureViewElement(top[1] as FluentTermNode).children
        assertEquals(1, termChildren.size)
        assertTrue((termChildren[0] as FluentStructureViewElement).value is FluentAttributeNode)
    }
}
