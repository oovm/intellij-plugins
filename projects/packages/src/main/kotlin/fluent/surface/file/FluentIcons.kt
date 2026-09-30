package fluent.surface.file

import com.intellij.ide.IconProvider
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.PsiElement
import javax.swing.Icon

class FluentIcons : IconProvider() {
    override fun getIcon(psiElement: PsiElement, flags: Int): Icon? {
        val fileName = psiElement.containingFile.name
        return when {
            fileName.endsWith(".ftl") -> FILE
            else -> null
        }
    }

    companion object {
        /** Resource path used by `plugin.xml` action icons and [IconLoader]. */
        const val FILE_PATH = "/icons/ftl.svg"

        @JvmField
        val FILE: Icon = IconLoader.getIcon(FILE_PATH, FluentIcons::class.java)
    }
}
