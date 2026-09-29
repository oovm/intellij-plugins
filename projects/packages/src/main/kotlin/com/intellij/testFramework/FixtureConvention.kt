package com.intellij.testFramework

import java.nio.file.Files
import java.nio.file.Path

/**
 * Unified fixture naming for Marketplace plugin tests in this monorepo.
 *
 * Preferred layout under a suite directory:
 * ```
 * Class.code.ftl
 * Class.expects.txt
 * ```
 *
 * Legacy IntelliJ layout is still accepted when the preferred files are missing:
 * ```
 * Class.ftl
 * Class.txt
 * ```
 */
object FixtureConvention {
    const val CODE_SEGMENT = "code"
    const val EXPECTS_SEGMENT = "expects"
    const val EXPECTS_EXTENSION = "txt"

    fun codeFileName(testName: String, languageExtension: String): String =
        "$testName.$CODE_SEGMENT.$languageExtension"

    fun expectsFileName(testName: String): String =
        "$testName.$EXPECTS_SEGMENT.$EXPECTS_EXTENSION"

    fun legacyCodeFileName(testName: String, languageExtension: String): String =
        "$testName.$languageExtension"

    fun legacyExpectsFileName(testName: String): String =
        "$testName.$EXPECTS_EXTENSION"

    data class Pair(
        val code: Path,
        val expects: Path,
        val preferred: Boolean,
    )

    /**
     * Resolve code + expects paths for [testName] under [suiteDir].
     * Prefers `*.code.*` / `*.expects.txt`, then falls back to legacy `*.ext` / `*.txt`.
     */
    fun resolve(suiteDir: Path, testName: String, languageExtension: String): Pair {
        val preferredCode = suiteDir.resolve(codeFileName(testName, languageExtension))
        val preferredExpects = suiteDir.resolve(expectsFileName(testName))
        if (Files.isRegularFile(preferredCode) || Files.isRegularFile(preferredExpects)) {
            return Pair(preferredCode, preferredExpects, preferred = true)
        }

        val legacyCode = suiteDir.resolve(legacyCodeFileName(testName, languageExtension))
        val legacyExpects = suiteDir.resolve(legacyExpectsFileName(testName))
        return Pair(legacyCode, legacyExpects, preferred = false)
    }

    fun readRequired(path: Path): String {
        check(Files.isRegularFile(path)) {
            "Missing fixture file: ${path.toAbsolutePath().normalize()}"
        }
        return Files.readString(path)
            .removePrefix("\uFEFF")
            .replace("\r\n", "\n")
            .replace('\r', '\n')
            .trimEnd()
    }
}
