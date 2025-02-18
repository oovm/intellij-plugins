package com.github.projectfluent.ide

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.nio.file.Files
import java.nio.file.Path

class FluentFormatterTest : BasePlatformTestCase() {
    override fun getTestDataPath(): String = "src/test/testData"

    fun testFormatter() {
        doFormatterTest("unformatted", "formatted")
    }

    fun testNestedSelectIndentation() {
        doFormatterTest("indent-unformatted", "indent-formatted")
    }

    private fun doFormatterTest(source: String, expected: String) {
        val file = myFixture.configureByFile("formatter/$source.ftl")

        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformat(file)
        }

        val expectedText = Files.readString(Path.of(testDataPath, "formatter", "$expected.ftl"))
        assertEquals(expectedText, file.text)
    }
}
