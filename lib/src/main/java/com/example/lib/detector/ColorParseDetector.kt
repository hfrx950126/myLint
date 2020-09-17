package com.example.lib.detector;

import com.android.tools.lint.detector.api.*
import com.intellij.lang.java.parser.JavaParser
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UastLanguagePlugin
import org.jline.builtins.Completers.TreeCompleter.node


/**
 * Create by huangfuruixin@immomo.com
 * 2020/9/17
 */
class ColorParseDetector : Detector(), Detector.UastScanner {
    companion object {
        private const val REPORT_MESSAGE = "Color.parseColor需要try-catch处理IllegalArgumentException"
        val ISSUE = Issue.create(
            "COlOR.PARSECOLOR",
            REPORT_MESSAGE,
            REPORT_MESSAGE,
            Category.SECURITY,
            10,
            Severity.ERROR,
            Implementation(ColorParseDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    override fun getApplicableMethodNames(): List<String>? {
        return listOf("parseColor")
    }


    override fun visitMethod(
        context: JavaContext,
        visitor: JavaElementVisitor?,
        call: PsiMethodCallExpression,
        method: PsiMethod
    ) {
        val psiClass: PsiClass? = method.containingClass

        val isSubClass: Boolean = false

    }


    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (context.evaluator.isMemberInClass(method, "android.graphics.Color")) {
            context.report(
                ISSUE, node, context.getLocation(node),
                "Color.parseColor需要try-catch处理IllegalArgumentException"
            )

        }
    }
}
