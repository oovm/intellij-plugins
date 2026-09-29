package com.github.projectfluent.language.file

import com.intellij.ide.projectView.ProjectViewNestingRulesProvider
import com.intellij.ide.projectView.ProjectViewNestingRulesProvider.Consumer
import org.jetbrains.annotations.NotNull

/**
 * Nests jss files created using introspection under their target JSON files.
 */
class FluentFileGroup : ProjectViewNestingRulesProvider {
    override fun addFileNestingRules(@NotNull consumer: Consumer) {
//        consumer.addNestingRule(".jss", ".json")
    }
}