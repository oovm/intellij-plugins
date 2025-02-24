package vos.surface.file

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
import vos.surface.file.VosFileNode
import vos.surface.lexer.VosLexer
import vos.surface.parser.VosParser
import vos.surface.psi.VosTypes

class VosParserDefinition : ParserDefinition {
    override fun createLexer(project: Project): Lexer = VosLexer()

    override fun createParser(project: Project): PsiParser = VosParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = STRING_LITERALS

    override fun createElement(node: ASTNode): PsiElement = VosTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = VosFileNode(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements =
        ParserDefinition.SpaceRequirements.MAY

    companion object {
        val COMMENTS = TokenSet.create(
            VosTypes.COMMENT,
            VosTypes.COMMENT_BLOCK,
            VosTypes.COMMENT_DOCUMENT,
        )
        val STRING_LITERALS = TokenSet.create(VosTypes.STRING)
        val FILE = IFileElementType(VosLanguage)

        /** Compatibility alias used by todo / brace helpers. */
        val commentTokens: TokenSet get() = COMMENTS
    }
}
