package yggdrasil.surface.psi

import com.intellij.lang.ASTNode
import com.intellij.lang.LightPsiParser
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

class YggdrasilParser : PsiParser, LightPsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        parseLight(root, builder)
        return builder.treeBuilt
    }

    override fun parseLight(root: IElementType?, builder: PsiBuilder?) {
        if (builder == null) return

        val rootMarker = builder.mark()
        parseRoot(builder)
        rootMarker.done(root ?: YggdrasilTypes.EXPRESSION)
    }

    private fun parseRoot(builder: PsiBuilder) {
        while (!builder.eof()) {
            parseStatement(builder)
        }
    }

    private fun parseStatement(builder: PsiBuilder) {
        when (builder.tokenType) {
            YggdrasilTypes.SEMICOLON -> {
                builder.advanceLexer()
            }
            YggdrasilTypes.KW_GRAMMAR -> {
                parseGrammar(builder)
            }
            YggdrasilTypes.KW_USING -> {
                parseUsing(builder)
            }
            YggdrasilTypes.KW_CLASS -> {
                parseDefineClass(builder)
            }
            YggdrasilTypes.KW_UNION -> {
                parseDefineUnion(builder)
            }
            YggdrasilTypes.KW_GROUP -> {
                parseGroup(builder)
            }
            YggdrasilTypes.KW_MACRO -> {
                parseDefineFunction(builder)
            }
            null -> {
                // Reached end of file, do nothing
            }
            else -> {
                builder.advanceLexer()
            }
        }
    }

    private fun parseAnnotations(builder: PsiBuilder) {
        val annotationsMarker = builder.mark()
        while (parseAttribute(builder) || parseModifier(builder)) {
            // Continue parsing annotations
        }
        annotationsMarker.done(YggdrasilTypes.ANNOTATIONS)
    }

    private fun parseAttribute(builder: PsiBuilder): Boolean {
        if (builder.tokenType == YggdrasilTypes.AT || builder.tokenType == YggdrasilTypes.HASH) {
            val attributeMarker = builder.mark()
            builder.advanceLexer()
            parseIdentifier(builder)
            if (builder.tokenType == YggdrasilTypes.PARENTHESIS_L) {
                parseTuple(builder)
            }
            attributeMarker.done(YggdrasilTypes.ATTRIBUTE)
            return true
        }
        return false
    }

    private fun parseModifier(builder: PsiBuilder): Boolean {
        if (builder.tokenType == YggdrasilTypes.SYMBOL || builder.tokenType == YggdrasilTypes.SYMBOW_RAW) {
            val modifierMarker = builder.mark()
            builder.advanceLexer()
            modifierMarker.done(YggdrasilTypes.MODIFIER)
            return true
        }
        return false
    }

    private fun parseGrammar(builder: PsiBuilder) {
        val grammarMarker = builder.mark()
        parseAnnotations(builder)
        builder.advanceLexer() // consume KW_GRAMMAR
        parseIdentifier(builder)
        if (builder.tokenType == YggdrasilTypes.COLON) {
            parseGrammarInherit(builder)
        }
        parseGrammarBody(builder)
        grammarMarker.done(YggdrasilTypes.GRAMMAR)
    }

    private fun parseGrammarInherit(builder: PsiBuilder) {
        val grammarInheritMarker = builder.mark()
        builder.advanceLexer() // consume COLON
        parseIdentifier(builder)
        grammarInheritMarker.done(YggdrasilTypes.GRAMMAR_INHERIT)
    }

    private fun parseGrammarBody(builder: PsiBuilder) {
        if (builder.tokenType == YggdrasilTypes.BRACE_L) {
            val grammarBodyMarker = builder.mark()
            builder.advanceLexer() // consume BRACE_L
            while (builder.tokenType != YggdrasilTypes.BRACE_R && !builder.eof()) {
                parseGrammarTerm(builder)
            }
            if (builder.tokenType == YggdrasilTypes.BRACE_R) {
                builder.advanceLexer() // consume BRACE_R
            }
            grammarBodyMarker.done(YggdrasilTypes.GRAMMAR_BODY)
        }
    }

    private fun parseGrammarTerm(builder: PsiBuilder) {
        when (builder.tokenType) {
            YggdrasilTypes.COMMA, YggdrasilTypes.SEMICOLON -> {
                builder.advanceLexer()
            }
            else -> {
                parsePair(builder)
            }
        }
    }

    private fun parseUsing(builder: PsiBuilder) {
        val usingMarker = builder.mark()
        parseAnnotations(builder)
        builder.advanceLexer() // consume KW_USING
        parseIdentifier(builder)
        if (builder.tokenType == YggdrasilTypes.DOT) {
            builder.advanceLexer() // consume DOT
            if (builder.tokenType == YggdrasilTypes.OP_MANY) {
                builder.advanceLexer() // consume OP_MANY
            } else {
                parseUsingBody(builder)
            }
        }
        usingMarker.done(YggdrasilTypes.USING)
    }

    private fun parseUsingBody(builder: PsiBuilder) {
        if (builder.tokenType == YggdrasilTypes.BRACE_L) {
            val usingBodyMarker = builder.mark()
            builder.advanceLexer() // consume BRACE_L
            while (builder.tokenType != YggdrasilTypes.BRACE_R && !builder.eof()) {
                parseUsingTerm(builder)
            }
            if (builder.tokenType == YggdrasilTypes.BRACE_R) {
                builder.advanceLexer() // consume BRACE_R
            }
            usingBodyMarker.done(YggdrasilTypes.USING_BODY)
        }
    }

    private fun parseUsingTerm(builder: PsiBuilder) {
        when (builder.tokenType) {
            YggdrasilTypes.SEMICOLON -> {
                builder.advanceLexer()
            }
            else -> {
                parseUsingAlias(builder)
            }
        }
    }

    private fun parseUsingAlias(builder: PsiBuilder) {
        val usingAliasMarker = builder.mark()
        parseKey(builder)
        if (builder.tokenType == YggdrasilTypes.KW_AS) {
            builder.advanceLexer() // consume KW_AS
            parseIdentifier(builder)
        }
        usingAliasMarker.done(YggdrasilTypes.USING_ALIAS)
    }

    private fun parseDefineClass(builder: PsiBuilder) {
        val defineClassMarker = builder.mark()
        parseAnnotations(builder)
        builder.advanceLexer() // consume KW_CLASS
        parseIdentifier(builder)
        if (builder.tokenType == YggdrasilTypes.TO) {
            parseClassCast(builder)
        }
        if (builder.tokenType == YggdrasilTypes.OP_REMARK) {
            builder.advanceLexer() // consume OP_REMARK
        }
        parseClassBody(builder)
        defineClassMarker.done(YggdrasilTypes.DEFINE_CLASS)
    }

    private fun parseClassCast(builder: PsiBuilder) {
        val classCastMarker = builder.mark()
        builder.advanceLexer() // consume TO
        parseIdentifier(builder)
        classCastMarker.done(YggdrasilTypes.CLASS_CAST)
    }

    private fun parseClassBody(builder: PsiBuilder) {
        if (builder.tokenType == YggdrasilTypes.BRACE_L) {
            val classBodyMarker = builder.mark()
            builder.advanceLexer() // consume BRACE_L
            if (builder.tokenType == YggdrasilTypes.OP_OR) {
                builder.advanceLexer() // consume OP_OR
            }
            if (builder.tokenType != YggdrasilTypes.BRACE_R && !builder.eof()) {
                parseExpression(builder)
            }
            if (builder.tokenType == YggdrasilTypes.BRACE_R) {
                builder.advanceLexer() // consume BRACE_R
            }
            classBodyMarker.done(YggdrasilTypes.CLASS_BODY)
        }
    }

    private fun parseDefineUnion(builder: PsiBuilder) {
        val defineUnionMarker = builder.mark()
        parseAnnotations(builder)
        builder.advanceLexer() // consume KW_UNION
        parseIdentifier(builder)
        if (builder.tokenType == YggdrasilTypes.OP_REMARK) {
            builder.advanceLexer() // consume OP_REMARK
        }
        parseUnionBody(builder)
        defineUnionMarker.done(YggdrasilTypes.DEFINE_UNION)
    }

    private fun parseUnionBody(builder: PsiBuilder) {
        if (builder.tokenType == YggdrasilTypes.BRACE_L) {
            val unionBodyMarker = builder.mark()
            builder.advanceLexer() // consume BRACE_L
            while (builder.tokenType != YggdrasilTypes.BRACE_R && !builder.eof()) {
                parseUnionVariant(builder)
            }
            if (builder.tokenType == YggdrasilTypes.BRACE_R) {
                builder.advanceLexer() // consume BRACE_R
            }
            unionBodyMarker.done(YggdrasilTypes.UNION_BODY)
        }
    }

    private fun parseUnionVariant(builder: PsiBuilder) {
        if (builder.tokenType == YggdrasilTypes.OP_OR) {
            val unionVariantMarker = builder.mark()
            builder.advanceLexer() // consume OP_OR
            parseExpressionHard(builder)
            if (builder.tokenType == YggdrasilTypes.HASH) {
                parseTagBranch(builder)
            }
            unionVariantMarker.done(YggdrasilTypes.UNION_VARIANT)
        }
    }

    private fun parseTagBranch(builder: PsiBuilder) {
        val tagBranchMarker = builder.mark()
        builder.advanceLexer() // consume HASH
        parseIdentifier(builder)
        tagBranchMarker.done(YggdrasilTypes.TAG_BRANCH)
    }

    private fun parseGroup(builder: PsiBuilder) {
        val groupMarker = builder.mark()
        parseAnnotations(builder)
        builder.advanceLexer() // consume KW_GROUP
        if (builder.tokenType == YggdrasilTypes.SYMBOL) {
            parseIdentifier(builder)
        }
        parseGroupBody(builder)
        groupMarker.done(YggdrasilTypes.GROUP)
    }

    private fun parseGroupBody(builder: PsiBuilder) {
        if (builder.tokenType == YggdrasilTypes.BRACE_L) {
            val groupBodyMarker = builder.mark()
            builder.advanceLexer() // consume BRACE_L
            while (builder.tokenType != YggdrasilTypes.BRACE_R && !builder.eof()) {
                parseGroupTerm(builder)
            }
            if (builder.tokenType == YggdrasilTypes.BRACE_R) {
                builder.advanceLexer() // consume BRACE_R
            }
            groupBodyMarker.done(YggdrasilTypes.GROUP_BODY)
        }
    }

    private fun parseGroupTerm(builder: PsiBuilder) {
        when (builder.tokenType) {
            YggdrasilTypes.SEMICOLON -> {
                builder.advanceLexer()
            }
            else -> {
                parseGroupItem(builder)
            }
        }
    }

    private fun parseGroupItem(builder: PsiBuilder) {
        val groupItemMarker = builder.mark()
        while (parseAttribute(builder)) {
            // Continue parsing attributes
        }
        while (parseIdentifier(builder)) {
            // Continue parsing identifiers
        }
        if (builder.tokenType == YggdrasilTypes.COLON) {
            builder.advanceLexer() // consume COLON
            parseAtomic(builder)
        }
        groupItemMarker.done(YggdrasilTypes.GROUP_ITEM)
    }

    private fun parseDefineFunction(builder: PsiBuilder) {
        val defineFunctionMarker = builder.mark()
        parseAnnotations(builder)
        builder.advanceLexer() // consume KW_MACRO
        parseIdentifier(builder)
        parseFunctionParameter(builder)
        parseFunctionBlock(builder)
        defineFunctionMarker.done(YggdrasilTypes.DEFINE_FUNCTION)
    }

    private fun parseFunctionParameter(builder: PsiBuilder) {
        if (builder.tokenType == YggdrasilTypes.PARENTHESIS_L) {
            val functionParameterMarker = builder.mark()
            builder.advanceLexer() // consume PARENTHESIS_L
            if (builder.tokenType != YggdrasilTypes.PARENTHESIS_R && !builder.eof()) {
                parseParameter(builder)
                while (builder.tokenType == YggdrasilTypes.COMMA) {
                    builder.advanceLexer() // consume COMMA
                    if (builder.tokenType != YggdrasilTypes.PARENTHESIS_R && !builder.eof()) {
                        parseParameter(builder)
                    }
                }
                if (builder.tokenType == YggdrasilTypes.COMMA) {
                    builder.advanceLexer() // consume trailing COMMA
                }
            }
            if (builder.tokenType == YggdrasilTypes.PARENTHESIS_R) {
                builder.advanceLexer() // consume PARENTHESIS_R
            }
            functionParameterMarker.done(YggdrasilTypes.FUNCTION_PARAMETER)
        }
    }

    private fun parseParameter(builder: PsiBuilder) {
        val parameterMarker = builder.mark()
        parseIdentifier(builder)
        if (builder.tokenType == YggdrasilTypes.COLON) {
            builder.advanceLexer() // consume COLON
            parseExpression(builder)
        }
        parameterMarker.done(YggdrasilTypes.PARAMETER)
    }

    private fun parseFunctionBlock(builder: PsiBuilder) {
        if (builder.tokenType == YggdrasilTypes.BRACE_L) {
            val functionBlockMarker = builder.mark()
            builder.advanceLexer() // consume BRACE_L
            if (builder.tokenType == YggdrasilTypes.OP_OR) {
                builder.advanceLexer() // consume OP_OR
            }
            if (builder.tokenType != YggdrasilTypes.BRACE_R && !builder.eof()) {
                parseExpression(builder)
            }
            if (builder.tokenType == YggdrasilTypes.BRACE_R) {
                builder.advanceLexer() // consume BRACE_R
            }
            functionBlockMarker.done(YggdrasilTypes.FUNCTION_BLOCK)
        }
    }

    private fun parseExpression(builder: PsiBuilder) {
        val expressionMarker = builder.mark()
        parseExpressionChoice(builder)
        expressionMarker.done(YggdrasilTypes.EXPRESSION)
    }

    private fun parseExpressionChoice(builder: PsiBuilder) {
        val expressionChoiceMarker = builder.mark()
        parseExpressionHard(builder)
        while (builder.tokenType == YggdrasilTypes.OP_OR) {
            builder.advanceLexer() // consume OP_OR
            parseExpressionHard(builder)
        }
        expressionChoiceMarker.done(YggdrasilTypes.EXPRESSION_CHOICE)
    }

    private fun parseExpressionHard(builder: PsiBuilder) {
        val expressionHardMarker = builder.mark()
        parseExpressionSoft(builder)
        while (builder.tokenType == YggdrasilTypes.OP_CONCAT) {
            builder.advanceLexer() // consume OP_CONCAT
            parseExpressionSoft(builder)
        }
        expressionHardMarker.done(YggdrasilTypes.EXPRESSION_HARD)
    }

    private fun parseExpressionSoft(builder: PsiBuilder) {
        val expressionSoftMarker = builder.mark()
        parseExpressionTag(builder)
        expressionSoftMarker.done(YggdrasilTypes.EXPRESSION_SOFT)
    }

    private fun parseExpressionTag(builder: PsiBuilder) {
        val expressionTagMarker = builder.mark()
        if (builder.tokenType == YggdrasilTypes.SYMBOL) {
            parseIdentifier(builder)
            if (builder.tokenType == YggdrasilTypes.BIND) {
                builder.advanceLexer() // consume BIND
            }
        }
        // Don't advance lexer here, let parseTerm handle it
        parseTerm(builder)
        expressionTagMarker.done(YggdrasilTypes.EXPRESSION_TAG)
    }

    private fun parseTerm(builder: PsiBuilder) {
        val termMarker = builder.mark()
        while (parsePrefix(builder)) {
            // Continue parsing prefixes
        }
        parseAtomic(builder)
        while (parseSuffix(builder)) {
            // Continue parsing suffixes
        }
        termMarker.done(YggdrasilTypes.TERM)
    }

    private fun parsePrefix(builder: PsiBuilder): Boolean {
        if (builder.tokenType == YggdrasilTypes.OP_NOT || builder.tokenType == YggdrasilTypes.OP_REMARK) {
            val prefixMarker = builder.mark()
            builder.advanceLexer()
            prefixMarker.done(YggdrasilTypes.PREFIX)
            return true
        }
        return false
    }

    private fun parseSuffix(builder: PsiBuilder): Boolean {
        if (builder.tokenType == YggdrasilTypes.OP_OPTIONAL || builder.tokenType == YggdrasilTypes.OP_MANY || builder.tokenType == YggdrasilTypes.OP_MANY1) {
            val suffixMarker = builder.mark()
            builder.advanceLexer()
            suffixMarker.done(YggdrasilTypes.SUFFIX)
            return true
        } else if (builder.tokenType == YggdrasilTypes.BRACE_L) {
            parseRange(builder)
            return true
        }
        return false
    }

    private fun parseAtomic(builder: PsiBuilder) {
        val atomicMarker = builder.mark()
        when (builder.tokenType) {
            YggdrasilTypes.PARENTHESIS_L -> {
                parseExpressionGroup(builder)
            }
            YggdrasilTypes.SYMBOL -> {
                parseIdentifier(builder)
            }
            YggdrasilTypes.ESCAPED -> {
                parseEscape(builder)
            }
            YggdrasilTypes.TEXT_SINGLE, YggdrasilTypes.TEXT_DOUBLE -> {
                parseString(builder)
            }
            YggdrasilTypes.REGULAR_EXPRESSION, YggdrasilTypes.REGULAR_RANGE -> {
                parseRegex(builder)
            }
            YggdrasilTypes.AT -> {
                parseFunctionCall(builder)
            }
            YggdrasilTypes.INTEGER -> {
                parseNumber(builder)
            }
            else -> {
                builder.advanceLexer()
            }
        }
        atomicMarker.done(YggdrasilTypes.ATOMIC)
    }

    private fun parseExpressionGroup(builder: PsiBuilder) {
        val expressionGroupMarker = builder.mark()
        builder.advanceLexer() // consume PARENTHESIS_L
        if (builder.tokenType == YggdrasilTypes.OP_OR) {
            builder.advanceLexer() // consume OP_OR
        }
        parseExpression(builder)
        if (builder.tokenType == YggdrasilTypes.PARENTHESIS_R) {
            builder.advanceLexer() // consume PARENTHESIS_R
        }
        expressionGroupMarker.done(YggdrasilTypes.EXPRESSION_GROUP)
    }

    private fun parseEscape(builder: PsiBuilder) {
        val escapeMarker = builder.mark()
        builder.advanceLexer() // consume ESCAPED
        if (builder.tokenType == YggdrasilTypes.BRACE_L) {
            parseCategory(builder)
        }
        escapeMarker.done(YggdrasilTypes.ESCAPE)
    }

    private fun parseCategory(builder: PsiBuilder) {
        val categoryMarker = builder.mark()
        builder.advanceLexer() // consume BRACE_L
        if (builder.tokenType == YggdrasilTypes.SYMBOL) {
            parseKey(builder)
            if (builder.tokenType == YggdrasilTypes.COLON) {
                builder.advanceLexer() // consume COLON
            }
        }
        parseIdentifier(builder)
        if (builder.tokenType == YggdrasilTypes.BRACE_R) {
            builder.advanceLexer() // consume BRACE_R
        }
        categoryMarker.done(YggdrasilTypes.CATEGORY)
    }

    private fun parseRange(builder: PsiBuilder) {
        val rangeMarker = builder.mark()
        builder.advanceLexer() // consume BRACE_L
        if (builder.tokenType == YggdrasilTypes.INTEGER) {
            parseRangeLower(builder)
        }
        if (builder.tokenType == YggdrasilTypes.COMMA) {
            builder.advanceLexer() // consume COMMA
        }
        if (builder.tokenType == YggdrasilTypes.INTEGER) {
            parseRangeUpper(builder)
        }
        if (builder.tokenType == YggdrasilTypes.BRACE_R) {
            builder.advanceLexer() // consume BRACE_R
        }
        rangeMarker.done(YggdrasilTypes.RANGE)
    }

    private fun parseRangeLower(builder: PsiBuilder) {
        val rangeLowerMarker = builder.mark()
        builder.advanceLexer() // consume INTEGER
        rangeLowerMarker.done(YggdrasilTypes.RANGE_LOWER)
    }

    private fun parseRangeUpper(builder: PsiBuilder) {
        val rangeUpperMarker = builder.mark()
        builder.advanceLexer() // consume INTEGER
        rangeUpperMarker.done(YggdrasilTypes.RANGE_UPPER)
    }

    private fun parseString(builder: PsiBuilder) {
        val stringMarker = builder.mark()
        builder.advanceLexer() // consume TEXT_SINGLE or TEXT_DOUBLE
        stringMarker.done(YggdrasilTypes.STRING)
    }

    private fun parseRegex(builder: PsiBuilder) {
        val regexMarker = builder.mark()
        builder.advanceLexer() // consume REGULAR_EXPRESSION or REGULAR_RANGE
        regexMarker.done(YggdrasilTypes.REGEX)
    }

    private fun parseFunctionCall(builder: PsiBuilder) {
        val functionCallMarker = builder.mark()
        builder.advanceLexer() // consume AT
        parseIdentifier(builder)
        if (builder.tokenType == YggdrasilTypes.PARENTHESIS_L) {
            parseTuple(builder)
        }
        functionCallMarker.done(YggdrasilTypes.FUNCTION_CALL)
    }

    private fun parseNumber(builder: PsiBuilder) {
        val numberMarker = builder.mark()
        builder.advanceLexer() // consume INTEGER
        numberMarker.done(YggdrasilTypes.NUMBER)
    }

    private fun parseIdentifier(builder: PsiBuilder): Boolean {
        if (builder.tokenType == YggdrasilTypes.SYMBOL) {
            val identifierMarker = builder.mark()
            builder.advanceLexer() // consume SYMBOL
            identifierMarker.done(YggdrasilTypes.IDENTIFIER)
            return true
        }
        return false
    }

    private fun parseKey(builder: PsiBuilder) {
        val keyMarker = builder.mark()
        parseIdentifier(builder)
        keyMarker.done(YggdrasilTypes.KEY)
    }

    private fun parseValue(builder: PsiBuilder) {
        val valueMarker = builder.mark()
        if (builder.tokenType == YggdrasilTypes.PARENTHESIS_L) {
            parseTuple(builder)
        } else {
            parseExpressionTag(builder)
        }
        valueMarker.done(YggdrasilTypes.VALUE)
    }

    private fun parsePair(builder: PsiBuilder) {
        val pairMarker = builder.mark()
        parseKey(builder)
        if (builder.tokenType == YggdrasilTypes.COLON) {
            builder.advanceLexer() // consume COLON
            parseValue(builder)
        }
        pairMarker.done(YggdrasilTypes.PAIR)
    }

    private fun parseArgument(builder: PsiBuilder) {
        val argumentMarker = builder.mark()
        if (builder.tokenType == YggdrasilTypes.SYMBOL) {
            parseIdentifier(builder)
            if (builder.tokenType == YggdrasilTypes.COLON) {
                builder.advanceLexer() // consume COLON
            }
        }
        parseValue(builder)
        argumentMarker.done(YggdrasilTypes.ARGUMENT)
    }

    private fun parseTuple(builder: PsiBuilder) {
        if (builder.tokenType == YggdrasilTypes.PARENTHESIS_L) {
            val tupleMarker = builder.mark()
            builder.advanceLexer() // consume PARENTHESIS_L
            if (builder.tokenType != YggdrasilTypes.PARENTHESIS_R && !builder.eof()) {
                parseArgument(builder)
                while (builder.tokenType == YggdrasilTypes.COMMA) {
                    builder.advanceLexer() // consume COMMA
                    if (builder.tokenType != YggdrasilTypes.PARENTHESIS_R && !builder.eof()) {
                        parseArgument(builder)
                    }
                }
                if (builder.tokenType == YggdrasilTypes.COMMA) {
                    builder.advanceLexer() // consume trailing COMMA
                }
            }
            if (builder.tokenType == YggdrasilTypes.PARENTHESIS_R) {
                builder.advanceLexer() // consume PARENTHESIS_R
            }
            tupleMarker.done(YggdrasilTypes.TUPLE)
        }
    }
}