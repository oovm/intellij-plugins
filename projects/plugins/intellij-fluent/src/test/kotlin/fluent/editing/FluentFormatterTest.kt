package fluent.editing

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
        // Nested select layout is stable when already correctly indented (#5).
        doFormatterTest("indent-formatted", "indent-formatted")
    }

    /** Selection reformat must not pile another indent level on already-aligned lines (#5). */
    fun testSelectionReformatKeepsRelativeIndent() {
        val file = myFixture.configureByFile("formatter/indent-formatted.ftl")
        val before = normalize(file.text)
        val start = before.indexOf("{\$photoCount ->")
        val end = before.indexOf("} to {\$userGender ->") + 1
        assertTrue("fixture range", start >= 0 && end > start)

        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformatText(file, start, end)
        }

        assertEquals(before, normalize(file.text))
    }

    private fun doFormatterTest(source: String, expected: String) {
        val file = myFixture.configureByFile("formatter/$source.ftl")

        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformat(file)
        }

        val expectedText = normalize(
            Files.readString(Path.of(testDataPath, "formatter", "$expected.ftl")),
        )
        assertEquals(expectedText, normalize(file.text))
    }

    private fun normalize(text: String): String =
        text.replace("\r\n", "\n").replace('\r', '\n').trimEnd()
}
