package vos.editing.todo

import com.intellij.lexer.Lexer
import com.intellij.psi.impl.cache.impl.BaseFilterLexer
import com.intellij.psi.impl.cache.impl.OccurrenceConsumer
import com.intellij.psi.impl.cache.impl.todo.LexerBasedTodoIndexer
import com.intellij.psi.search.UsageSearchContext
import vos.surface.file.VosParserDefinition
import vos.surface.lexer.VosLexer

class TodoIndexer : LexerBasedTodoIndexer() {
    override fun createLexer(consumer: OccurrenceConsumer): Lexer {
        return object : BaseFilterLexer(VosLexer(), consumer) {
            override fun advance() {
                if (myDelegate.tokenType in VosParserDefinition.commentTokens) {
                    scanWordsInToken(UsageSearchContext.IN_COMMENTS.toInt(), false, false)
                    advanceTodoItemCountsInToken()
                }
                myDelegate.advance()
            }
        }
    }
}
