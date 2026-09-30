package yggdrasil.surface.file

import com.intellij.ide.IconProvider
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.PsiElement
import javax.swing.Icon

class YggdrasilIconProvider : IconProvider() {
    override fun getIcon(psiElement: PsiElement, flags: Int): Icon? {
        val file = psiElement.containingFile ?: return null
        return when {
            file.name.endsWith(".ygg") -> Instance.Yggdrasil
            file.name.endsWith(".yggdrasil") -> Instance.Yggdrasil
            else -> null
        }
    }

    object Instance {
        val Yggdrasil = IconLoader.getIcon("/icons/ygg.svg", YggdrasilIconProvider::class.java)
    }
}
