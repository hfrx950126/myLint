package com.example.lib.detector;

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

public class LogDetector : Detector(), Detector.UastScanner {
    companion object {
        private const val REPORT_MESSAGE = "不要直接使用Log,请使用MDLog，放置在正式包打印Log"
        val ISSUE = Issue.create(
                "LogDetector",
                REPORT_MESSAGE,
                REPORT_MESSAGE,
                Category.SECURITY,
                10,
                Severity.ERROR,
                Implementation(LogDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    override fun getApplicableMethodNames(): List<String>? {
        return listOf("v", "d", "i", "w", "e")
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (context.evaluator.isMemberInClass(method, "android.util.Log")) {
            context.report(ISSUE, node, context.getLocation(node), "请勿直接调用android.util.Log，应该使用统一Log工具类")
        }
    }


}
