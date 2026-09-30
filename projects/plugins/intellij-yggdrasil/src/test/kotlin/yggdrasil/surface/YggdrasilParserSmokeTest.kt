package yggdrasil.surface

import com.intellij.psi.PsiErrorElement
import com.intellij.psi.impl.DebugUtil
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.ParsingTestCase
import yggdrasil.surface.file.YggdrasilFileNode
import yggdrasil.surface.psi.YggdrasilParserDefinition
import yggdrasil.test.TestTimeout

class YggdrasilParserSmokeTest : ParsingTestCase("parser", "yggdrasil", YggdrasilParserDefinition()) {
    override fun getTestDataPath(): String = "src/test/testData"

    fun testBasicClass() = TestTimeout.run {
        assertNoErrors(
            """
            class TestClass {
                '{' '}'
            }
            """.trimIndent(),
        )
    }

    fun testClassWithExpressionBody() = TestTimeout.run {
        assertNoErrors(
            """
            class TestClassWithMembers {
                '{'
                    TestMember (',' TestMember)* ','?
                '}'
            }
            """.trimIndent(),
        )
    }

    fun testUnionVariant() = TestTimeout.run {
        assertNoErrors(
            """
            union TestUnion {
                | 'a'
                | 'b'
            }
            """.trimIndent(),
        )
    }

    fun testGroupTerm() = TestTimeout.run {
        assertNoErrors(
            """
            group TestGroup {
                item: 'x';
            }
            """.trimIndent(),
        )
    }

    fun testMacroDeclaration() = TestTimeout.run {
        assertNoErrors(
            """
            macro test() {
                | 'ok'
            }
            """.trimIndent(),
        )
    }

    private fun assertNoErrors(text: String) {
        val psiFile = createPsiFile("case.yggdrasil", text)
        assertInstanceOf(psiFile, YggdrasilFileNode::class.java)
        val errors = PsiTreeUtil.findChildrenOfType(psiFile, PsiErrorElement::class.java).toList()
        if (errors.isNotEmpty()) {
            fail(DebugUtil.psiToString(psiFile, true))
        }
    }
}
