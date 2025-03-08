package vos.surface.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import vos.surface.psi.VosTypes

class VosParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()
        while (!builder.eof()) {
            if (!parseStatement(builder)) {
                builder.error("Expected statement")
                builder.advanceLexer()
            }
        }
        rootMarker.done(root)
        return builder.treeBuilt
    }

    private fun parseStatement(builder: PsiBuilder): Boolean =
        parseSchemaStatement(builder) ||
            parseNamespaceStatement(builder) ||
            parseClassStatement(builder) ||
            parseUnionStatement(builder) ||
            parseLetStatement(builder) ||
            parseAnnotation(builder) ||
            consume(builder, VosTypes.SEMICOLON) ||
            consume(builder, VosTypes.COMMA)

    private fun parseSchemaStatement(builder: PsiBuilder): Boolean {
        if (builder.tokenType != VosTypes.SYMBOL) {
            return false
        }
        val keyword = builder.tokenText ?: return false
        if (keyword !in SCHEMA_DECLARATION_KEYWORDS) {
            return false
        }
        val marker = builder.mark()
        if (keyword == "schema") {
            val schemaKw = builder.mark()
            builder.advanceLexer()
            schemaKw.done(VosTypes.SCHEMA)
        } else {
            builder.advanceLexer()
        }
        if (!parseIdentifier(builder)) {
            marker.error("Expected identifier")
            marker.done(VosTypes.SCHEMA_STATEMENT)
            return true
        }
        if (consume(builder, VosTypes.COLON)) {
            parseTypeExpression(builder)
        }
        if (builder.tokenType == VosTypes.BRACE_L) {
            if (!parseBraceBlock(builder, VosTypes.BRACE_BLOCK) { parseKvPair(it) || parseIgnore(it) }) {
                marker.error("Expected schema block")
            }
        } else {
            consume(builder, VosTypes.SEMICOLON)
        }
        marker.done(VosTypes.SCHEMA_STATEMENT)
        return true
    }

    private fun parseNamespaceStatement(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, VosTypes.KW_NAMESPACE)) {
            marker.drop()
            return false
        }
        if (!parseNamespace(builder)) {
            marker.error("Expected namespace")
        }
        marker.done(VosTypes.NAMESPACE_STATEMENT)
        return true
    }

    private fun parseLetStatement(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, VosTypes.KW_LET)) {
            marker.drop()
            return false
        }
        if (!parseIdentifier(builder)) {
            marker.error("Expected identifier")
            marker.done(VosTypes.LET_STATEMENT)
            return true
        }
        parseTypeExpression(builder)
        consume(builder, VosTypes.EQ)
        parseValue(builder)
        marker.done(VosTypes.LET_STATEMENT)
        return true
    }

    private fun parseClassStatement(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, VosTypes.KW_CLASS)) {
            marker.drop()
            return false
        }
        parseModifiers(builder)
        if (!parseIdentifier(builder)) {
            marker.error("Expected identifier")
            marker.done(VosTypes.CLASS_STATEMENT)
            return true
        }
        if (consume(builder, VosTypes.COLON)) {
            parseTypeExpression(builder)
        }
        parseClassBlock(builder)
        marker.done(VosTypes.CLASS_STATEMENT)
        return true
    }

    private fun parseUnionStatement(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, VosTypes.KW_UNION)) {
            marker.drop()
            return false
        }
        parseModifiers(builder)
        if (!parseIdentifier(builder)) {
            marker.error("Expected identifier")
            marker.done(VosTypes.UNION_STATEMENT)
            return true
        }
        if (consume(builder, VosTypes.COLON)) {
            parseTypeExpression(builder)
        }
        parseUnionBlock(builder)
        marker.done(VosTypes.UNION_STATEMENT)
        return true
    }

    private fun parseModifiers(builder: PsiBuilder): Boolean {
        if (builder.tokenType != VosTypes.SYMBOL) {
            return false
        }
        // modifiers ::= (identifier !(':'|'{'))*
        val marker = builder.mark()
        var count = 0
        while (builder.tokenType == VosTypes.SYMBOL) {
            val next = builder.lookAhead(1)
            if (next == VosTypes.COLON || next == VosTypes.BRACE_L || next == null) {
                // last identifier before : or { is the name, not a modifier
                break
            }
            // Heuristic: if next is also SYMBOL, current is a modifier.
            if (next != VosTypes.SYMBOL) {
                break
            }
            parseIdentifier(builder)
            count++
        }
        if (count == 0) {
            marker.drop()
            return false
        }
        marker.done(VosTypes.MODIFIERS)
        return true
    }

    private fun parseClassBlock(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, VosTypes.BRACE_L)) {
            marker.drop()
            return false
        }
        while (!builder.eof() && builder.tokenType != VosTypes.BRACE_R) {
            if (!(parseAnnotation(builder) || parseClassField(builder) || parseClassBound(builder) || parseIgnore(builder))) {
                builder.error("Expected class member")
                builder.advanceLexer()
            }
        }
        if (!consume(builder, VosTypes.BRACE_R)) {
            marker.error("Expected '}'")
        }
        marker.done(VosTypes.CLASS_BLOCK)
        return true
    }

    private fun parseUnionBlock(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, VosTypes.BRACE_L)) {
            marker.drop()
            return false
        }
        while (!builder.eof() && builder.tokenType != VosTypes.BRACE_R) {
            if (!(parseUnionInner(builder) || parseIgnore(builder))) {
                builder.error("Expected union member")
                builder.advanceLexer()
            }
        }
        if (!consume(builder, VosTypes.BRACE_R)) {
            marker.error("Expected '}'")
        }
        marker.done(VosTypes.UNION_BLOCK)
        return true
    }

    private fun parseUnionInner(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        val ok = parseAnnotation(builder) || parseUnionField(builder) || parseClassBound(builder)
        if (!ok) {
            marker.drop()
            return false
        }
        marker.done(VosTypes.UNION_INNER)
        return true
    }

    private fun parseClassField(builder: PsiBuilder): Boolean {
        if (builder.tokenType != VosTypes.SYMBOL) return false
        val marker = builder.mark()
        if (!parseIdentifier(builder)) {
            marker.drop()
            return false
        }
        if (consume(builder, VosTypes.COLON)) {
            parseTypeExpression(builder)
        }
        if (consume(builder, VosTypes.EQ)) {
            parseValue(builder)
        }
        marker.done(VosTypes.CLASS_FIELD)
        return true
    }

    private fun parseUnionField(builder: PsiBuilder): Boolean {
        if (builder.tokenType != VosTypes.SYMBOL) return false
        val marker = builder.mark()
        if (!parseIdentifier(builder)) {
            marker.drop()
            return false
        }
        if (consume(builder, VosTypes.EQ)) {
            parseIntegerSigned(builder)
        }
        if (builder.tokenType == VosTypes.BRACE_L) {
            parseClassBlock(builder)
        }
        marker.done(VosTypes.UNION_FIELD)
        return true
    }

    private fun parseClassBound(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, VosTypes.ACCENT)) {
            marker.drop()
            return false
        }
        if (!parseIdentifier(builder)) {
            marker.error("Expected identifier")
            marker.done(VosTypes.CLASS_BOUND)
            return true
        }
        if (consume(builder, VosTypes.COLON) || consume(builder, VosTypes.EQ)) {
            parseValue(builder)
        }
        marker.done(VosTypes.CLASS_BOUND)
        return true
    }

    private fun parseAnnotation(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, VosTypes.ANNOTATION_MARK)) {
            marker.drop()
            return false
        }
        if (builder.tokenType == VosTypes.BRACKET_L) {
            if (!parseBracketBlock(builder) { parseAnnotationOne(it) || consume(it, VosTypes.COMMA) }) {
                marker.error("Expected annotation list")
            }
        } else if (!parseAnnotationOne(builder)) {
            marker.error("Expected annotation")
        }
        marker.done(VosTypes.ANNOTATION)
        return true
    }

    private fun parseAnnotationOne(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!parseIdentifier(builder)) {
            marker.drop()
            return false
        }
        if (consume(builder, VosTypes.PARENTHESIS_L)) {
            val block = builder.mark()
            consume(builder, VosTypes.PARENTHESIS_R)
            block.done(VosTypes.ANNOTATION_BLOCK)
        }
        marker.done(VosTypes.ANNOTATION_ONE)
        return true
    }

    private fun parseKvPair(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!parseKey(builder)) {
            marker.drop()
            return false
        }
        parseSet(builder)
        if (!parseValue(builder)) {
            marker.error("Expected value")
        }
        marker.done(VosTypes.KV_PAIR)
        return true
    }

    private fun parseKey(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        val ok = parseIdentifier(builder) || parseStringLiteral(builder)
        if (!ok) {
            marker.drop()
            return false
        }
        marker.done(VosTypes.KEY)
        return true
    }

    private fun parseSet(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!(consume(builder, VosTypes.EQ) || consume(builder, VosTypes.COLON))) {
            marker.drop()
            return false
        }
        marker.done(VosTypes.SET)
        return true
    }

    private fun parseValue(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        val ok = parseNull(builder) ||
            parseBoolean(builder) ||
            parseNum(builder) ||
            parseArray(builder) ||
            parseObject(builder) ||
            parseStringLiteral(builder) ||
            parseNamespace(builder) ||
            parseUrlMaybeValid(builder)
        if (!ok) {
            marker.drop()
            return false
        }
        marker.done(VosTypes.VALUE)
        return true
    }

    private fun parseNull(builder: PsiBuilder): Boolean =
        consume(builder, VosTypes.NULL)

    private fun parseBoolean(builder: PsiBuilder): Boolean =
        consume(builder, VosTypes.BOOLEAN)

    private fun parseUrlMaybeValid(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, VosTypes.URL)) {
            marker.drop()
            return false
        }
        marker.done(VosTypes.URL_MAYBE_VALID)
        return true
    }

    private fun parseArray(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!parseBracketBlock(builder, VosTypes.ARRAY) { parseValue(it) || parseIgnore(it) }) {
            marker.drop()
            return false
        }
        marker.drop()
        return true
    }

    private fun parseObject(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!parseBraceBlock(builder, VosTypes.OBJECT) { parseKvPair(it) || parseIgnore(it) }) {
            marker.drop()
            return false
        }
        marker.drop()
        return true
    }

    private fun parseTypeExpression(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!parseTypeSymbol(builder)) {
            marker.drop()
            return false
        }
        if (builder.tokenType == VosTypes.BRACKET_L) {
            val numberMarker = builder.mark()
            builder.advanceLexer()
            parseTypeNumber(builder)
            if (!consume(builder, VosTypes.BRACKET_R)) {
                numberMarker.error("Expected ']'")
            } else {
                numberMarker.done(VosTypes.TYPE_NUMBER)
            }
        }
        marker.done(VosTypes.TYPE_EXPRESSION)
        return true
    }

    private fun parseTypeNumber(builder: PsiBuilder): Boolean =
        parseTypeGenericBound(builder) ||
            parseTypeGenericCompare(builder) ||
            parseTypeGenericRange(builder) ||
            parseTypeGeneric(builder)

    private fun parseTypeGenericBound(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        parseCompare(builder)
        if (!parseNum(builder)) {
            marker.drop()
            return false
        }
        marker.done(VosTypes.TYPE_GENERIC_BOUND)
        return true
    }

    private fun parseTypeGenericRange(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!parseNum(builder)) {
            marker.drop()
            return false
        }
        if (!(consume(builder, VosTypes.RANGE_LE) || consume(builder, VosTypes.RANGE_EQ))) {
            marker.rollbackTo()
            return false
        }
        if (!parseNum(builder)) {
            marker.error("Expected number")
        }
        marker.done(VosTypes.TYPE_GENERIC_RANGE)
        return true
    }

    private fun parseTypeGenericCompare(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!parseNum(builder)) {
            marker.drop()
            return false
        }
        if (!parseCompare(builder)) {
            marker.rollbackTo()
            return false
        }
        if (!consume(builder, VosTypes.SYMBOL)) {
            marker.rollbackTo()
            return false
        }
        if (!parseCompare(builder) || !parseNum(builder)) {
            marker.error("Expected compare bound")
        }
        marker.done(VosTypes.TYPE_GENERIC_COMPARE)
        return true
    }

    private fun parseTypeGeneric(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!parseTypeSymbol(builder)) {
            marker.drop()
            return false
        }
        if (consume(builder, VosTypes.COMMA)) {
            parseTypeSymbol(builder)
        }
        marker.done(VosTypes.TYPE_GENERIC)
        return true
    }

    private fun parseCompare(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!(
                consume(builder, VosTypes.ANGLE_L) ||
                    consume(builder, VosTypes.ANGLE_R) ||
                    consume(builder, VosTypes.LEQ) ||
                    consume(builder, VosTypes.GEQ) ||
                    consume(builder, VosTypes.EQ)
                )
        ) {
            marker.drop()
            return false
        }
        marker.done(VosTypes.COMPARE)
        return true
    }

    private fun parseTypeSymbol(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        val ok = when (builder.tokenType) {
            VosTypes.SYMBOL, VosTypes.STRING -> {
                builder.advanceLexer()
                true
            }
            VosTypes.KW_LET -> builder.tokenText == "object" && consume(builder, VosTypes.KW_LET)
            else -> false
        }
        if (!ok) {
            marker.drop()
            return false
        }
        marker.done(VosTypes.TYPE_SYMBOL)
        return true
    }

    private fun parseNum(builder: PsiBuilder): Boolean {
        consume(builder, VosTypes.SIGN)
        return consume(builder, VosTypes.INTEGER) ||
            consume(builder, VosTypes.DECIMAL) ||
            consume(builder, VosTypes.BYTE)
    }

    private fun parseIntegerSigned(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        consume(builder, VosTypes.SIGN)
        if (!consume(builder, VosTypes.INTEGER)) {
            marker.drop()
            return false
        }
        marker.done(VosTypes.INTEGER_SIGNED)
        return true
    }

    private fun parseStringLiteral(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, VosTypes.STRING)) {
            marker.drop()
            return false
        }
        marker.done(VosTypes.STRING_LITERAL)
        return true
    }

    private fun parseNamespace(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!parseIdentifier(builder)) {
            marker.drop()
            return false
        }
        while (consume(builder, VosTypes.DOT)) {
            if (!parseIdentifier(builder)) {
                marker.error("Expected identifier")
                break
            }
        }
        marker.done(VosTypes.NAMESPACE)
        return true
    }

    private fun parseIdentifier(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, VosTypes.SYMBOL)) {
            marker.drop()
            return false
        }
        marker.done(VosTypes.IDENTIFIER)
        return true
    }

    private fun parseIgnore(builder: PsiBuilder): Boolean =
        consume(builder, VosTypes.SEMICOLON) || consume(builder, VosTypes.COMMA)

    private fun parseBraceBlock(
        builder: PsiBuilder,
        type: IElementType,
        item: (PsiBuilder) -> Boolean,
    ): Boolean {
        val marker = builder.mark()
        if (!consume(builder, VosTypes.BRACE_L)) {
            marker.drop()
            return false
        }
        while (!builder.eof() && builder.tokenType != VosTypes.BRACE_R) {
            if (!item(builder)) {
                builder.error("Unexpected token")
                builder.advanceLexer()
            }
        }
        if (!consume(builder, VosTypes.BRACE_R)) {
            marker.error("Expected '}'")
        }
        marker.done(type)
        return true
    }

    private fun parseBracketBlock(
        builder: PsiBuilder,
        type: IElementType = VosTypes.BRACKET_BLOCK,
        item: (PsiBuilder) -> Boolean,
    ): Boolean {
        val marker = builder.mark()
        if (!consume(builder, VosTypes.BRACKET_L)) {
            marker.drop()
            return false
        }
        while (!builder.eof() && builder.tokenType != VosTypes.BRACKET_R) {
            if (!item(builder)) {
                builder.error("Unexpected token")
                builder.advanceLexer()
            }
        }
        if (!consume(builder, VosTypes.BRACKET_R)) {
            marker.error("Expected ']'")
        }
        marker.done(type)
        return true
    }

    private fun consume(builder: PsiBuilder, type: IElementType): Boolean {
        if (builder.tokenType != type) return false
        builder.advanceLexer()
        return true
    }

    private companion object {
        val SCHEMA_DECLARATION_KEYWORDS = setOf("schema", "property", "properties")
    }
}
