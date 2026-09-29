package com.github.projectfluent.language

import com.github.projectfluent.language.psi.FluentTypes
import com.github.projectfluent.language.psi.nodes.FluentMessageNode
import com.github.projectfluent.language.psi.nodes.FluentFileNode
import com.intellij.openapi.util.io.FileUtil
import com.intellij.psi.impl.DebugUtil
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.openapi.util.text.StringUtil
import java.io.File

class FluentParserTest : BasePlatformTestCase() {
    override fun getTestDataPath(): String = "src/test/testData"

    fun testEmpty() = doParseTest("empty.ftl")

    fun testIssue8() = doParseTest("issue8.ftl")

    fun testSimpleMessages() = doParseTest("SimpleMessages.ftl")

    fun testPlaceables() = doParseTest("Placeables.ftl")

    fun testAttributes() = doParseTest("attributes.ftl")

    fun testTerms() = doParseTest("terms.ftl")

    fun testSelectExpressions() = doParseTest("SelectExpressions.ftl")

    fun testFunctionCalls() = doParseTest("FunctionCalls.ftl")

    fun testNestedPlaceables() = doParseTest("NestedPlaceables.ftl")

    fun testDotMessageBoundaries() {
        val file = myFixture.configureByText(
            "dot-message.ftl",
            """
            title = This thing
            
            description = Blah blah blah.
            
            message = This is message!
            """.trimIndent()
        )
        assertInstanceOf(file, FluentFileNode::class.java)

        val error = PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java)
        assertNull("Unexpected parse error: ${error?.errorDescription}", error)

        val messages = PsiTreeUtil.getChildrenOfTypeAsList(file, FluentMessageNode::class.java)
        assertEquals(listOf("title", "description", "message"), messages.map { it.firstChild.text })
    }

    fun testDefaultVariantStarIsNotInlineText() {
        val file = myFixture.configureByText(
            "default-variant.ftl",
            """
            gender = { ${'$'}gender ->
                [male] He
                [female] She
                * [other] They
            }
            """.trimIndent()
        )
        assertInstanceOf(file, FluentFileNode::class.java)

        val error = PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java)
        assertNull("Unexpected parse error: ${error?.errorDescription}", error)

        val inlineStars = PsiTreeUtil.collectElements(file) {
            it.node?.elementType == FluentTypes.INLINE_TEXT && it.text == "*"
        }
        assertEmpty(inlineStars.asList())

        val starTokens = PsiTreeUtil.collectElements(file) {
            it.node?.elementType == FluentTypes.STAR
        }
        assertEquals(1, starTokens.size)

        val variantKeys = PsiTreeUtil.collectElements(file) {
            it.node?.elementType == FluentTypes.VARIANT_KEY && it.text == "other"
        }
        assertEquals(1, variantKeys.size)
    }

    fun testNestedDefaultVariantKeyIsNotInlineText() {
        val file = myFixture.configureByText(
            "nested-default-variant.ftl",
            """
            complex-message =
              {${'$'}userName} {${'$'}photoCount ->
                 [1] added one photo to {${'$'}userGender ->
                       [male] his stream
                       [female] her stream
                      *[other] their stream
                     }
                *[other] added {${'$'}photoCount} photos to {${'$'}userGender ->
                       [male] his stream
                       [female] her stream
                      *[other] their stream
                     }
              }
            """.trimIndent()
        )
        assertInstanceOf(file, FluentFileNode::class.java)

        val error = PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java)
        assertNull("Unexpected parse error: ${error?.errorDescription}", error)

        val inlineOtherKeys = PsiTreeUtil.collectElements(file) {
            it.node?.elementType == FluentTypes.INLINE_TEXT && it.text.contains("other")
        }
        assertEmpty(inlineOtherKeys.asList())

        val variantKeys = PsiTreeUtil.collectElements(file) {
            it.node?.elementType == FluentTypes.VARIANT_KEY && it.text == "other"
        }
        assertEquals(3, variantKeys.size)
    }

    fun testTermReferenceIsNotSplitIntoInlineText() {
        val file = myFixture.configureByText(
            "term-reference.ftl",
            """
            hello = Hello, world!
            -brand-name = Firefox
            installing = Installing { -brand-name }.
            """.trimIndent()
        )
        assertInstanceOf(file, FluentFileNode::class.java)

        val error = PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java)
        assertNull("Unexpected parse error: ${error?.errorDescription}", error)

        val splitInlineText = PsiTreeUtil.collectElements(file) {
            it.node?.elementType == FluentTypes.INLINE_TEXT &&
                (it.text == "-" || it.text == "brand-name")
        }
        assertEmpty(splitInlineText.asList())

        val termReferences = PsiTreeUtil.collectElements(file) {
            it.node?.elementType == FluentTypes.TERM_ID && it.text == "-brand-name"
        }
        assertEquals(2, termReferences.size)
    }

    fun testIfSelectHeaderOperatorsAreNotInlineText() {
        val file = myFixture.configureByText(
            "if-select-header.ftl",
            """
            complex-nested =
              Hello, {if {${'$'}unreadCount} > 0 ->
                 [true] one
                *[false] none
              }
            """.trimIndent()
        )
        assertInstanceOf(file, FluentFileNode::class.java)

        val error = PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java)
        assertNull("Unexpected parse error: ${error?.errorDescription}", error)

        val badInlineTokens = PsiTreeUtil.collectElements(file) {
            it.node?.elementType == FluentTypes.INLINE_TEXT &&
                (it.text == ">" || it.text == "0" || it.text == "-")
        }
        assertEmpty(badInlineTokens.asList())

        val unreadCountPlaceables = PsiTreeUtil.collectElements(file) {
            it.node?.elementType == FluentTypes.INLINE_PLACEABLE && it.text.trim() == "{\$unreadCount}"
        }
        assertEmpty(unreadCountPlaceables.asList())

        val unreadCountVariables = PsiTreeUtil.collectElements(file) {
            it.node?.elementType == FluentTypes.VARIABLE_ID && it.text == "\$unreadCount"
        }
        assertEquals(1, unreadCountVariables.size)
    }

    fun testSpacedNestedFunctionsAndVariablesAreNotInlineText() {
        val file = myFixture.configureByText(
            "spaced-nested.ftl",
            """
            nested =
              { if {${'$'}count} > 0 ->
                 [ true ] You have { plural (${ '$' }count, one: "one item", other: "{${'$'}count} items")}
                * [ false ] You have no items
              }.

            complex-nested =
              Hello, {${'$'}userName}! You have { if {${'$'}unreadCount} > 0 ->
                 [ true ] { plural (${ '$' }unreadCount, one: "one unread message", other: "{${'$'}unreadCount} unread messages")}
                * [ false ] no unread messages
              } in { NUMBER (${ '$' }folderCount, one: "one folder", other: "{${'$'}folderCount} folders")}.

            nested-functions =
              The total is { NUMBER ({ ADD (${ '$' }price, ${ '$' }tax)}, currency: "USD")}.
            """.replace("${ '$' }", "$").trimIndent()
        )
        assertInstanceOf(file, FluentFileNode::class.java)

        val error = PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java)
        assertNull("Unexpected parse error: ${error?.errorDescription}", error)

        val functionIds = PsiTreeUtil.collectElements(file) {
            it.node?.elementType == FluentTypes.FUNCTION_ID
        }.map { it.text }
        assertTrue(functionIds.contains("plural"))
        assertTrue(functionIds.contains("NUMBER"))
        assertTrue(functionIds.contains("ADD"))

        val badInlineTokens = PsiTreeUtil.collectElements(file) {
            it.node?.elementType == FluentTypes.INLINE_TEXT &&
                (it.text == "plural" || it.text == "NUMBER" || it.text == "ADD" ||
                    it.text == "\$count" || it.text == "\$unreadCount" || it.text == "\$folderCount")
        }
        assertEmpty(badInlineTokens.asList())
    }

    private fun doParseTest(fileName: String) {
        val file = myFixture.configureByFile("parser/$fileName")
        assertInstanceOf(file, FluentFileNode::class.java)

        val error = PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java)
        assertNull("Unexpected parse error: ${error?.errorDescription}", error)

        val actual = StringUtil.convertLineSeparators(DebugUtil.psiToString(file, false, true)).trimEnd()
        val expectedFile = resolveExpectedFile(fileName)
        if (System.getProperty("regenerate") == "true") {
            FileUtil.writeToFile(expectedFile, actual)
        }
        val expected = StringUtil.convertLineSeparators(FileUtil.loadFile(expectedFile)).trimEnd()
        assertEquals(expected, actual)
    }

    private fun resolveExpectedFile(fileName: String): File {
        val parserDir = File(testDataPath, "parser")
        val expectedName = "${fileName.removeSuffix(".ftl")}.txt"
        return parserDir.listFiles()
            ?.firstOrNull { it.isFile && it.name.equals(expectedName, ignoreCase = true) }
            ?: File(parserDir, expectedName)
    }
}
