package com.github.bytecodealliance.wit.language.psi

import com.github.bytecodealliance.wit.WitLanguage
import com.github.bytecodealliance.wit.language.psi.nodes.WitFileNode
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

object WitParserDefinition : ParserDefinition {
    private val fileElementType = IFileElementType(WitLanguage)

    override fun createLexer(project: Project): Lexer = WitLexer()

    override fun createParser(project: Project): PsiParser = WitParser()

    override fun getFileNodeType(): IFileElementType = fileElementType

    override fun getCommentTokens(): TokenSet =
        TokenSet.create(WitTypes.LINE_COMMENT, WitTypes.BLOCK_COMMENT)

    override fun getStringLiteralElements(): TokenSet = TokenSet.create(WitTypes.STRING_LITERAL)

    override fun getWhitespaceTokens(): TokenSet = TokenSet.create(TokenType.WHITE_SPACE)

    override fun createElement(node: ASTNode): PsiElement = WitFactory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = WitFileNode(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): SpaceRequirements =
        SpaceRequirements.MAY
}
