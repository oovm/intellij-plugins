@file:Suppress("ConvertSecondaryConstructorToPrimary", "MemberVisibilityCanBePrivate", "UnstableApiUsage")

package yggdrasil.semantic.symbol


import com.intellij.psi.PsiElement
import yggdrasil.editing.documentation.DocumentationRenderer
import yggdrasil.editing.highlight.HighlightColor


class ModifierData(
    val namespace: Array<String>,
    val name: String,
    val detail: String = "",
) {
    fun documentation(doc: DocumentationRenderer) {
        doc.append(HighlightColor.KEYWORD, "modifier ")
        for (item in namespace) {
            doc.append(item)
            doc.append("::")
        }
        doc.append(HighlightColor.SYM_MACRO, name)
        doc.append("<hr/>")
        doc.append(detail)
    }

    companion object {
        fun builtinData(name: PsiElement): ModifierData? {
//            if (name.elementType != ValkyrieTypes.KW_MODIFIER) {
//                return null;
//            }
            return builtinData.firstOrNull { it.name == name.text }
        }

        val builtinData: Array<ModifierData> = arrayOf(
            ModifierData(
                arrayOf("std", "modifiers"),
                "get",
                """
                def get field(self) {}
                """.trimIndent()
            ),
            ModifierData(
                arrayOf("std", "modifiers"),
                "set",
                """
                def set field(mut self) {}
                """.trimIndent()
            ),
            ModifierData(
                arrayOf("std", "modifiers"),
                "mut",
                """
                let mut field = 0
                """.trimIndent()
            ),
        )
    }
}