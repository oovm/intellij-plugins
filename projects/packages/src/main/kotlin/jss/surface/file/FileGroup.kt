package jss.surface.file

import com.intellij.ide.projectView.ProjectViewNestingRulesProvider
import com.intellij.ide.projectView.ProjectViewNestingRulesProvider.Consumer
import org.jetbrains.annotations.NotNull

/**
 * Nests jss files created using introspection under their target JSON files.
 */
class FileGroup : ProjectViewNestingRulesProvider {
    override fun addFileNestingRules(@NotNull consumer: Consumer) {
        consumer.addNestingRule(".jss", ".fake.json")
        consumer.addNestingRule(".jss", ".fake.json5")
    }
}