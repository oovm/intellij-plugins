package jss.parser

import com.intellij.psi.PsiErrorElement
import com.intellij.psi.impl.DebugUtil
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.ParsingTestCase
import jss.test.TestTimeout
import jss.surface.file.JssParserDefinition
import java.nio.file.Path

class JssParserSmokeTest : ParsingTestCase("parser", "jss", JssParserDefinition()) {
    override fun getTestDataPath(): String =
        Path.of("src/test/resources").toAbsolutePath().normalize().toString()

    fun testLetHasNoErrors() = assertNoErrors("let x = 1")

    fun testClassHasNoErrors() = assertNoErrors(
        """
        class Foo {
            bar: Int = 1
        }
        """.trimIndent(),
    )

    fun testSchemaHasNoErrors() = assertNoErrors(
        """
        schema Point {
            x: Int
            y: Int
        }
        """.trimIndent(),
    )

    fun testNamespaceHasNoErrors() = assertNoErrors("namespace a.b.c")

    fun testSchemaObjectTypeHasNoErrors() = assertNoErrors(
        """
        schema Product: object {
            required: ["productId"]
        }
        """.trimIndent(),
    )

    fun testPropertyDeclarationHasNoErrors() = assertNoErrors(
        """
        properties productId: integer;
        """.trimIndent(),
    )

    fun testArrayPropertyBlockHasNoErrors() = assertNoErrors(
        """
        properties tags: array {
            minItems: 1
        }
        """.trimIndent(),
    )

    private fun assertNoErrors(text: String) = TestTimeout.run {
        val psiFile = createPsiFile("case.jss", text)
        val errors = PsiTreeUtil.findChildrenOfType(psiFile, PsiErrorElement::class.java).toList()
        if (errors.isNotEmpty()) {
            fail(DebugUtil.psiToString(psiFile, true))
        }
    }
}
