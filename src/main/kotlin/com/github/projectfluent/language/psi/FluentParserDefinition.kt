package com.github.projectfluent.language.psi


import com.github.projectfluent.FluentLanguage
import com.github.projectfluent.language.psi.nodes.FluentFileNode
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


object FluentParserDefinition : ParserDefinition {
    override fun createLexer(project: Project): Lexer = FluentLexer()
    override fun createParser(project: Project): PsiParser = FluentParser()
    override fun getFileNodeType(): IFileElementType = IFileElementType(FluentLanguage)
    override fun getCommentTokens(): TokenSet = TokenSet.create(FluentTypes.COMMENT_LINE)
    override fun getStringLiteralElements(): TokenSet = TokenSet.create(
        FluentTypes.STRING_QUOTE,
        FluentTypes.STRING_CHAR,
        FluentTypes.STRING_ESCAPE
    )
    override fun getWhitespaceTokens(): TokenSet = TokenSet.EMPTY
    override fun createElement(node: ASTNode): PsiElement = FluentFactory.createElement(node)
    override fun createFile(viewProvider: FileViewProvider): PsiFile = FluentFileNode(viewProvider)
    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): SpaceRequirements {
        return SpaceRequirements.MAY
    }
}
