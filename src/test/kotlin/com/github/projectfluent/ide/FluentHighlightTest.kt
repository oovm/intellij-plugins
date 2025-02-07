package com.github.projectfluent.ide

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class FluentHighlightTest : BasePlatformTestCase() {
    override fun getTestDataPath(): String {
        return "src/test/testData"
    }

    fun testBasicHighlighting() {
        myFixture.configureByFile("highlighting/basic.ftl")
        myFixture.checkHighlighting()
    }

    fun testKeyHighlighting() {
        myFixture.configureByFile("highlighting/test-keys.ftl")
        myFixture.checkHighlighting()
    }

    fun testDotMessage() {
        myFixture.configureByFile("highlighting/dot-message.ftl")
        myFixture.checkHighlighting()
    }

    fun testStringInterpolation() {
        myFixture.configureByFile("highlighting/string-interpolation.ftl")
        myFixture.checkHighlighting()
    }
}
