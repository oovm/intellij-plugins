package com.intellij.testFramework

import junit.framework.TestCase as JUnit3TestCase
import java.nio.file.Path

/**
 * Shared JUnit3 base for monorepo plugin tests.
 *
 * - Resolves fixtures via [FixtureConvention] (`*.code.*` / `*.expects.txt`, with legacy fallback)
 * - Wraps work in [TestTimeout] so lexer/parser hangs fail instead of blocking CI
 */
abstract class TestCase : JUnit3TestCase() {
    protected open val fixtureTimeoutMs: Long
        get() = TestTimeout.DEFAULT_TIMEOUT_MS

    protected fun <T> withTimeout(timeoutMs: Long = fixtureTimeoutMs, block: () -> T): T =
        TestTimeout.run(timeoutMs, block)

    protected fun resolveFixtures(
        suiteDir: Path,
        testName: String,
        languageExtension: String,
    ): FixtureConvention.Pair =
        FixtureConvention.resolve(suiteDir, testName, languageExtension)

    protected fun loadCode(suiteDir: Path, testName: String, languageExtension: String): String {
        val pair = resolveFixtures(suiteDir, testName, languageExtension)
        return FixtureConvention.readRequired(pair.code)
    }

    protected fun loadExpects(suiteDir: Path, testName: String, languageExtension: String): String {
        val pair = resolveFixtures(suiteDir, testName, languageExtension)
        return FixtureConvention.readRequired(pair.expects)
    }
}
