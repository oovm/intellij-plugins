package wit.language

import com.intellij.psi.PsiErrorElement
import com.intellij.psi.impl.DebugUtil
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.ParsingTestCase
import wit.surface.psi.WitParserDefinition
import wit.surface.psi.nodes.WitFileNode
import java.nio.file.Path

class WitParserTest : ParsingTestCase("parser", "wit", WitParserDefinition) {
    override fun getTestDataPath(): String =
        Path.of("src/test/testData").toAbsolutePath().normalize().toString()

    fun testBasic() = assertNoErrors(readFixture("Basic.wit"))

    fun testExperimentalSyntax() = assertNoErrors(readFixture("Experimental.wit"))

    fun testPackageOnly() = assertNoErrors(
        """
        package docs:example;
        """.trimIndent(),
    )

    private fun readFixture(name: String): String {
        val path = Path.of(getTestDataPath(), "parser", name)
        return path.toFile().readText()
    }

    private fun assertNoErrors(text: String) {
        val psiFile = createPsiFile("case.wit", text)
        assertInstanceOf(psiFile, WitFileNode::class.java)
        val errors = PsiTreeUtil.findChildrenOfType(psiFile, PsiErrorElement::class.java).toList()
        if (errors.isNotEmpty()) {
            fail(DebugUtil.psiToString(psiFile, true))
        }
    }
}
