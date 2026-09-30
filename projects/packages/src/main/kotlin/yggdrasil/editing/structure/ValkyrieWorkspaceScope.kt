package yggdrasil.editing.structure.scope

import com.intellij.openapi.module.Module
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope

object ValkyrieWorkspaceScope : GlobalSearchScope() {
    override fun contains(file: VirtualFile): Boolean {
        return true;
    }

    override fun isSearchInModuleContent(aModule: Module): Boolean {
        return true;
    }

    override fun isSearchInLibraries(): Boolean {
        return true;
    }
}