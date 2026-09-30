package yggdrasil.integration.lsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.ProjectWideLspClientDescriptor
import yggdrasil.surface.file.YggdrasilFileType

class YggdrasilLanguageServerDescriptor(project: Project) : ProjectWideLspClientDescriptor(project, "Yggdrasil LSP") {
    override fun isSupportedFile(file: VirtualFile): Boolean = file.fileType is YggdrasilFileType

    override fun createCommandLine(): GeneralCommandLine = GeneralCommandLine("yggdrasil-lsp")
}
