/**
 * Flatten `xi:include` fragments in processed `META-INF/plugin.xml`.
 * IntelliJ test sandboxes do not resolve XInclude, so extensions must be inlined at build time.
 */
tasks.withType<ProcessResources>().configureEach {
    val metaInf = project.file("src/main/resources/META-INF")
    doLast {
        val pluginXml = destinationDir.resolve("META-INF/plugin.xml")
        if (!pluginXml.isFile) {
            return@doLast
        }
        val includePattern = Regex("""<xi:include\s+href="([^"]+)"\s+parse="xml"\s*/>""")
        var result = pluginXml.readText()
        while (true) {
            val match = includePattern.find(result) ?: break
            val href = match.groupValues[1]
            val included = metaInf.resolve(href).readText().trim()
            result = result.replaceRange(match.range, included)
        }
        result = result.replace(" xmlns:xi=\"http://www.w3.org/2001/XInclude\"", "")
        pluginXml.writeText(result)
    }
}
