package wit.surface.psi

import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

class WitParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): com.intellij.lang.ASTNode {
        val marker = builder.mark()
        parseFile(builder)
        marker.done(root)
        return builder.treeBuilt
    }

    private fun parseFile(builder: PsiBuilder) {
        while (!builder.eof()) {
            if (!parseTopLevelItem(builder)) {
                builder.advanceLexer()
            }
        }
    }

    private fun parseTopLevelItem(builder: PsiBuilder): Boolean {
        skipBlanks(builder)
        if (builder.eof()) {
            return false
        }

        return when (builder.tokenType) {
            WitTypes.KW_PACKAGE -> parsePackageDecl(builder)
            WitTypes.KW_INTERFACE -> parseInterfaceDecl(builder)
            WitTypes.KW_WORLD -> parseWorldDecl(builder)
            WitTypes.KW_USE -> parseUseDecl(builder, WitTypes.USE_DECL)
            WitTypes.KW_EXTEND -> parseExtendDecl(builder)
            else -> false
        }
    }

    private fun parsePackageDecl(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer() // package
        if (!parsePackageName(builder)) {
            marker.drop()
            return false
        }
        if (builder.tokenType == WitTypes.BRACE_L) {
            builder.advanceLexer()
            while (!builder.eof() && builder.tokenType != WitTypes.BRACE_R) {
                if (!parseTopLevelItem(builder)) {
                    builder.advanceLexer()
                }
            }
            if (builder.tokenType == WitTypes.BRACE_R) {
                builder.advanceLexer()
            }
            marker.done(WitTypes.PACKAGE_DECL)
            return true
        }
        if (builder.tokenType == WitTypes.SEMICOLON) {
            builder.advanceLexer()
        }
        marker.done(WitTypes.PACKAGE_DECL)
        return true
    }

    private fun parsePackageName(builder: PsiBuilder): Boolean {
        if (builder.tokenType != WitTypes.IDENTIFIER) {
            return false
        }
        val marker = builder.mark()
        while (builder.tokenType == WitTypes.IDENTIFIER || builder.tokenType == WitTypes.SLASH) {
            builder.advanceLexer()
        }
        marker.done(WitTypes.PACKAGE_NAME)
        return true
    }

    private fun parseInterfaceDecl(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer() // interface
        if (builder.tokenType != WitTypes.IDENTIFIER) {
            marker.drop()
            return false
        }
        builder.advanceLexer()
        if (!parseBodyItems(builder)) {
            marker.drop()
            return false
        }
        marker.done(WitTypes.INTERFACE_DECL)
        return true
    }

    private fun parseWorldDecl(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer() // world
        if (builder.tokenType != WitTypes.IDENTIFIER) {
            marker.drop()
            return false
        }
        builder.advanceLexer()
        if (!parseBodyItems(builder)) {
            marker.drop()
            return false
        }
        marker.done(WitTypes.WORLD_DECL)
        return true
    }

    private fun parseBodyItems(builder: PsiBuilder): Boolean {
        if (builder.tokenType != WitTypes.BRACE_L) {
            return false
        }
        builder.advanceLexer()
        while (!builder.eof() && builder.tokenType != WitTypes.BRACE_R) {
            if (!parseBodyItem(builder)) {
                builder.advanceLexer()
            }
        }
        if (builder.tokenType == WitTypes.BRACE_R) {
            builder.advanceLexer()
        }
        return true
    }

    private fun parseBodyItem(builder: PsiBuilder): Boolean {
        skipBlanks(builder)
        if (builder.eof()) {
            return false
        }

        while (builder.tokenType == WitTypes.AT) {
            parseAttribute(builder)
            skipBlanks(builder)
        }

        return when (builder.tokenType) {
            WitTypes.KW_USE -> parseUseDecl(builder, WitTypes.USE_DECL)
            WitTypes.KW_IMPORT -> parseImportExport(builder, WitTypes.IMPORT_DECL)
            WitTypes.KW_EXPORT -> parseImportExport(builder, WitTypes.EXPORT_DECL)
            WitTypes.KW_INCLUDE -> parseIncludeDecl(builder)
            WitTypes.KW_RECORD -> parseNamedBlockDecl(builder, WitTypes.RECORD_DECL)
            WitTypes.KW_VARIANT -> parseNamedBlockDecl(builder, WitTypes.VARIANT_DECL)
            WitTypes.KW_ENUM -> parseNamedBlockDecl(builder, WitTypes.ENUM_DECL)
            WitTypes.KW_FLAGS -> parseNamedBlockDecl(builder, WitTypes.FLAGS_DECL)
            WitTypes.KW_RESOURCE -> parseNamedBlockDecl(builder, WitTypes.RESOURCE_DECL)
            WitTypes.KW_TYPE -> parseTypeAlias(builder)
            WitTypes.KW_FUNC, WitTypes.KW_STATIC -> parseFuncDecl(builder)
            WitTypes.IDENTIFIER -> parseItemWithColon(builder)
            else -> false
        }
    }

    private fun parseExtendDecl(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer() // extend
        if (builder.tokenType != WitTypes.IDENTIFIER) {
            marker.drop()
            return false
        }
        builder.advanceLexer()
        while (builder.tokenType == WitTypes.DOT) {
            builder.advanceLexer()
            if (builder.tokenType == WitTypes.IDENTIFIER) {
                builder.advanceLexer()
            }
        }
        if (builder.tokenType != WitTypes.KW_WITH) {
            marker.drop()
            return false
        }
        builder.advanceLexer()
        if (!parseBodyItems(builder)) {
            marker.drop()
            return false
        }
        marker.done(WitTypes.EXTEND_DECL)
        return true
    }

    private fun parseAttribute(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // @
        if (builder.tokenType == WitTypes.IDENTIFIER) {
            builder.advanceLexer()
        }
        skipBlanks(builder)
        if (builder.tokenType == WitTypes.PAREN_L) {
            parseBalancedParens(builder)
        }
        marker.done(WitTypes.ATTRIBUTE)
    }

    private fun parseUseDecl(builder: PsiBuilder, elementType: IElementType): Boolean {
        val marker = builder.mark()
        builder.advanceLexer()
        parseUntilSemicolonOrBrace(builder)
        if (builder.tokenType == WitTypes.SEMICOLON) {
            builder.advanceLexer()
        }
        marker.done(elementType)
        return true
    }

    private fun parseImportExport(builder: PsiBuilder, elementType: IElementType): Boolean {
        val marker = builder.mark()
        builder.advanceLexer()
        parseUntilSemicolonOrBrace(builder)
        if (builder.tokenType == WitTypes.SEMICOLON) {
            builder.advanceLexer()
        }
        marker.done(elementType)
        return true
    }

    private fun parseIncludeDecl(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer()
        parseUntilSemicolonOrBrace(builder)
        if (builder.tokenType == WitTypes.SEMICOLON) {
            builder.advanceLexer()
        }
        marker.done(WitTypes.INCLUDE_DECL)
        return true
    }

    private fun parseNamedBlockDecl(builder: PsiBuilder, elementType: IElementType): Boolean {
        val marker = builder.mark()
        builder.advanceLexer()
        if (builder.tokenType == WitTypes.IDENTIFIER) {
            builder.advanceLexer()
        }
        if (builder.tokenType == WitTypes.BRACE_L) {
            parseBalancedBraces(builder)
        } else {
            parseUntilSemicolonOrBrace(builder)
            if (builder.tokenType == WitTypes.SEMICOLON) {
                builder.advanceLexer()
            }
        }
        marker.done(elementType)
        return true
    }

    private fun parseTypeAlias(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer()
        if (builder.tokenType == WitTypes.IDENTIFIER) {
            builder.advanceLexer()
        }
        parseUntilSemicolonOrBrace(builder)
        if (builder.tokenType == WitTypes.SEMICOLON) {
            builder.advanceLexer()
        }
        marker.done(WitTypes.TYPE_DECL)
        return true
    }

    private fun parseFuncDecl(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (builder.tokenType == WitTypes.KW_STATIC) {
            builder.advanceLexer()
        }
        if (builder.tokenType == WitTypes.KW_FUNC) {
            builder.advanceLexer()
            if (builder.tokenType == WitTypes.IDENTIFIER) {
                builder.advanceLexer()
            }
            if (builder.tokenType == WitTypes.PAREN_L) {
                parseBalancedParens(builder)
            }
            parseUntilSemicolonOrBrace(builder)
            if (builder.tokenType == WitTypes.SEMICOLON) {
                builder.advanceLexer()
            }
            marker.done(WitTypes.FUNC_DECL)
            return true
        }
        marker.drop()
        return false
    }

    private fun parseItemWithColon(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer()
        if (builder.tokenType == WitTypes.COLON) {
            builder.advanceLexer()
            skipBlanks(builder)
            when (builder.tokenType) {
                WitTypes.KW_FUNC -> {
                    marker.drop()
                    return parseFuncDecl(builder)
                }
                WitTypes.KW_STATIC -> {
                    marker.drop()
                    return parseFuncDecl(builder)
                }
                else -> {
                    parseUntilSemicolonOrBrace(builder)
                    if (builder.tokenType == WitTypes.SEMICOLON) {
                        builder.advanceLexer()
                    }
                    marker.done(WitTypes.ITEM_DECL)
                    return true
                }
            }
        }
        marker.drop()
        return false
    }

    private fun parseUntilSemicolonOrBrace(builder: PsiBuilder) {
        while (!builder.eof()) {
            when (builder.tokenType) {
                WitTypes.SEMICOLON, WitTypes.BRACE_R -> return
                WitTypes.PAREN_L -> parseBalancedParens(builder)
                WitTypes.BRACE_L -> parseBalancedBraces(builder)
                WitTypes.ANGLE_L -> parseBalancedAngles(builder)
                else -> builder.advanceLexer()
            }
        }
    }

    private fun parseBalancedParens(builder: PsiBuilder) {
        if (builder.tokenType != WitTypes.PAREN_L) {
            return
        }
        var depth = 0
        while (!builder.eof()) {
            when (builder.tokenType) {
                WitTypes.PAREN_L -> {
                    depth++
                    builder.advanceLexer()
                }
                WitTypes.PAREN_R -> {
                    depth--
                    builder.advanceLexer()
                    if (depth <= 0) {
                        return
                    }
                }
                WitTypes.BRACE_L -> parseBalancedBraces(builder)
                WitTypes.ANGLE_L -> parseBalancedAngles(builder)
                else -> builder.advanceLexer()
            }
        }
    }

    private fun parseBalancedBraces(builder: PsiBuilder) {
        if (builder.tokenType != WitTypes.BRACE_L) {
            return
        }
        var depth = 0
        while (!builder.eof()) {
            when (builder.tokenType) {
                WitTypes.BRACE_L -> {
                    depth++
                    builder.advanceLexer()
                }
                WitTypes.BRACE_R -> {
                    depth--
                    builder.advanceLexer()
                    if (depth <= 0) {
                        return
                    }
                }
                WitTypes.PAREN_L -> parseBalancedParens(builder)
                WitTypes.ANGLE_L -> parseBalancedAngles(builder)
                else -> builder.advanceLexer()
            }
        }
    }

    private fun parseBalancedAngles(builder: PsiBuilder) {
        if (builder.tokenType != WitTypes.ANGLE_L) {
            return
        }
        var depth = 0
        while (!builder.eof()) {
            when (builder.tokenType) {
                WitTypes.ANGLE_L -> {
                    depth++
                    builder.advanceLexer()
                }
                WitTypes.ANGLE_R -> {
                    depth--
                    builder.advanceLexer()
                    if (depth <= 0) {
                        return
                    }
                }
                WitTypes.PAREN_L -> parseBalancedParens(builder)
                WitTypes.BRACE_L -> parseBalancedBraces(builder)
                else -> builder.advanceLexer()
            }
        }
    }

    private fun skipBlanks(builder: PsiBuilder) {
        while (builder.tokenType in BLANK_TOKENS) {
            builder.advanceLexer()
        }
    }

    companion object {
        private val BLANK_TOKENS: TokenSet = TokenSet.create(
            com.intellij.psi.TokenType.WHITE_SPACE,
            WitTypes.LINE_COMMENT,
            WitTypes.BLOCK_COMMENT,
        )
    }
}
