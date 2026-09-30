package witx.surface.psi

import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

class WitxParser : PsiParser {
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
            WitxTypes.KW_PACKAGE -> parsePackageDecl(builder)
            WitxTypes.KW_INTERFACE -> parseInterfaceDecl(builder)
            WitxTypes.KW_WORLD -> parseWorldDecl(builder)
            WitxTypes.KW_USE -> parseUseDecl(builder, WitxTypes.USE_DECL)
            WitxTypes.KW_EXTEND -> parseExtendDecl(builder)
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
        if (builder.tokenType == WitxTypes.BRACE_L) {
            builder.advanceLexer()
            while (!builder.eof() && builder.tokenType != WitxTypes.BRACE_R) {
                if (!parseTopLevelItem(builder)) {
                    builder.advanceLexer()
                }
            }
            if (builder.tokenType == WitxTypes.BRACE_R) {
                builder.advanceLexer()
            }
            marker.done(WitxTypes.PACKAGE_DECL)
            return true
        }
        if (builder.tokenType == WitxTypes.SEMICOLON) {
            builder.advanceLexer()
        }
        marker.done(WitxTypes.PACKAGE_DECL)
        return true
    }

    private fun parsePackageName(builder: PsiBuilder): Boolean {
        if (builder.tokenType != WitxTypes.IDENTIFIER) {
            return false
        }
        val marker = builder.mark()
        while (builder.tokenType == WitxTypes.IDENTIFIER || builder.tokenType == WitxTypes.SLASH) {
            builder.advanceLexer()
        }
        marker.done(WitxTypes.PACKAGE_NAME)
        return true
    }

    private fun parseInterfaceDecl(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer() // interface
        if (builder.tokenType != WitxTypes.IDENTIFIER) {
            marker.drop()
            return false
        }
        builder.advanceLexer()
        if (!parseBodyItems(builder)) {
            marker.drop()
            return false
        }
        marker.done(WitxTypes.INTERFACE_DECL)
        return true
    }

    private fun parseWorldDecl(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer() // world
        if (builder.tokenType != WitxTypes.IDENTIFIER) {
            marker.drop()
            return false
        }
        builder.advanceLexer()
        if (!parseBodyItems(builder)) {
            marker.drop()
            return false
        }
        marker.done(WitxTypes.WORLD_DECL)
        return true
    }

    private fun parseBodyItems(builder: PsiBuilder): Boolean {
        if (builder.tokenType != WitxTypes.BRACE_L) {
            return false
        }
        builder.advanceLexer()
        while (!builder.eof() && builder.tokenType != WitxTypes.BRACE_R) {
            if (!parseBodyItem(builder)) {
                builder.advanceLexer()
            }
        }
        if (builder.tokenType == WitxTypes.BRACE_R) {
            builder.advanceLexer()
        }
        return true
    }

    private fun parseBodyItem(builder: PsiBuilder): Boolean {
        skipBlanks(builder)
        if (builder.eof()) {
            return false
        }

        while (builder.tokenType == WitxTypes.AT) {
            parseAttribute(builder)
            skipBlanks(builder)
        }

        return when (builder.tokenType) {
            WitxTypes.KW_USE -> parseUseDecl(builder, WitxTypes.USE_DECL)
            WitxTypes.KW_IMPORT -> parseImportExport(builder, WitxTypes.IMPORT_DECL)
            WitxTypes.KW_EXPORT -> parseImportExport(builder, WitxTypes.EXPORT_DECL)
            WitxTypes.KW_INCLUDE -> parseIncludeDecl(builder)
            WitxTypes.KW_RECORD -> parseNamedBlockDecl(builder, WitxTypes.RECORD_DECL)
            WitxTypes.KW_VARIANT -> parseNamedBlockDecl(builder, WitxTypes.VARIANT_DECL)
            WitxTypes.KW_ENUM -> parseNamedBlockDecl(builder, WitxTypes.ENUM_DECL)
            WitxTypes.KW_FLAGS -> parseNamedBlockDecl(builder, WitxTypes.FLAGS_DECL)
            WitxTypes.KW_RESOURCE -> parseNamedBlockDecl(builder, WitxTypes.RESOURCE_DECL)
            WitxTypes.KW_TYPE -> parseTypeAlias(builder)
            WitxTypes.KW_FUNC, WitxTypes.KW_STATIC -> parseFuncDecl(builder)
            WitxTypes.IDENTIFIER -> parseItemWithColon(builder)
            else -> false
        }
    }

    private fun parseExtendDecl(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer() // extend
        if (builder.tokenType != WitxTypes.IDENTIFIER) {
            marker.drop()
            return false
        }
        builder.advanceLexer()
        while (builder.tokenType == WitxTypes.DOT) {
            builder.advanceLexer()
            if (builder.tokenType == WitxTypes.IDENTIFIER) {
                builder.advanceLexer()
            }
        }
        if (builder.tokenType != WitxTypes.KW_WITH) {
            marker.drop()
            return false
        }
        builder.advanceLexer()
        if (!parseBodyItems(builder)) {
            marker.drop()
            return false
        }
        marker.done(WitxTypes.EXTEND_DECL)
        return true
    }

    private fun parseAttribute(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // @
        if (builder.tokenType == WitxTypes.IDENTIFIER) {
            builder.advanceLexer()
        }
        skipBlanks(builder)
        if (builder.tokenType == WitxTypes.PAREN_L) {
            parseBalancedParens(builder)
        }
        marker.done(WitxTypes.ATTRIBUTE)
    }

    private fun parseUseDecl(builder: PsiBuilder, elementType: IElementType): Boolean {
        val marker = builder.mark()
        builder.advanceLexer()
        parseUntilSemicolonOrBrace(builder)
        if (builder.tokenType == WitxTypes.SEMICOLON) {
            builder.advanceLexer()
        }
        marker.done(elementType)
        return true
    }

    private fun parseImportExport(builder: PsiBuilder, elementType: IElementType): Boolean {
        val marker = builder.mark()
        builder.advanceLexer()
        parseUntilSemicolonOrBrace(builder)
        if (builder.tokenType == WitxTypes.SEMICOLON) {
            builder.advanceLexer()
        }
        marker.done(elementType)
        return true
    }

    private fun parseIncludeDecl(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer()
        parseUntilSemicolonOrBrace(builder)
        if (builder.tokenType == WitxTypes.SEMICOLON) {
            builder.advanceLexer()
        }
        marker.done(WitxTypes.INCLUDE_DECL)
        return true
    }

    private fun parseNamedBlockDecl(builder: PsiBuilder, elementType: IElementType): Boolean {
        val marker = builder.mark()
        builder.advanceLexer()
        if (builder.tokenType == WitxTypes.IDENTIFIER) {
            builder.advanceLexer()
        }
        if (builder.tokenType == WitxTypes.BRACE_L) {
            parseBalancedBraces(builder)
        } else {
            parseUntilSemicolonOrBrace(builder)
            if (builder.tokenType == WitxTypes.SEMICOLON) {
                builder.advanceLexer()
            }
        }
        marker.done(elementType)
        return true
    }

    private fun parseTypeAlias(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer()
        if (builder.tokenType == WitxTypes.IDENTIFIER) {
            builder.advanceLexer()
        }
        parseUntilSemicolonOrBrace(builder)
        if (builder.tokenType == WitxTypes.SEMICOLON) {
            builder.advanceLexer()
        }
        marker.done(WitxTypes.TYPE_DECL)
        return true
    }

    private fun parseFuncDecl(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (builder.tokenType == WitxTypes.KW_STATIC) {
            builder.advanceLexer()
        }
        if (builder.tokenType == WitxTypes.KW_FUNC) {
            builder.advanceLexer()
            if (builder.tokenType == WitxTypes.IDENTIFIER) {
                builder.advanceLexer()
            }
            if (builder.tokenType == WitxTypes.PAREN_L) {
                parseBalancedParens(builder)
            }
            parseUntilSemicolonOrBrace(builder)
            if (builder.tokenType == WitxTypes.SEMICOLON) {
                builder.advanceLexer()
            }
            marker.done(WitxTypes.FUNC_DECL)
            return true
        }
        marker.drop()
        return false
    }

    private fun parseItemWithColon(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer()
        if (builder.tokenType == WitxTypes.COLON) {
            builder.advanceLexer()
            skipBlanks(builder)
            when (builder.tokenType) {
                WitxTypes.KW_FUNC -> {
                    marker.drop()
                    return parseFuncDecl(builder)
                }
                WitxTypes.KW_STATIC -> {
                    marker.drop()
                    return parseFuncDecl(builder)
                }
                else -> {
                    parseUntilSemicolonOrBrace(builder)
                    if (builder.tokenType == WitxTypes.SEMICOLON) {
                        builder.advanceLexer()
                    }
                    marker.done(WitxTypes.ITEM_DECL)
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
                WitxTypes.SEMICOLON, WitxTypes.BRACE_R -> return
                WitxTypes.PAREN_L -> parseBalancedParens(builder)
                WitxTypes.BRACE_L -> parseBalancedBraces(builder)
                WitxTypes.ANGLE_L -> parseBalancedAngles(builder)
                else -> builder.advanceLexer()
            }
        }
    }

    private fun parseBalancedParens(builder: PsiBuilder) {
        if (builder.tokenType != WitxTypes.PAREN_L) {
            return
        }
        var depth = 0
        while (!builder.eof()) {
            when (builder.tokenType) {
                WitxTypes.PAREN_L -> {
                    depth++
                    builder.advanceLexer()
                }
                WitxTypes.PAREN_R -> {
                    depth--
                    builder.advanceLexer()
                    if (depth <= 0) {
                        return
                    }
                }
                WitxTypes.BRACE_L -> parseBalancedBraces(builder)
                WitxTypes.ANGLE_L -> parseBalancedAngles(builder)
                else -> builder.advanceLexer()
            }
        }
    }

    private fun parseBalancedBraces(builder: PsiBuilder) {
        if (builder.tokenType != WitxTypes.BRACE_L) {
            return
        }
        var depth = 0
        while (!builder.eof()) {
            when (builder.tokenType) {
                WitxTypes.BRACE_L -> {
                    depth++
                    builder.advanceLexer()
                }
                WitxTypes.BRACE_R -> {
                    depth--
                    builder.advanceLexer()
                    if (depth <= 0) {
                        return
                    }
                }
                WitxTypes.PAREN_L -> parseBalancedParens(builder)
                WitxTypes.ANGLE_L -> parseBalancedAngles(builder)
                else -> builder.advanceLexer()
            }
        }
    }

    private fun parseBalancedAngles(builder: PsiBuilder) {
        if (builder.tokenType != WitxTypes.ANGLE_L) {
            return
        }
        var depth = 0
        while (!builder.eof()) {
            when (builder.tokenType) {
                WitxTypes.ANGLE_L -> {
                    depth++
                    builder.advanceLexer()
                }
                WitxTypes.ANGLE_R -> {
                    depth--
                    builder.advanceLexer()
                    if (depth <= 0) {
                        return
                    }
                }
                WitxTypes.PAREN_L -> parseBalancedParens(builder)
                WitxTypes.BRACE_L -> parseBalancedBraces(builder)
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
            WitxTypes.LINE_COMMENT,
            WitxTypes.BLOCK_COMMENT,
        )
    }
}
