package com.github.projectfluent.language.psi

import com.intellij.lang.ASTNode
import com.intellij.lang.LightPsiParser
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

class FluentParser : PsiParser, LightPsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        parseRoot(builder, root)
        return builder.treeBuilt
    }

    override fun parseLight(root: IElementType?, builder: PsiBuilder?) {
        if (root != null && builder != null) {
            parseRoot(builder, root)
        }
    }

    private fun parseRoot(builder: PsiBuilder, root: IElementType) {
        val rootMarker = builder.mark()
        while (!builder.eof()) {
            when (builder.tokenType) {
                FluentTypes.LINE_END,
                FluentTypes.INLINE_BLANK,
                FluentTypes.INDENT,
                FluentTypes.COMMENT_LINE -> consumeLeaf(builder)
                FluentTypes.SYMBOL -> if (isMessageStart(builder)) parseMessage(builder) else consumeTopLevelJunkLine(builder)
                FluentTypes.HYPHEN -> if (isTermStart(builder)) parseTerm(builder) else consumeTopLevelJunkLine(builder)
                else -> consumeTopLevelJunkLine(builder)
            }
        }
        rootMarker.done(root)
    }

    private fun consumeTopLevelJunkLine(builder: PsiBuilder) {
        while (!builder.eof() && builder.tokenType != FluentTypes.LINE_END) {
            builder.advanceLexer()
        }
        if (builder.tokenType == FluentTypes.LINE_END) {
            consumeLeaf(builder)
        }
    }

    private fun consumeLeaf(builder: PsiBuilder) {
        val tokenType = builder.tokenType ?: return
        val marker = builder.mark()
        builder.advanceLexer()
        marker.done(tokenType)
    }

    private fun parseMessage(builder: PsiBuilder) {
        val messageMarker = builder.mark()

        val idMarker = builder.mark()
        builder.advanceLexer()
        idMarker.done(FluentTypes.MESSAGE_ID)

        consumeInlineBlanks(builder)
        parseEq(builder)
        consumeInlineBlanks(builder)
        parsePattern(builder)

        while (startsAttributeAfterLineEnd(builder)) {
            consumeLeaf(builder)
            if (builder.tokenType == FluentTypes.INDENT) {
                consumeLeaf(builder)
            }
            parseAttribute(builder)
        }

        messageMarker.done(FluentTypes.MESSAGE)
    }

    private fun parseTerm(builder: PsiBuilder) {
        val termMarker = builder.mark()

        val idMarker = builder.mark()
        if (builder.tokenType == FluentTypes.HYPHEN) {
            builder.advanceLexer()
        }
        if (builder.tokenType == FluentTypes.SYMBOL) {
            builder.advanceLexer()
        }
        idMarker.done(FluentTypes.TERM_ID)

        consumeInlineBlanks(builder)
        parseEq(builder)
        consumeInlineBlanks(builder)
        parsePattern(builder)

        while (startsAttributeAfterLineEnd(builder)) {
            consumeLeaf(builder)
            if (builder.tokenType == FluentTypes.INDENT) {
                consumeLeaf(builder)
            }
            parseAttribute(builder)
        }

        termMarker.done(FluentTypes.TERM)
    }

    private fun parseAttribute(builder: PsiBuilder): Boolean {
        if (!isAttributeStart(builder)) {
            return false
        }

        val attributeMarker = builder.mark()
        val idMarker = builder.mark()
        builder.advanceLexer()
        builder.advanceLexer()
        idMarker.done(FluentTypes.ATTRIBUTE_ID)

        consumeInlineBlanks(builder)
        parseEq(builder)
        consumeInlineBlanks(builder)
        parsePattern(builder)

        attributeMarker.done(FluentTypes.ATTRIBUTE)
        return true
    }

    private fun parseEq(builder: PsiBuilder) {
        if (builder.tokenType == FluentTypes.EQ) {
            val eqMarker = builder.mark()
            builder.advanceLexer()
            eqMarker.done(FluentTypes.EQ)
        }
    }

    private fun parsePattern(builder: PsiBuilder) {
        val patternMarker = builder.mark()

        pattern@ while (!builder.eof()) {
            when (builder.tokenType) {
                FluentTypes.LINE_END -> {
                    if (!startsPatternContinuation(builder)) {
                        break@pattern
                    }
                    while (builder.tokenType == FluentTypes.LINE_END && startsPatternContinuation(builder)) {
                        consumeLeaf(builder)
                    }
                    if (builder.tokenType == FluentTypes.INDENT) {
                        consumeLeaf(builder)
                    }
                }
                FluentTypes.COMMENT_LINE -> break@pattern
                FluentTypes.BRACE_L -> parseInlinePlaceable(builder)
                else -> {
                    if (builder.tokenType == FluentTypes.DOT && isAttributeStart(builder)) {
                        break@pattern
                    }
                    val inlineTextMarker = builder.mark()
                    builder.advanceLexer()
                    inlineTextMarker.done(FluentTypes.INLINE_TEXT)
                }
            }
        }

        patternMarker.done(FluentTypes.PATTERN)
    }

    private fun parseInlinePlaceable(builder: PsiBuilder) {
        val placeableMarker = builder.mark()
        builder.advanceLexer()

        var openBraces = 1
        while (openBraces > 0 && !builder.eof()) {
            if (isMessageBoundary(builder)) {
                placeableMarker.drop()
                return
            }

            when (builder.tokenType) {
                FluentTypes.LINE_END,
                FluentTypes.INLINE_BLANK,
                FluentTypes.INDENT,
                FluentTypes.COMMENT_LINE -> consumeLeaf(builder)
                FluentTypes.BRACE_L -> parseInlinePlaceable(builder)
                FluentTypes.BRACE_R -> {
                    openBraces--
                    builder.advanceLexer()
                }
                FluentTypes.DOLLAR -> {
                    if (startsSelectExpression(builder)) {
                        parseSelectExpression(builder)
                    } else {
                        parseVariableReference(builder)
                    }
                }
                FluentTypes.SYMBOL -> {
                    if (startsSelectExpression(builder)) {
                        parseSelectExpression(builder)
                    } else if (startsFunctionReference(builder)) {
                        parseFunctionReference(builder)
                    } else {
                        parseMessageReference(builder)
                    }
                }
                FluentTypes.HYPHEN -> {
                    if (startsTermReference(builder)) {
                        parseTermReference(builder)
                    } else {
                        val inlineTextMarker = builder.mark()
                        builder.advanceLexer()
                        inlineTextMarker.done(FluentTypes.INLINE_TEXT)
                    }
                }
                FluentTypes.IF_KEYWORD -> parseSelectExpression(builder)
                else -> {
                    val inlineTextMarker = builder.mark()
                    builder.advanceLexer()
                    inlineTextMarker.done(FluentTypes.INLINE_TEXT)
                }
            }
        }

        if (openBraces == 0) {
            placeableMarker.done(FluentTypes.INLINE_PLACEABLE)
        } else {
            placeableMarker.drop()
        }
    }

    private fun parseExpression(builder: PsiBuilder) {
        val expressionMarker = builder.mark()

        when (builder.tokenType) {
            FluentTypes.BRACE_L -> parseInlinePlaceable(builder)
            FluentTypes.STRING_QUOTE -> parseStringLiteral(builder)
            FluentTypes.HYPHEN -> {
                if (startsTermReference(builder)) {
                    parseTermReference(builder)
                } else {
                    builder.advanceLexer()
                }
            }
            FluentTypes.SYMBOL -> {
                if (startsSelectExpression(builder)) {
                    parseSelectExpression(builder)
                } else if (startsFunctionReference(builder)) {
                    parseFunctionReference(builder)
                } else {
                    parseMessageReference(builder)
                }
            }
            FluentTypes.DOLLAR -> {
                if (startsSelectExpression(builder)) {
                    parseSelectExpression(builder)
                } else {
                    parseVariableReference(builder)
                }
            }
            FluentTypes.IF_KEYWORD -> parseSelectExpression(builder)
            else -> builder.advanceLexer()
        }

        expressionMarker.done(FluentTypes.EXPRESSION)
    }

    private fun parseSelectExpression(builder: PsiBuilder) {
        parseSelectSelector(builder)
        if (isMessageBoundary(builder)) {
            return
        }

        if (isArrowOperator(builder)) {
            builder.advanceLexer()
            consumeSpaceAndComments(builder)
            if (builder.tokenType == FluentTypes.ANGLE_R) {
                builder.advanceLexer()
            }
        }

        while (builder.tokenType != FluentTypes.BRACE_R && !builder.eof() && !isMessageBoundary(builder)) {
            consumeSpaceAndComments(builder)
            if (builder.tokenType == FluentTypes.BRACE_R || isMessageBoundary(builder)) {
                break
            }

            if (startsDefaultVariant(builder)) {
                builder.advanceLexer()
                consumeInlineBlanks(builder)
            }
            if (builder.tokenType == FluentTypes.BRACKET_L) {
                builder.advanceLexer()
                val variantKeyMarker = builder.mark()
                while (builder.tokenType != FluentTypes.BRACKET_R && !builder.eof() && !isMessageBoundary(builder)) {
                    builder.advanceLexer()
                }
                variantKeyMarker.done(FluentTypes.VARIANT_KEY)
                if (builder.tokenType == FluentTypes.BRACKET_R) {
                    builder.advanceLexer()
                }
            }

            while (
                builder.tokenType != FluentTypes.BRACKET_L &&
                !startsDefaultVariant(builder) &&
                builder.tokenType != FluentTypes.BRACE_R &&
                !builder.eof() &&
                !isMessageBoundary(builder)
            ) {
                when (builder.tokenType) {
                    FluentTypes.BRACE_L -> parseInlinePlaceable(builder)
                    FluentTypes.LINE_END,
                    FluentTypes.INLINE_BLANK,
                    FluentTypes.INDENT,
                    FluentTypes.COMMENT_LINE -> consumeLeaf(builder)
                    else -> {
                        val inlineTextMarker = builder.mark()
                        builder.advanceLexer()
                        inlineTextMarker.done(FluentTypes.INLINE_TEXT)
                    }
                }
            }
        }

    }

    private fun parseSelectSelector(builder: PsiBuilder) {
        while (!builder.eof() && !isMessageBoundary(builder) && !isArrowOperator(builder)) {
            when (builder.tokenType) {
                FluentTypes.LINE_END,
                FluentTypes.INLINE_BLANK,
                FluentTypes.INDENT,
                FluentTypes.COMMENT_LINE,
                FluentTypes.INTEGER,
                FluentTypes.DECIMAL,
                FluentTypes.ANGLE_L,
                FluentTypes.ANGLE_R,
                FluentTypes.EQ,
                FluentTypes.COLON,
                FluentTypes.COMMA,
                FluentTypes.STAR,
                FluentTypes.IF_KEYWORD -> consumeLeaf(builder)
                FluentTypes.BRACE_L -> parseBracedSelectorExpression(builder)
                FluentTypes.DOLLAR -> parseVariableReference(builder)
                FluentTypes.HYPHEN -> {
                    if (startsTermReference(builder)) {
                        parseTermReference(builder)
                    } else {
                        consumeLeaf(builder)
                    }
                }
                FluentTypes.SYMBOL -> {
                    if (startsFunctionReference(builder)) {
                        parseFunctionReference(builder)
                    } else {
                        parseMessageReference(builder)
                    }
                }
                else -> consumeLeaf(builder)
            }
        }

        consumeSpaceAndComments(builder)
    }

    private fun parseBracedSelectorExpression(builder: PsiBuilder) {
        if (builder.tokenType != FluentTypes.BRACE_L) {
            return
        }

        consumeLeaf(builder)
        consumeSpaceAndComments(builder)
        if (builder.tokenType != FluentTypes.BRACE_R && !builder.eof() && !isMessageBoundary(builder)) {
            parseExpression(builder)
            consumeSpaceAndComments(builder)
        }
        if (builder.tokenType == FluentTypes.BRACE_R) {
            consumeLeaf(builder)
        }
    }

    private fun parseFunctionReference(builder: PsiBuilder) {
        val functionMarker = builder.mark()

        val idMarker = builder.mark()
        builder.advanceLexer()
        idMarker.done(FluentTypes.FUNCTION_ID)
        consumeInlineBlanks(builder)

        if (builder.tokenType == FluentTypes.PARENTHESIS_L) {
            builder.advanceLexer()
            parseCallArguments(builder)
            if (builder.tokenType == FluentTypes.PARENTHESIS_R) {
                builder.advanceLexer()
            }
        }

        functionMarker.done(FluentTypes.FUNCTION_REFERENCE)
    }

    private fun parseMessageReference(builder: PsiBuilder) {
        val referenceMarker = builder.mark()

        val messageIdMarker = builder.mark()
        builder.advanceLexer()
        messageIdMarker.done(FluentTypes.MESSAGE_ID)

        while (builder.tokenType == FluentTypes.DOT) {
            builder.advanceLexer()
            if (builder.tokenType == FluentTypes.SYMBOL) {
                val attributeMarker = builder.mark()
                builder.advanceLexer()
                attributeMarker.done(FluentTypes.ATTRIBUTE_ID)
            }
        }

        referenceMarker.done(FluentTypes.SYMBOL_REFERENCE)
    }

    private fun parseTermReference(builder: PsiBuilder) {
        val referenceMarker = builder.mark()

        val termIdMarker = builder.mark()
        builder.advanceLexer()
        if (builder.tokenType == FluentTypes.SYMBOL) {
            builder.advanceLexer()
            termIdMarker.done(FluentTypes.TERM_ID)
        } else {
            termIdMarker.drop()
        }

        while (builder.tokenType == FluentTypes.DOT) {
            builder.advanceLexer()
            if (builder.tokenType == FluentTypes.SYMBOL) {
                val attributeMarker = builder.mark()
                builder.advanceLexer()
                attributeMarker.done(FluentTypes.ATTRIBUTE_ID)
            }
        }

        referenceMarker.done(FluentTypes.SYMBOL_REFERENCE)
    }

    private fun parseStringLiteral(builder: PsiBuilder) {
        if (builder.tokenType != FluentTypes.STRING_QUOTE) {
            return
        }

        consumeLeaf(builder)
        while (builder.tokenType != FluentTypes.STRING_QUOTE && !builder.eof()) {
            when (builder.tokenType) {
                FluentTypes.STRING_CHAR,
                FluentTypes.STRING_ESCAPE -> consumeLeaf(builder)
                FluentTypes.BRACE_L -> parseInlinePlaceable(builder)
                else -> break
            }
        }
        if (builder.tokenType == FluentTypes.STRING_QUOTE) {
            consumeLeaf(builder)
        }
    }

    private fun parseVariableReference(builder: PsiBuilder) {
        val variableMarker = builder.mark()

        if (builder.tokenType == FluentTypes.DOLLAR) {
            val idMarker = builder.mark()
            builder.advanceLexer()
            if (builder.tokenType == FluentTypes.SYMBOL) {
                builder.advanceLexer()
                idMarker.done(FluentTypes.VARIABLE_ID)
            } else {
                idMarker.drop()
            }
        }

        variableMarker.done(FluentTypes.VARIABLE_REFERENCE)
    }

    private fun parseCallArguments(builder: PsiBuilder) {
        val argumentsMarker = builder.mark()

        while (builder.tokenType != FluentTypes.PARENTHESIS_R && !builder.eof()) {
            consumeSpaceAndComments(builder)
            if (builder.tokenType == FluentTypes.PARENTHESIS_R || builder.eof()) {
                break
            }
            parseArgument(builder)
            consumeSpaceAndComments(builder)
            if (builder.tokenType == FluentTypes.COMMA) {
                builder.advanceLexer()
            }
        }

        argumentsMarker.done(FluentTypes.CALL_ARGUMENTS)
    }

    private fun parseArgument(builder: PsiBuilder) {
        val argumentMarker = builder.mark()

        if (builder.tokenType == FluentTypes.SYMBOL && builder.lookAhead(1) == FluentTypes.COLON) {
            parseNamedArgument(builder)
        } else {
            parseExpression(builder)
        }

        argumentMarker.done(FluentTypes.ARGUMENT)
    }

    private fun parseNamedArgument(builder: PsiBuilder) {
        val namedArgumentMarker = builder.mark()
        builder.advanceLexer()
        builder.advanceLexer()
        consumeSpaceAndComments(builder)
        parseExpression(builder)
        namedArgumentMarker.done(FluentTypes.NAMED_ARGUMENT)
    }

    private fun consumeInlineBlanks(builder: PsiBuilder) {
        while (builder.tokenType == FluentTypes.INLINE_BLANK) {
            consumeLeaf(builder)
        }
    }

    private fun consumeSpaceAndComments(builder: PsiBuilder) {
        while (isSpaceToken(builder.tokenType) || builder.tokenType == FluentTypes.COMMENT_LINE) {
            consumeLeaf(builder)
        }
    }

    private fun isArrowOperator(builder: PsiBuilder): Boolean {
        if (builder.tokenType != FluentTypes.HYPHEN) {
            return false
        }

        var lookAheadOffset = 1
        while (isSpaceToken(builder.lookAhead(lookAheadOffset)) || builder.lookAhead(lookAheadOffset) == FluentTypes.COMMENT_LINE) {
            lookAheadOffset++
        }
        return builder.lookAhead(lookAheadOffset) == FluentTypes.ANGLE_R
    }

    private fun startsSelectExpression(builder: PsiBuilder): Boolean {
        return when (builder.tokenType) {
            FluentTypes.DOLLAR -> {
                if (builder.lookAhead(1) != FluentTypes.SYMBOL) {
                    false
                } else {
                    var lookAheadOffset = 2
                    while (isSpaceToken(builder.lookAhead(lookAheadOffset)) || builder.lookAhead(lookAheadOffset) == FluentTypes.COMMENT_LINE) {
                        lookAheadOffset++
                    }
                    builder.lookAhead(lookAheadOffset) == FluentTypes.HYPHEN &&
                        builder.lookAhead(lookAheadOffset + 1) == FluentTypes.ANGLE_R
                }
            }
            FluentTypes.SYMBOL -> {
                var lookAheadOffset = 1
                if (builder.lookAhead(lookAheadOffset) == FluentTypes.PARENTHESIS_L) {
                    var depth = 0
                    while (true) {
                        val tokenType = builder.lookAhead(lookAheadOffset) ?: return false
                        if (tokenType == FluentTypes.PARENTHESIS_L) {
                            depth++
                        } else if (tokenType == FluentTypes.PARENTHESIS_R) {
                            depth--
                            if (depth == 0) {
                                lookAheadOffset++
                                break
                            }
                        }
                        lookAheadOffset++
                    }
                }
                while (isSpaceToken(builder.lookAhead(lookAheadOffset)) || builder.lookAhead(lookAheadOffset) == FluentTypes.COMMENT_LINE) {
                    lookAheadOffset++
                }
                builder.lookAhead(lookAheadOffset) == FluentTypes.HYPHEN &&
                    builder.lookAhead(lookAheadOffset + 1) == FluentTypes.ANGLE_R
            }
            FluentTypes.IF_KEYWORD -> true
            else -> false
        }
    }

    private fun isMessageStart(builder: PsiBuilder): Boolean = isMessageStart(builder, 0)

    private fun isMessageStart(builder: PsiBuilder, startOffset: Int): Boolean {
        if (builder.lookAhead(startOffset) != FluentTypes.SYMBOL) {
            return false
        }
        var lookAheadOffset = startOffset + 1
        while (builder.lookAhead(lookAheadOffset) == FluentTypes.INLINE_BLANK) {
            lookAheadOffset++
        }
        return builder.lookAhead(lookAheadOffset) == FluentTypes.EQ
    }

    private fun isTermStart(builder: PsiBuilder): Boolean = isTermStart(builder, 0)

    private fun isTermStart(builder: PsiBuilder, startOffset: Int): Boolean {
        if (builder.lookAhead(startOffset) != FluentTypes.HYPHEN || builder.lookAhead(startOffset + 1) != FluentTypes.SYMBOL) {
            return false
        }
        var eqOffset = startOffset + 2
        while (builder.lookAhead(eqOffset) == FluentTypes.INLINE_BLANK) {
            eqOffset++
        }
        return builder.lookAhead(eqOffset) == FluentTypes.EQ
    }

    private fun isAttributeStart(builder: PsiBuilder): Boolean = isAttributeStart(builder, 0)

    private fun isAttributeStart(builder: PsiBuilder, startOffset: Int): Boolean {
        if (builder.lookAhead(startOffset) != FluentTypes.DOT || builder.lookAhead(startOffset + 1) != FluentTypes.SYMBOL) {
            return false
        }
        var eqOffset = startOffset + 2
        while (builder.lookAhead(eqOffset) == FluentTypes.INLINE_BLANK) {
            eqOffset++
        }
        return builder.lookAhead(eqOffset) == FluentTypes.EQ
    }

    private fun startsDefaultVariant(builder: PsiBuilder): Boolean = startsDefaultVariant(builder, 0)

    private fun startsDefaultVariant(builder: PsiBuilder, startOffset: Int): Boolean {
        if (builder.lookAhead(startOffset) != FluentTypes.STAR) {
            return false
        }
        var lookAheadOffset = startOffset + 1
        while (builder.lookAhead(lookAheadOffset) == FluentTypes.INLINE_BLANK) {
            lookAheadOffset++
        }
        return builder.lookAhead(lookAheadOffset) == FluentTypes.BRACKET_L
    }

    private fun startsFunctionReference(builder: PsiBuilder): Boolean = startsFunctionReference(builder, 0)

    private fun startsFunctionReference(builder: PsiBuilder, startOffset: Int): Boolean {
        if (builder.lookAhead(startOffset) != FluentTypes.SYMBOL) {
            return false
        }
        var lookAheadOffset = startOffset + 1
        while (builder.lookAhead(lookAheadOffset) == FluentTypes.INLINE_BLANK) {
            lookAheadOffset++
        }
        return builder.lookAhead(lookAheadOffset) == FluentTypes.PARENTHESIS_L
    }

    private fun startsTermReference(builder: PsiBuilder): Boolean = startsTermReference(builder, 0)

    private fun startsTermReference(builder: PsiBuilder, startOffset: Int): Boolean {
        return builder.lookAhead(startOffset) == FluentTypes.HYPHEN &&
            builder.lookAhead(startOffset + 1) == FluentTypes.SYMBOL
    }

    private fun startsAttributeAfterLineEnd(builder: PsiBuilder): Boolean {
        if (builder.tokenType != FluentTypes.LINE_END) {
            return false
        }
        var lookAheadOffset = 1
        if (builder.lookAhead(lookAheadOffset) == FluentTypes.INDENT) {
            lookAheadOffset++
        }
        return isAttributeStart(builder, lookAheadOffset)
    }

    private fun startsPatternContinuation(builder: PsiBuilder): Boolean {
        if (builder.tokenType != FluentTypes.LINE_END) {
            return false
        }

        var lookAheadOffset = 1
        while (builder.lookAhead(lookAheadOffset) == FluentTypes.LINE_END) {
            lookAheadOffset++
        }

        if (builder.lookAhead(lookAheadOffset) != FluentTypes.INDENT) {
            return false
        }

        val contentOffset = lookAheadOffset + 1
        val next = builder.lookAhead(contentOffset)
        if (
            next == null ||
            next == FluentTypes.LINE_END ||
            next == FluentTypes.COMMENT_LINE
        ) {
            return false
        }
        return !isAttributeStart(builder, contentOffset)
    }

    private fun isMessageBoundary(builder: PsiBuilder): Boolean = isMessageBoundary(builder, 0)

    private fun isMessageBoundary(builder: PsiBuilder, startOffset: Int): Boolean {
        if (isMessageStart(builder, startOffset) || isTermStart(builder, startOffset)) {
            return true
        }

        var lookAheadOffset = startOffset
        while (builder.lookAhead(lookAheadOffset) == FluentTypes.LINE_END) {
            lookAheadOffset++
            if (builder.lookAhead(lookAheadOffset) == FluentTypes.INDENT) {
                return false
            }
            if (builder.lookAhead(lookAheadOffset) == FluentTypes.COMMENT_LINE) {
                return true
            }
            if (isMessageStart(builder, lookAheadOffset) || isTermStart(builder, lookAheadOffset)) {
                return true
            }
        }

        return false
    }

    private fun isSpaceToken(tokenType: IElementType?): Boolean {
        return tokenType == FluentTypes.LINE_END ||
            tokenType == FluentTypes.INLINE_BLANK ||
            tokenType == FluentTypes.INDENT
    }
}
