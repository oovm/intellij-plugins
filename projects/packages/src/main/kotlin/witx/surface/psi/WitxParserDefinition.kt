package witx.surface.psi

import witx.definition.WitxLanguage
import witx.surface.psi.nodes.WitxFileNode
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

object WitxParserDefinition : ParserDefinition {
    private val fileElementType = IFileElementType(WitxLanguage)

    override fun createLexer(project: Project): Lexer = WitxLexer()

    override fun createParser(project: Project): PsiParser = WitxParser()

    override fun getFileNodeType(): IFileElementType = fileElementType

    override fun getCommentTokens(): TokenSet =
        TokenSet.create(WitxTypes.LINE_COMMENT, WitxTypes.BLOCK_COMMENT)

    override fun getStringLiteralElements(): TokenSet = TokenSet.create(WitxTypes.STRING_LITERAL)

    override fun getWhitespaceTokens(): TokenSet = TokenSet.create(TokenType.WHITE_SPACE)

    override fun createElement(node: ASTNode): PsiElement = WitxFactory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = WitxFileNode(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): SpaceRequirements =
        SpaceRequirements.MAY
}
