package com.example.lib.detector

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameterList
import org.jetbrains.uast.UCallExpression


/**
 * Create by huangfuruixin@immomo.com
 * 2020/9/17
 * 检测Activity 和 Fragment 的布局 xml 文件是否是以activity_ 和 fragment_ 开头
 */
class ActivityFragmentLayoutNameDetector : Detector(), Detector.UastScanner {
    companion object {
        private const val ISSUE_ACTIVITY_ID = "LayoutNamePrefixError"
        private const val ISSUE_ACTIVITY_DESCRIPTION = "FBI WARING!:You should name an activity-layout file with prefix {activity_}"
        private const val ISSUE_ACTIVITY_EXPLANATION = "FBI WARING!:You should name an activity-layout file with prefix {activity_}. For example, `activity_function.xml`."
        private const val ISSUE_FRAGMENT_ID = "LayoutNamePrefixError"
        private const val ISSUE_FRAGMENT_DESCRIPTION = "FBI WARING!:You should name an fragment-layout file with prefix {fragment_}"
        private const val ISSUE_FRAGMENT_EXPLANATION = "FBI WARING!:You should name an fragment-layout file with prefix {fragment_}. For example, `fragment_function.xml`."

        val ACTIVITY_ISSUE = Issue.create(
                ISSUE_ACTIVITY_ID,
                ISSUE_ACTIVITY_DESCRIPTION,
                ISSUE_ACTIVITY_EXPLANATION,
                Category.SECURITY,
                10,
                Severity.ERROR,
                Implementation(LogDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
        val FRAGMENT_ISSUE = Issue.create(
                ISSUE_FRAGMENT_ID,
                ISSUE_FRAGMENT_DESCRIPTION,
                ISSUE_FRAGMENT_EXPLANATION,
                Category.SECURITY,
                10,
                Severity.ERROR,
                Implementation(LogDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    override fun getApplicableMethodNames(): List<String>? {
        return listOf("setContentView", "inflate")
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (method.name == "setContentView") {
            //TODO 检测
            context.report(ACTIVITY_ISSUE,
                    node,
                    context.getLocation(node),
                    ISSUE_ACTIVITY_DESCRIPTION)
        } else if (method.name == "inflate") {
            context.report(FRAGMENT_ISSUE,
                    node,
                    context.getLocation(node),
                    ISSUE_ACTIVITY_DESCRIPTION)
        }
    }

    /**
     * We get the layout file resource name, for example "R.layout.fragment_blank".
     * This method will check if it starts with the given prefix.
     *
     * @param layoutFileResourceString layout resource file name, like "R.layout.fragment_blank"
     * @param prefix                   the given prefix, must be "activity_" or "fragment)"
     * @return "true" if layoutFileResourceString starts with prefix, "false" otherwise.
     */
    private fun isFileStringStartWithPrefix(layoutFileResourceString: String, prefix: String): Boolean {
        val lastDotIndex = layoutFileResourceString.lastIndexOf(".")
        val fileName = layoutFileResourceString.substring(lastDotIndex + 1)
        return fileName.startsWith(prefix)
    }

}
