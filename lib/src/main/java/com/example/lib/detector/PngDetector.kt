package com.example.lib.detector

import com.android.SdkConstants
import com.android.resources.ResourceFolderType
import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector.UastScanner
import com.android.tools.lint.detector.api.Scope.Companion.ALL_RESOURCES_SCOPE
import com.android.tools.lint.detector.api.Scope.Companion.JAVA_FILE_SCOPE
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.util.isMethodCall
import org.w3c.dom.Attr
import java.io.File
import java.util.*

/**
 * Author: Omooo
 * Date: 2019/7/5
 * Desc: Avoid to use png in layout or java file
 */
class PngDetector : Detector(), Detector.XmlScanner, UastScanner {
    override fun getApplicableAttributes(): Collection<String>? {
        return listOf(SdkConstants.ATTR_SRC)
    }

    override fun visitAttribute(context: XmlContext, attribute: Attr) {
        val srcValue = attribute.value
        if (check(srcValue.substring(10),
                        context.mainProject.resourceFolders)) {
            context.report(
                    ISSUE_PNG_IN_XML,
                    attribute,
                    context.getLocation(attribute),
                    LINT_MSG
            )
        }
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {
            override fun visitCallExpression(node: UCallExpression) {
                if (!node.isMethodCall()
                        || "setImageResource" != node.methodName
                        || node.valueArgumentCount != 1) {
                    return
                }
                val psiMethod = node.resolve()
                val b = context.evaluator.isMemberInClass(psiMethod, "android.widget.ImageView")
                if (b) {
                    if (check(node.valueArguments[0].toString().substring(11),
                                    context.mainProject.resourceFolders)) {
                        context.report(
                                ISSUE_PNG_IN_CODE,
                                node,
                                context.getLocation(node),
                                LINT_MSG
                        )
                    }
                }
            }
        }
    }

    private fun check(imageName: String, resFolders: List<File>): Boolean {
        var resFolder: File? = null
        for (file in resFolders) {
            if ("res" == file.name) {
                resFolder = File(file.path)
                break
            }
        }
        if (resFolder == null) {
            return false
        }
        val drawableFolder = File(resFolder.path, "drawable")
        if (!drawableFolder.exists() && !drawableFolder.isDirectory) {
            return false
        }
        // TODO: 2019/7/8 还需处理 drawable 目录下还有目录的情况
        val filesName = drawableFolder.list()
        if (filesName == null || filesName.size == 0) {
            return false
        }
        for (fileName in filesName) {
            if (fileName.substring(0, fileName.indexOf("."))
                    == imageName) {
                if (fileName.endsWith(".png")) {
                    return true
                }
                break
            }
        }
        return false
    }

    override fun appliesTo(folderType: ResourceFolderType): Boolean {
        return folderType == ResourceFolderType.LAYOUT
    }
    override fun getApplicableUastTypes(): List<Class<out UElement>>? {
        return Collections.singletonList(UCallExpression::class.java)
    }

    override fun run(context: Context) {
        assert(false)
    }

    companion object {
        private const val LINT_EXPLANATION = "WebP can provide better compression than PNG. "
        private const val LINT_MSG = "\u21E2 Please use webp instead of png."
        val ISSUE_PNG_IN_XML: Issue = Issue.create(
                "PngUseInXml",
                "Png Usage",
                LINT_EXPLANATION,
                CORRECTNESS,
                5, Severity.WARNING,
                Implementation(PngDetector::class.java, ALL_RESOURCES_SCOPE)
        )
        val ISSUE_PNG_IN_CODE: Issue = Issue.create(
                "PngUseInCode",
                "Png Usage",
                LINT_EXPLANATION,
                CORRECTNESS,
                5, Severity.WARNING,
                Implementation(PngDetector::class.java, JAVA_FILE_SCOPE)
        )
    }
}