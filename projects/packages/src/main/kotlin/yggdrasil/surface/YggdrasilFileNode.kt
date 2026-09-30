package yggdrasil.surface.file

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.model.psi.PsiSymbolDeclaration
import com.intellij.model.psi.PsiSymbolReference
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.SearchScope


/**
ValkyrieFile 是个 PsiElement
 */
@Suppress("UnstableApiUsage")
class YggdrasilFileNode(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, yggdrasil.definition.YggdrasilLanguage) {
    override fun getFileType(): FileType = YggdrasilFileType

    override fun toString(): String = yggdrasil.definition.YggdrasilBundle.message("action.create_file")

    val definitions = YggdrasilFileCache(this)


    override fun getOwnDeclarations(): MutableCollection<out PsiSymbolDeclaration> {
//        val output = mutableListOf<PsiSymbolDeclaration>()
//        for (child in this.children) {
//            output.addAll(child.ownDeclarations)
//        }
//        return output
        return mutableListOf()
    }

    override fun getOwnReferences(): MutableCollection<out PsiSymbolReference> {
//        val output = mutableListOf<PsiSymbolReference>()
//        for (child in this.children) {
//            output.addAll(child.ownReferences)
//        }
//        return output
        return mutableListOf()
    }

    override fun getResolveScope(): GlobalSearchScope {
        return super.getResolveScope()
    }

    override fun getUseScope(): SearchScope {
        return super.getUseScope()
    }

    fun isIndexFile(): Boolean {
        return this.name == "index.ygg"
    }
}

