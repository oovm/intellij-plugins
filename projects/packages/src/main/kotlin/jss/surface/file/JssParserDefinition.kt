package jss.surface.file

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import jss.surface.file.JssFileNode
import jss.surface.lexer.JssLexer
import jss.surface.parser.JssParser
import jss.surface.psi.JssTypes

class JssParserDefinition : ParserDefinition {
    override fun createLexer(project: Project): Lexer = JssLexer()

    override fun createParser(project: Project): PsiParser = JssParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = STRING_LITERALS

    override fun createElement(node: ASTNode): PsiElement = JssTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = JssFileNode(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements =
        ParserDefinition.SpaceRequirements.MAY

    companion object {
        val COMMENTS = TokenSet.create(
            JssTypes.COMMENT,
            JssTypes.COMMENT_BLOCK,
            JssTypes.COMMENT_DOCUMENT,
        )
        val STRING_LITERALS = TokenSet.create(JssTypes.STRING)
        val FILE = IFileElementType(JssLanguage)

        /** Compatibility alias used by todo / brace helpers. */
        val commentTokens: TokenSet get() = COMMENTS
    }
}
