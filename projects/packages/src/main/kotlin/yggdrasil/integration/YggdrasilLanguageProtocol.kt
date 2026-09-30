package yggdrasil.integration.lsp

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServer
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.platform.lsp.api.LspServerSupportProvider.LspServerStarter
import com.intellij.platform.lsp.api.lsWidget.LspServerWidgetItem
import yggdrasil.surface.file.YggdrasilFileType
import yggdrasil.surface.file.YggdrasilIconProvider

class YggdrasilLanguageProtocol : LspServerSupportProvider {
    override fun fileOpened(project: Project, file: VirtualFile, serverStarter: LspServerStarter) {
        if (file.fileType is YggdrasilFileType) {
            serverStarter.ensureServerStarted(YggdrasilLanguageServerDescriptor(project))
        }
    }

    override fun createLspServerWidgetItem(lspServer: LspServer, currentFile: VirtualFile?): LspServerWidgetItem? {
        return LspServerWidgetItem(
            lspServer,
            currentFile,
            YggdrasilIconProvider.Instance.Yggdrasil,
            null
        )
    }
}

