package com.github.projectfluent.ide

import com.github.projectfluent.language.psi.FluentTypes
import com.github.projectfluent.language.psi.nodes.FluentAttributeIDNode
import com.github.projectfluent.language.psi.nodes.FluentFileNode
import com.github.projectfluent.language.psi.nodes.FluentFunctionIDNode
import com.github.projectfluent.language.psi.nodes.FluentMessageIDNode
import com.github.projectfluent.language.psi.nodes.FluentTermIDNode
import com.github.projectfluent.language.psi.nodes.FluentVariableIDNode
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.io.File

class FluentHighlightTest : BasePlatformTestCase() {
    override fun getTestDataPath(): String {
        return "src/test/testData"
    }

    fun testBasicHighlighting() {
        doHighlightTest("basic")
    }

    fun testKeyHighlighting() {
        doHighlightTest("test-keys")
    }

    fun testDotMessage() {
        doHighlightTest("dot-message")
    }

    fun testStringInterpolation() {
        doHighlightTest("string-interpolation")
    }

    private fun doHighlightTest(baseName: String) {
        val rawFile = File(testDataPath, "highlighting/$baseName.ftl")
        val expectedFile = File(testDataPath, "highlighting/$baseName.txt")

        val raw = StringUtil.convertLineSeparators(FileUtil.loadFile(rawFile)).trimEnd()
        val expected = if (!expectedFile.exists() || System.getProperty("regenerate") == "true") {
            val generated = generateHighlightMarkup(baseName)
            FileUtil.writeToFile(expectedFile, generated)
            generated
        } else {
            StringUtil.convertLineSeparators(FileUtil.loadFile(expectedFile)).trimEnd()
        }
        assertEquals(raw, stripHighlightingMarkup(expected))

        myFixture.configureByText("$baseName.ftl", expected)
        myFixture.checkHighlighting()
    }

    private fun stripHighlightingMarkup(text: String): String {
        return HIGHLIGHTING_TAG_REGEX.replace(text, "")
    }

    private fun generateHighlightMarkup(baseName: String): String {
        val file = myFixture.configureByFile("highlighting/$baseName.ftl")
        assertInstanceOf(file, FluentFileNode::class.java)

        val tags = PsiTreeUtil.collectElements(file) { true }
            .mapNotNull(::toHighlightTag)
            .sortedByDescending { it.startOffset }

        val text = StringBuilder(StringUtil.convertLineSeparators(file.text))
        tags.forEach { tag ->
            text.insert(tag.endOffset, "</${tag.name}>")
            text.insert(tag.startOffset, "<${tag.name}>")
        }
        return text.toString().trimEnd()
    }

    private fun toHighlightTag(element: PsiElement): HighlightTag? {
        val range = element.textRange ?: return null
        if (range.isEmpty) {
            return null
        }

        val tagName = when (element) {
            is FluentMessageIDNode -> if (element.parent.node.elementType == FluentTypes.MESSAGE) {
                "SYM_MESSAGE"
            } else {
                "SYM_MESSAGE_REFERENCE"
            }
            is FluentTermIDNode -> "SYM_TERM"
            is FluentAttributeIDNode -> if (element.parent.node.elementType == FluentTypes.ATTRIBUTE) {
                "SYM_ATTRIBUTE"
            } else {
                "SYM_ATTRIBUTE_REFERENCE"
            }
            is FluentVariableIDNode -> "SYM_VARIABLE"
            is FluentFunctionIDNode -> "SYM_FUNCTION"
            else -> if (element.node.elementType == FluentTypes.VARIANT_KEY) "SYM_TERM" else null
        } ?: return null

        return HighlightTag(range.startOffset, range.endOffset, tagName)
    }

    companion object {
        private val HIGHLIGHTING_TAG_REGEX = Regex("""</?[A-Za-z_][A-Za-z0-9_:-]*(?:\s+[^<>]*)?>""")
    }

    private data class HighlightTag(
        val startOffset: Int,
        val endOffset: Int,
        val name: String,
    )
}
