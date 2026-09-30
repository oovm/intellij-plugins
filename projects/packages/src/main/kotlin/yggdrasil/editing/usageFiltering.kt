package yggdrasil.editing.navigation

import com.intellij.openapi.project.Project
import com.intellij.usages.rules.UsageFilteringRule
import com.intellij.usages.rules.UsageFilteringRuleProvider

class usageFiltering : UsageFilteringRuleProvider {
    override fun getApplicableRules(project: Project): MutableCollection<out UsageFilteringRule> {
        return super.getApplicableRules(project)
    }
}