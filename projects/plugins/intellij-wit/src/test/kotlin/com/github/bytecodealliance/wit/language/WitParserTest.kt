package com.github.bytecodealliance.wit.language

import com.github.bytecodealliance.wit.language.psi.nodes.WitFileNode
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.impl.DebugUtil
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.io.File

class WitParserTest : BasePlatformTestCase() {
    override fun getTestDataPath(): String = "src/test/testData"

    fun testBasic() = doParseTest("Basic.wit")

    fun testExperimentalSyntax() = doParseTest("Experimental.wit")

    fun testPackageOnly() {
        val file = myFixture.configureByText(
            "package-only.wit",
            "package docs:example;",
        )
        assertInstanceOf(file, WitFileNode::class.java)
        val error = PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java)
        assertNull("Unexpected parse error: ${error?.errorDescription}", error)
    }

    private fun doParseTest(fileName: String) {
        val file = myFixture.configureByFile("parser/$fileName")
        assertInstanceOf(file, WitFileNode::class.java)

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
        val expectedName = "${fileName.removeSuffix(".wit")}.txt"
        return parserDir.listFiles()
            ?.firstOrNull { it.isFile && it.name.equals(expectedName, ignoreCase = true) }
            ?: File(parserDir, expectedName)
    }
}
