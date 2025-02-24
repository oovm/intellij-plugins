package vos.editing.assist

import vos.surface.psi.VosTypes
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler

class JssQuoteHandler : SimpleTokenSetQuoteHandler(VosTypes.STRING)
