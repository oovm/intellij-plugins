package yggdrasil.integration.lsp

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspClient
import com.intellij.platform.lsp.api.LspIntegrationProvider
import com.intellij.platform.lsp.api.lsWidget.LspClientWidgetItem
import yggdrasil.surface.file.YggdrasilFileType
import yggdrasil.surface.file.YggdrasilIconProvider

class YggdrasilLanguageProtocol : LspIntegrationProvider {
    override fun fileOpened(
        project: Project,
        file: VirtualFile,
        clientStarter: LspIntegrationProvider.LspClientStarter,
    ) {
        if (file.fileType is YggdrasilFileType) {
            clientStarter.ensureClientStarted(YggdrasilLanguageServerDescriptor(project))
        }
    }

    override fun createWidgetItem(lspClient: LspClient, currentFile: VirtualFile?): LspClientWidgetItem? =
        LspClientWidgetItem(
            lspClient,
            currentFile,
            YggdrasilIconProvider.Instance.Yggdrasil,
        )
}
