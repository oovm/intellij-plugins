#!/usr/bin/env python3
"""One-shot Fluent migration into packages/fluent. Delete after use."""

from __future__ import annotations

import re
import shutil
from pathlib import Path

ROOT = Path(r"E:\victory 胜利女神\intellij-plugins")
SRC_ROOT = ROOT / "projects/plugins/intellij-fluent/src/main/kotlin/com/github/projectfluent"
PKG_ROOT = ROOT / "projects/packages/src/main/kotlin/fluent"
PLUGIN_KOTLIN = ROOT / "projects/plugins/intellij-fluent/src/main/kotlin"
TEST_ROOT = ROOT / "projects/plugins/intellij-fluent/src/test/kotlin"

INTEGRATION_FILES = {
    "ide/highlight/InjectVue.kt",
    "ide/highlight/FluentMarkdownFenceLanguageProvider.kt",
}

REPLACEMENTS = [
    ("com.github.projectfluent.language.psi.nodes", "fluent.surface.psi.nodes"),
    ("com.github.projectfluent.language.psi", "fluent.surface.psi"),
    ("com.github.projectfluent.language.file", "fluent.surface.file"),
    ("com.github.projectfluent.language.ast", "fluent.surface.ast"),
    ("com.github.projectfluent.ide.formatter", "fluent.editing.format"),
    ("com.github.projectfluent.ide.highlight", "fluent.editing.highlight"),
    ("com.github.projectfluent.ide.view", "fluent.editing.structure"),
    ("com.github.projectfluent.ide.matcher", "fluent.editing.assist"),
    ("com.github.projectfluent.ide.reference", "fluent.semantic.resolve"),
    ("com.github.projectfluent.ide.annotator", "fluent.semantic.inspect"),
    ("com.github.projectfluent.ide.codeStyle", "fluent.editing.format.codestyle"),
    ("com.github.projectfluent.ide.actions", "fluent.editing.actions"),
    ("com.github.projectfluent.ide.completion", "fluent.editing.completion"),
    ("com.github.projectfluent.ide.doc", "fluent.editing.documentation"),
    ("com.github.projectfluent.ide.todo", "fluent.editing.todo"),
    ("com.github.projectfluent.ide.project", "fluent.project"),
    ("com.github.projectfluent.FluentBundle", "fluent.definition.FluentBundle"),
    ("com.github.projectfluent.FluentLanguage", "fluent.definition.FluentLanguage"),
    ("package com.github.projectfluent.language.psi.nodes", "package fluent.surface.psi.nodes"),
    ("package com.github.projectfluent.language.psi", "package fluent.surface.psi"),
    ("package com.github.projectfluent.language.file", "package fluent.surface.file"),
    ("package com.github.projectfluent.language.ast", "package fluent.surface.ast"),
    ("package com.github.projectfluent.language", "package fluent.surface"),
    ("package com.github.projectfluent.ide.formatter", "package fluent.editing.format"),
    ("package com.github.projectfluent.ide.highlight", "package fluent.editing.highlight"),
    ("package com.github.projectfluent.ide.view", "package fluent.editing.structure"),
    ("package com.github.projectfluent.ide.matcher", "package fluent.editing.assist"),
    ("package com.github.projectfluent.ide.reference", "package fluent.semantic.resolve"),
    ("package com.github.projectfluent.ide.annotator", "package fluent.semantic.inspect"),
    ("package com.github.projectfluent.ide.codeStyle", "package fluent.editing.format.codestyle"),
    ("package com.github.projectfluent.ide.actions", "package fluent.editing.actions"),
    ("package com.github.projectfluent.ide.completion", "package fluent.editing.completion"),
    ("package com.github.projectfluent.ide.doc", "package fluent.editing.documentation"),
    ("package com.github.projectfluent.ide.todo", "package fluent.editing.todo"),
    ("package com.github.projectfluent.ide.project", "package fluent.project"),
    ("package com.github.projectfluent.ide", "package fluent.editing"),
    ("package com.github.projectfluent", "package fluent.definition"),
]


def rewrite(text: str) -> str:
    for old, new in REPLACEMENTS:
        text = text.replace(old, new)
    return text


def map_target(rel: str) -> str | None:
    if rel in INTEGRATION_FILES:
        return None
    if rel == "FluentLanguage.kt":
        return "definition/FluentLanguage.kt"
    if rel == "FluentBundle.kt":
        return "definition/FluentBundle.kt"
    prefixes = {
        "language/file/": "surface/file/",
        "language/psi/": "surface/psi/",
        "language/ast/": "surface/ast/",
        "ide/formatter/": "editing/format/",
        "ide/view/": "editing/structure/",
        "ide/matcher/": "editing/assist/",
        "ide/reference/": "semantic/resolve/",
        "ide/annotator/": "semantic/inspect/",
        "ide/codeStyle/": "editing/format/codestyle/",
        "ide/actions/": "editing/actions/",
        "ide/highlight/": "editing/highlight/",
        "ide/completion/": "editing/completion/",
        "ide/doc/": "editing/documentation/",
        "ide/todo/": "editing/todo/",
        "ide/project/": "project/",
    }
    for prefix, target in prefixes.items():
        if rel.startswith(prefix):
            return target + rel[len(prefix) :]
    raise RuntimeError(f"unmapped fluent source: {rel}")


def migrate_sources() -> None:
    if PKG_ROOT.exists():
        shutil.rmtree(PKG_ROOT)
    PKG_ROOT.mkdir(parents=True)

    integration_dir = PLUGIN_KOTLIN / "fluent/plugin/integration"
    if integration_dir.exists():
        shutil.rmtree(integration_dir.parent.parent)
    integration_dir.mkdir(parents=True)

    for src in sorted(SRC_ROOT.rglob("*.kt")):
        rel = src.relative_to(SRC_ROOT).as_posix()
        if rel in INTEGRATION_FILES:
            dst = integration_dir / src.name
            text = rewrite(src.read_text(encoding="utf-8"))
            text = text.replace("package fluent.editing.highlight", "package fluent.plugin.integration")
            text = text.replace("fluent.editing.highlight.FluentMarkdownFenceLanguageProvider", "fluent.plugin.integration.FluentMarkdownFenceLanguageProvider")
            dst.write_text(text, encoding="utf-8", newline="\n")
            continue
        target_rel = map_target(rel)
        dst = PKG_ROOT / target_rel
        dst.parent.mkdir(parents=True, exist_ok=True)
        dst.write_text(rewrite(src.read_text(encoding="utf-8")), encoding="utf-8", newline="\n")

    shutil.rmtree(SRC_ROOT.parent.parent.parent)


def migrate_tests() -> None:
    legacy_root = TEST_ROOT / "com/github/projectfluent"
    if not legacy_root.exists():
        return

    for src in sorted(legacy_root.rglob("*.kt")):
        text = rewrite(src.read_text(encoding="utf-8"))
        rel = src.relative_to(legacy_root)
        if rel.parts[0] == "language":
            new_rel = Path("fluent/surface") / rel.name
        elif rel.name == "FluentInjectionTest.kt":
            new_rel = Path("fluent/plugin/integration") / rel.name
            text = text.replace("package fluent.editing", "package fluent.plugin.integration")
            text = text.replace(
                "fluent.editing.highlight.InjectVue",
                "fluent.plugin.integration.InjectVue",
            )
            text = text.replace(
                "fluent.editing.highlight.FluentMarkdownFenceLanguageProvider",
                "fluent.plugin.integration.FluentMarkdownFenceLanguageProvider",
            )
        elif len(rel.parts) > 1 and rel.parts[0] == "ide" and rel.parts[1] == "view":
            new_rel = Path("fluent/editing/structure") / rel.name
        elif rel.parts[0] == "ide":
            new_rel = Path("fluent/editing") / rel.name
        else:
            raise RuntimeError(f"unmapped fluent test: {rel.as_posix()}")

        dst = TEST_ROOT / new_rel
        dst.parent.mkdir(parents=True, exist_ok=True)
        dst.write_text(text, encoding="utf-8", newline="\n")
        src.unlink()

    legacy = TEST_ROOT / "com"
    if legacy.exists():
        shutil.rmtree(legacy)


def write_plugin_descriptors() -> None:
    meta = ROOT / "projects/plugins/intellij-fluent/src/main/resources/META-INF"
    languages = meta / "languages"
    actions = meta / "actions"
    integrations = meta / "integrations"
    languages.mkdir(exist_ok=True)
    actions.mkdir(exist_ok=True)
    integrations.mkdir(exist_ok=True)

    (languages / "fluent.xml").write_text(
        """        <fileType
            name="Fluent"
            language="Fluent"
            extensions="ftl"
            implementationClass="fluent.surface.file.FluentFileType"
            fieldName="INSTANCE"
        />
        <lang.parserDefinition
            language="Fluent"
            implementationClass="fluent.surface.psi.FluentParserDefinition"
        />
        <lang.psiStructureViewFactory language="Fluent"
                                      implementationClass="fluent.editing.structure.FluentStructureViewFactory"/>
        <projectViewNestingRulesProvider implementation="fluent.surface.file.FluentFileGroup"/>
        <additionalTextAttributes scheme="Default" file="colors/FluentDefault.xml"/>
        <additionalTextAttributes scheme="Darcula" file="colors/FluentDarcula.xml"/>
        <colorSettingsPage implementation="fluent.editing.highlight.FluentHighlightSetting"/>
        <highlightVisitor implementation="fluent.editing.highlight.FluentSemanticHighlighter"/>
        <lang.syntaxHighlighter
            language="Fluent"
            implementationClass="fluent.editing.highlight.FluentSyntaxHighlighter"
        />
        <lang.formatter
            language="Fluent"
            implementationClass="fluent.editing.format.FluentFormatBuilder"
        />
        <lang.commenter
            language="Fluent"
            implementationClass="fluent.editing.format.FluentCommenter"
        />
        <lang.smartEnterProcessor
            language="Fluent"
            implementationClass="fluent.editing.format.FluentSmartEnter"
        />
        <lang.braceMatcher
            language="Fluent"
            implementationClass="fluent.editing.assist.FluentBraceMatcher"
        />
        <lang.foldingBuilder
            language="Fluent"
            implementationClass="fluent.editing.assist.FluentFoldingBuilder"
        />
        <annotator language="Fluent" implementationClass="fluent.semantic.inspect.LiteralChecker"/>
        <langCodeStyleSettingsProvider
            implementation="fluent.editing.format.codestyle.FluentLanguageCodeStyleSettingsProvider"
        />
        <multiHostInjector implementation="fluent.plugin.integration.InjectVue"/>
        <psi.referenceContributor implementation="fluent.semantic.resolve.FluentReferenceContributor"/>
""",
        encoding="utf-8",
        newline="\n",
    )

    (actions / "fluent-actions.xml").write_text(
        """    <actions>
        <action id="NewFluentFile" class="fluent.editing.actions.FluentCreateFile" text="Create Fluent" description="Create fluent file" icon="/icons/ftl.svg">
            <add-to-group group-id="NewGroup" anchor="before" relative-to-action="NewHtmlFile"/>
        </action>
        <group id="fluent.FluentGenerator" popup="true" text="FluentGenerator">
            <reference ref="NewFluentFile"/>
            <add-to-group group-id="EditorPopupMenu" anchor="after" relative-to-action="Github.Create.Gist"/>
            <add-to-group group-id="ProjectViewPopupMenu" anchor="after" relative-to-action="Github.Create.Gist"/>
            <add-to-group group-id="EditorTabPopupMenu" anchor="after" relative-to-action="Github.Create.Gist"/>
            <add-to-group group-id="ConsoleEditorPopupMenu" anchor="after" relative-to-action="Github.Create.Gist"/>
        </group>
    </actions>
""",
        encoding="utf-8",
        newline="\n",
    )

    (integrations / "plugin-with-markdown.xml").write_text(
        """<idea-plugin>
    <extensions defaultExtensionNs="org.intellij.markdown">
        <fenceLanguageProvider implementation="fluent.plugin.integration.FluentMarkdownFenceLanguageProvider"/>
    </extensions>
</idea-plugin>
""",
        encoding="utf-8",
        newline="\n",
    )

    old_md = meta / "plugin-with-markdown.xml"
    if old_md.exists():
        old_md.unlink()

    (meta / "plugin.xml").write_text(
        """<idea-plugin xmlns:xi="http://www.w3.org/2001/XInclude">
    <id>FluentLanguage</id>
    <name>Fluent Language</name>
    <vendor>voml</vendor>
    <depends>com.intellij.modules.platform</depends>
    <depends>com.intellij.modules.xml</depends>
    <depends optional="true" config-file="integrations/plugin-with-markdown.xml">org.intellij.plugins.markdown</depends>
    <resource-bundle>messages.FluentBundle</resource-bundle>
    <extensions defaultExtensionNs="com.intellij">
        <xi:include href="languages/fluent.xml" parse="xml"/>
    </extensions>
    <xi:include href="actions/fluent-actions.xml" parse="xml"/>
</idea-plugin>
""",
        encoding="utf-8",
        newline="\n",
    )

    eco = ROOT / "projects/designs/ecosystems/fluent.yaml"
    eco.write_text(
        """id: fluent
plugin: intellij-fluent
repo: intellij-plugins
languages:
  - fluent
marketplace_id: FluentLanguage
""",
        encoding="utf-8",
        newline="\n",
    )


def patch_build_gradle() -> None:
    path = ROOT / "projects/plugins/intellij-fluent/build.gradle.kts"
    text = path.read_text(encoding="utf-8")
    if 'implementation(project(":packages"))' not in text:
        text = text.replace(
            "dependencies {\n    testImplementation(libs.junit)",
            "dependencies {\n    implementation(project(\":packages\"))\n\n    testImplementation(libs.junit)",
        )
        path.write_text(text, encoding="utf-8", newline="\n")


def main() -> None:
    migrate_sources()
    migrate_tests()
    write_plugin_descriptors()
    patch_build_gradle()
    print("fluent migration complete")


if __name__ == "__main__":
    main()
