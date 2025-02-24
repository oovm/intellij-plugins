package vos.surface.file

import com.intellij.lang.Language

object VosLanguage : Language("VOS") {
    override fun getDisplayName(): String {
        return super.getDisplayName()
    }
    override fun getMimeTypes(): Array<String> = arrayOf("text/x-vos")
}


