package yggdrasil.editing.completion

import com.intellij.codeInsight.daemon.ReferenceImporter
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import java.util.function.BooleanSupplier


@Suppress("UnstableApiUsage")
class ValkyrieImportInsert : ReferenceImporter {
    override fun computeAutoImportAtOffset(editor: Editor, file: PsiFile, offset: Int, allowCaretNearReference: Boolean): BooleanSupplier {
        return super.computeAutoImportAtOffset(editor, file, offset, allowCaretNearReference)
    }

    override fun isAddUnambiguousImportsOnTheFlyEnabled(file: PsiFile): Boolean {
        return super.isAddUnambiguousImportsOnTheFlyEnabled(file)
    }
}

