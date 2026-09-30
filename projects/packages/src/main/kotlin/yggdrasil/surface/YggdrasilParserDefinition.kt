package yggdrasil.surface.psi

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import yggdrasil.definition.YggdrasilLanguage
import yggdrasil.surface.file.YggdrasilFileNode

class YggdrasilParserDefinition : ParserDefinition {
    override fun createLexer(project: Project) = YggdrasilLexer()
    override fun createParser(project: Project) = YggdrasilParser()
    override fun getFileNodeType(): IFileElementType = IFileElementType(YggdrasilLanguage)
    override fun getCommentTokens(): TokenSet =
        TokenSet.create(YggdrasilTypes.COMMENT_LINE, YggdrasilTypes.COMMENT_BLOCK)

    override fun getStringLiteralElements(): TokenSet = TokenSet.create()
    override fun getWhitespaceTokens(): TokenSet = TokenSet.create(TokenType.WHITE_SPACE)
    override fun createElement(node: ASTNode): PsiElement = YggdrasilTypes.Factory.createElement(node)
    override fun createFile(viewProvider: FileViewProvider): PsiFile = YggdrasilFileNode(viewProvider)
    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements {
        return ParserDefinition.SpaceRequirements.MAY
    }

}

