package jss.surface.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import jss.surface.psi.JssTypes

class JssParser : PsiParser {
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
            consume(builder, JssTypes.SEMICOLON) ||
            consume(builder, JssTypes.COMMA)

    private fun parseSchemaStatement(builder: PsiBuilder): Boolean {
        if (builder.tokenType != JssTypes.SYMBOL) {
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
            schemaKw.done(JssTypes.SCHEMA)
        } else {
            builder.advanceLexer()
        }
        if (!parseIdentifier(builder)) {
            marker.error("Expected identifier")
            marker.done(JssTypes.SCHEMA_STATEMENT)
            return true
        }
        if (consume(builder, JssTypes.COLON)) {
            parseTypeExpression(builder)
        }
        if (builder.tokenType == JssTypes.BRACE_L) {
            if (!parseBraceBlock(builder, JssTypes.BRACE_BLOCK) { parseKvPair(it) || parseIgnore(it) }) {
                marker.error("Expected schema block")
            }
        } else {
            consume(builder, JssTypes.SEMICOLON)
        }
        marker.done(JssTypes.SCHEMA_STATEMENT)
        return true
    }

    private fun parseNamespaceStatement(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, JssTypes.KW_NAMESPACE)) {
            marker.drop()
            return false
        }
        if (!parseNamespace(builder)) {
            marker.error("Expected namespace")
        }
        marker.done(JssTypes.NAMESPACE_STATEMENT)
        return true
    }

    private fun parseLetStatement(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, JssTypes.KW_LET)) {
            marker.drop()
            return false
        }
        if (!parseIdentifier(builder)) {
            marker.error("Expected identifier")
            marker.done(JssTypes.LET_STATEMENT)
            return true
        }
        parseTypeExpression(builder)
        consume(builder, JssTypes.EQ)
        parseValue(builder)
        marker.done(JssTypes.LET_STATEMENT)
        return true
    }

    private fun parseClassStatement(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, JssTypes.KW_CLASS)) {
            marker.drop()
            return false
        }
        parseModifiers(builder)
        if (!parseIdentifier(builder)) {
            marker.error("Expected identifier")
            marker.done(JssTypes.CLASS_STATEMENT)
            return true
        }
        if (consume(builder, JssTypes.COLON)) {
            parseTypeExpression(builder)
        }
        parseClassBlock(builder)
        marker.done(JssTypes.CLASS_STATEMENT)
        return true
    }

    private fun parseUnionStatement(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, JssTypes.KW_UNION)) {
            marker.drop()
            return false
        }
        parseModifiers(builder)
        if (!parseIdentifier(builder)) {
            marker.error("Expected identifier")
            marker.done(JssTypes.UNION_STATEMENT)
            return true
        }
        if (consume(builder, JssTypes.COLON)) {
            parseTypeExpression(builder)
        }
        parseUnionBlock(builder)
        marker.done(JssTypes.UNION_STATEMENT)
        return true
    }

    private fun parseModifiers(builder: PsiBuilder): Boolean {
        if (builder.tokenType != JssTypes.SYMBOL) {
            return false
        }
        // modifiers ::= (identifier !(':'|'{'))*
        val marker = builder.mark()
        var count = 0
        while (builder.tokenType == JssTypes.SYMBOL) {
            val next = builder.lookAhead(1)
            if (next == JssTypes.COLON || next == JssTypes.BRACE_L || next == null) {
                // last identifier before : or { is the name, not a modifier
                break
            }
            // Heuristic: if next is also SYMBOL, current is a modifier.
            if (next != JssTypes.SYMBOL) {
                break
            }
            parseIdentifier(builder)
            count++
        }
        if (count == 0) {
            marker.drop()
            return false
        }
        marker.done(JssTypes.MODIFIERS)
        return true
    }

    private fun parseClassBlock(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, JssTypes.BRACE_L)) {
            marker.drop()
            return false
        }
        while (!builder.eof() && builder.tokenType != JssTypes.BRACE_R) {
            if (!(parseAnnotation(builder) || parseClassField(builder) || parseClassBound(builder) || parseIgnore(builder))) {
                builder.error("Expected class member")
                builder.advanceLexer()
            }
        }
        if (!consume(builder, JssTypes.BRACE_R)) {
            marker.error("Expected '}'")
        }
        marker.done(JssTypes.CLASS_BLOCK)
        return true
    }

    private fun parseUnionBlock(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, JssTypes.BRACE_L)) {
            marker.drop()
            return false
        }
        while (!builder.eof() && builder.tokenType != JssTypes.BRACE_R) {
            if (!(parseUnionInner(builder) || parseIgnore(builder))) {
                builder.error("Expected union member")
                builder.advanceLexer()
            }
        }
        if (!consume(builder, JssTypes.BRACE_R)) {
            marker.error("Expected '}'")
        }
        marker.done(JssTypes.UNION_BLOCK)
        return true
    }

    private fun parseUnionInner(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        val ok = parseAnnotation(builder) || parseUnionField(builder) || parseClassBound(builder)
        if (!ok) {
            marker.drop()
            return false
        }
        marker.done(JssTypes.UNION_INNER)
        return true
    }

    private fun parseClassField(builder: PsiBuilder): Boolean {
        if (builder.tokenType != JssTypes.SYMBOL) return false
        val marker = builder.mark()
        if (!parseIdentifier(builder)) {
            marker.drop()
            return false
        }
        if (consume(builder, JssTypes.COLON)) {
            parseTypeExpression(builder)
        }
        if (consume(builder, JssTypes.EQ)) {
            parseValue(builder)
        }
        marker.done(JssTypes.CLASS_FIELD)
        return true
    }

    private fun parseUnionField(builder: PsiBuilder): Boolean {
        if (builder.tokenType != JssTypes.SYMBOL) return false
        val marker = builder.mark()
        if (!parseIdentifier(builder)) {
            marker.drop()
            return false
        }
        if (consume(builder, JssTypes.EQ)) {
            parseIntegerSigned(builder)
        }
        if (builder.tokenType == JssTypes.BRACE_L) {
            parseClassBlock(builder)
        }
        marker.done(JssTypes.UNION_FIELD)
        return true
    }

    private fun parseClassBound(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, JssTypes.ACCENT)) {
            marker.drop()
            return false
        }
        if (!parseIdentifier(builder)) {
            marker.error("Expected identifier")
            marker.done(JssTypes.CLASS_BOUND)
            return true
        }
        if (consume(builder, JssTypes.COLON) || consume(builder, JssTypes.EQ)) {
            parseValue(builder)
        }
        marker.done(JssTypes.CLASS_BOUND)
        return true
    }

    private fun parseAnnotation(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, JssTypes.ANNOTATION_MARK)) {
            marker.drop()
            return false
        }
        if (builder.tokenType == JssTypes.BRACKET_L) {
            if (!parseBracketBlock(builder) { parseAnnotationOne(it) || consume(it, JssTypes.COMMA) }) {
                marker.error("Expected annotation list")
            }
        } else if (!parseAnnotationOne(builder)) {
            marker.error("Expected annotation")
        }
        marker.done(JssTypes.ANNOTATION)
        return true
    }

    private fun parseAnnotationOne(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!parseIdentifier(builder)) {
            marker.drop()
            return false
        }
        if (consume(builder, JssTypes.PARENTHESIS_L)) {
            val block = builder.mark()
            consume(builder, JssTypes.PARENTHESIS_R)
            block.done(JssTypes.ANNOTATION_BLOCK)
        }
        marker.done(JssTypes.ANNOTATION_ONE)
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
        marker.done(JssTypes.KV_PAIR)
        return true
    }

    private fun parseKey(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        val ok = parseIdentifier(builder) || parseStringLiteral(builder)
        if (!ok) {
            marker.drop()
            return false
        }
        marker.done(JssTypes.KEY)
        return true
    }

    private fun parseSet(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!(consume(builder, JssTypes.EQ) || consume(builder, JssTypes.COLON))) {
            marker.drop()
            return false
        }
        marker.done(JssTypes.SET)
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
        marker.done(JssTypes.VALUE)
        return true
    }

    private fun parseNull(builder: PsiBuilder): Boolean =
        consume(builder, JssTypes.NULL)

    private fun parseBoolean(builder: PsiBuilder): Boolean =
        consume(builder, JssTypes.BOOLEAN)

    private fun parseUrlMaybeValid(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, JssTypes.URL)) {
            marker.drop()
            return false
        }
        marker.done(JssTypes.URL_MAYBE_VALID)
        return true
    }

    private fun parseArray(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!parseBracketBlock(builder, JssTypes.ARRAY) { parseValue(it) || parseIgnore(it) }) {
            marker.drop()
            return false
        }
        marker.drop()
        return true
    }

    private fun parseObject(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!parseBraceBlock(builder, JssTypes.OBJECT) { parseKvPair(it) || parseIgnore(it) }) {
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
        if (builder.tokenType == JssTypes.BRACKET_L) {
            val numberMarker = builder.mark()
            builder.advanceLexer()
            parseTypeNumber(builder)
            if (!consume(builder, JssTypes.BRACKET_R)) {
                numberMarker.error("Expected ']'")
            } else {
                numberMarker.done(JssTypes.TYPE_NUMBER)
            }
        }
        marker.done(JssTypes.TYPE_EXPRESSION)
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
        marker.done(JssTypes.TYPE_GENERIC_BOUND)
        return true
    }

    private fun parseTypeGenericRange(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!parseNum(builder)) {
            marker.drop()
            return false
        }
        if (!(consume(builder, JssTypes.RANGE_LE) || consume(builder, JssTypes.RANGE_EQ))) {
            marker.rollbackTo()
            return false
        }
        if (!parseNum(builder)) {
            marker.error("Expected number")
        }
        marker.done(JssTypes.TYPE_GENERIC_RANGE)
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
        if (!consume(builder, JssTypes.SYMBOL)) {
            marker.rollbackTo()
            return false
        }
        if (!parseCompare(builder) || !parseNum(builder)) {
            marker.error("Expected compare bound")
        }
        marker.done(JssTypes.TYPE_GENERIC_COMPARE)
        return true
    }

    private fun parseTypeGeneric(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!parseTypeSymbol(builder)) {
            marker.drop()
            return false
        }
        if (consume(builder, JssTypes.COMMA)) {
            parseTypeSymbol(builder)
        }
        marker.done(JssTypes.TYPE_GENERIC)
        return true
    }

    private fun parseCompare(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!(
                consume(builder, JssTypes.ANGLE_L) ||
                    consume(builder, JssTypes.ANGLE_R) ||
                    consume(builder, JssTypes.LEQ) ||
                    consume(builder, JssTypes.GEQ) ||
                    consume(builder, JssTypes.EQ)
                )
        ) {
            marker.drop()
            return false
        }
        marker.done(JssTypes.COMPARE)
        return true
    }

    private fun parseTypeSymbol(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        val ok = when (builder.tokenType) {
            JssTypes.SYMBOL, JssTypes.STRING -> {
                builder.advanceLexer()
                true
            }
            JssTypes.KW_LET -> builder.tokenText == "object" && consume(builder, JssTypes.KW_LET)
            else -> false
        }
        if (!ok) {
            marker.drop()
            return false
        }
        marker.done(JssTypes.TYPE_SYMBOL)
        return true
    }

    private fun parseNum(builder: PsiBuilder): Boolean {
        consume(builder, JssTypes.SIGN)
        return consume(builder, JssTypes.INTEGER) ||
            consume(builder, JssTypes.DECIMAL) ||
            consume(builder, JssTypes.BYTE)
    }

    private fun parseIntegerSigned(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        consume(builder, JssTypes.SIGN)
        if (!consume(builder, JssTypes.INTEGER)) {
            marker.drop()
            return false
        }
        marker.done(JssTypes.INTEGER_SIGNED)
        return true
    }

    private fun parseStringLiteral(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, JssTypes.STRING)) {
            marker.drop()
            return false
        }
        marker.done(JssTypes.STRING_LITERAL)
        return true
    }

    private fun parseNamespace(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!parseIdentifier(builder)) {
            marker.drop()
            return false
        }
        while (consume(builder, JssTypes.DOT)) {
            if (!parseIdentifier(builder)) {
                marker.error("Expected identifier")
                break
            }
        }
        marker.done(JssTypes.NAMESPACE)
        return true
    }

    private fun parseIdentifier(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        if (!consume(builder, JssTypes.SYMBOL)) {
            marker.drop()
            return false
        }
        marker.done(JssTypes.IDENTIFIER)
        return true
    }

    private fun parseIgnore(builder: PsiBuilder): Boolean =
        consume(builder, JssTypes.SEMICOLON) || consume(builder, JssTypes.COMMA)

    private fun parseBraceBlock(
        builder: PsiBuilder,
        type: IElementType,
        item: (PsiBuilder) -> Boolean,
    ): Boolean {
        val marker = builder.mark()
        if (!consume(builder, JssTypes.BRACE_L)) {
            marker.drop()
            return false
        }
        while (!builder.eof() && builder.tokenType != JssTypes.BRACE_R) {
            if (!item(builder)) {
                builder.error("Unexpected token")
                builder.advanceLexer()
            }
        }
        if (!consume(builder, JssTypes.BRACE_R)) {
            marker.error("Expected '}'")
        }
        marker.done(type)
        return true
    }

    private fun parseBracketBlock(
        builder: PsiBuilder,
        type: IElementType = JssTypes.BRACKET_BLOCK,
        item: (PsiBuilder) -> Boolean,
    ): Boolean {
        val marker = builder.mark()
        if (!consume(builder, JssTypes.BRACKET_L)) {
            marker.drop()
            return false
        }
        while (!builder.eof() && builder.tokenType != JssTypes.BRACKET_R) {
            if (!item(builder)) {
                builder.error("Unexpected token")
                builder.advanceLexer()
            }
        }
        if (!consume(builder, JssTypes.BRACKET_R)) {
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
