package jss.surface.file

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader

object JssIcons {
    val FILE = IconLoader.getIcon("/icons/jssIcon.svg", JssIcons::class.java)
    val SCHEMA = AllIcons.Actions.Annotate
    val CLASS = AllIcons.Nodes.Class
    val UNION = AllIcons.Nodes.Enum
    val CONSTANT = AllIcons.Nodes.Constant
    val MUTABLE = AllIcons.Nodes.Variable
    val FIELD = AllIcons.Nodes.Field
    val BOUND = AllIcons.Nodes.ObjectTypeAttribute
    val PROPERTY = AllIcons.Nodes.Property
    val ANNOTATION = AllIcons.Nodes.ObjectTypeAttribute
}