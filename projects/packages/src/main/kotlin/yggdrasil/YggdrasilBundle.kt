package yggdrasil.definition

import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey
import java.util.function.Supplier


private const val Bundle = "messages.YggdrasilBundle"

object YggdrasilBundle : DynamicBundle(yggdrasil.definition.Bundle) {

    @Suppress("SpreadOperator")
    @JvmStatic
    fun message(@PropertyKey(resourceBundle = yggdrasil.definition.Bundle) key: String, vararg params: Any): String {
        return getMessage(key, *params)
    }

    @Suppress("SpreadOperator", "unused")
    @JvmStatic
    fun messagePointer(@PropertyKey(resourceBundle = yggdrasil.definition.Bundle) key: String, vararg params: Any): Supplier<String> {
        return getLazyMessage(key, *params)
    }
}