package com.github.projectfluent.ide

import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
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
        val expectedFile = File(testDataPath, "highlighting/$baseName.txt")
        assertTrue("Missing highlighting baseline: ${expectedFile.path}", expectedFile.exists())
        val expected = StringUtil.convertLineSeparators(FileUtil.loadFile(expectedFile))
        myFixture.configureByText("$baseName.ftl", expected)
        myFixture.checkHighlighting()
    }
}
