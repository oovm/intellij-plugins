package jss.editing.assist

import jss.surface.psi.JssTypes
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler

class JssQuoteHandler : SimpleTokenSetQuoteHandler(JssTypes.STRING)
