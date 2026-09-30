package yggdrasil.editing.completion

import com.intellij.codeInsight.completion.XmlCharFilter
import com.intellij.codeInsight.editorActions.XmlAutoPopupHandler
import com.intellij.codeInsight.lookup.CharFilter
import com.intellij.codeInsight.lookup.Lookup

class CompletionInterrupter : CharFilter() {
    override fun acceptChar(c: Char, prefixLength: Int, lookup: Lookup): Result? {
        val element = lookup.psiElement ?: return null
        if (element.language != yggdrasil.definition.YggdrasilLanguage) {
            return null
        }
        return if (Character.isJavaIdentifierPart(c)) Result.ADD_TO_PREFIX
        else when (c) {
            '-', ':', '?' -> Result.ADD_TO_PREFIX
            '/' -> {
                if (XmlCharFilter.isWithinTag(lookup)) {
                    if (prefixLength > 0) {
                        return Result.SELECT_ITEM_AND_FINISH_LOOKUP
                    }
                    XmlAutoPopupHandler.autoPopupXmlLookup(lookup.project, lookup.editor)
                    return Result.HIDE_LOOKUP
                }
                Result.ADD_TO_PREFIX
            }

            '>' -> {
                if (prefixLength > 0) {
                    Result.SELECT_ITEM_AND_FINISH_LOOKUP
                } else Result.SELECT_ITEM_AND_FINISH_LOOKUP
            }

            '\'', '\"' -> Result.SELECT_ITEM_AND_FINISH_LOOKUP
            else -> null
        }
    }
}