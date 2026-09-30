package yggdrasil.editing.format

import com.intellij.lang.Commenter


class YggdrasilCommenter : Commenter {
    override fun getLineCommentPrefix() = "//"
    override fun getBlockCommentPrefix() = "/*"
    override fun getBlockCommentSuffix() = "*/"
    override fun getCommentedBlockCommentPrefix() = null
    override fun getCommentedBlockCommentSuffix() = null
    override fun getLineCommentPrefixes(): MutableList<String> {
        return super.getLineCommentPrefixes()
    }


    override fun blockCommentRequiresFullLineSelection(): Boolean {
        return false
    }
}
