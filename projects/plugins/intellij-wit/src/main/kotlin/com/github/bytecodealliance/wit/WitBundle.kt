package com.github.bytecodealliance.wit

import com.github.bytecodealliance.wit.WitLanguage.Bundle
import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey
import java.util.function.Supplier

object WitBundle : DynamicBundle(Bundle) {
    @Suppress("SpreadOperator")
    @JvmStatic
    fun message(@PropertyKey(resourceBundle = Bundle) key: String, vararg params: Any): String =
        getMessage(key, *params)

    @Suppress("SpreadOperator", "unused")
    @JvmStatic
    fun messagePointer(@PropertyKey(resourceBundle = Bundle) key: String, vararg params: Any): Supplier<String> =
        getLazyMessage(key, *params)
}
