package com.example.lib.detector;

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.example.lib.detector.LineEx.getQualifiedName
import com.intellij.lang.java.parser.JavaParser
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import org.jetbrains.uast.*
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

    override fun getApplicableUastTypes(): List<Class<out UElement>>? {
        return listOf(UCallExpression::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {

            override fun visitCallExpression(node: UCallExpression) {
                checkMethod(context, node)
            }
        }
    }

    private fun checkMethod(context: JavaContext, node: UCallExpression) {
        if (LintMatcher.match(
                "android.graphics.Color.parseColor",
                "",
                node.getQualifiedName(),
                node.getContainingUClass()?.qualifiedName, null, null
            )
        ) {
            val tryExpression: UTryExpression? =
                node.getParentOfType(UTryExpression::class.java)//获取try节点

            if (tryExpression == null) {
                context.report(
                    ColorParseDetector.ISSUE,
                    node,
                    context.getLocation(node),
                    ColorParseDetector.REPORT_MESSAGE
                )
                return
            }
            for (catch in tryExpression.catchClauses) {//拿到catch
                for (reference in catch.typeReferences) {//拿到异常类型
                    if (context.evaluator.typeMatches(
                            reference.type,
                            "java.lang.IllegalArgumentException"
                        )//同一个异常
                        || context.evaluator.extendsClass(
                            context.evaluator.findClass("java.lang.IllegalArgumentException"),
                            reference.getQualifiedName()!!,
                            true
                        )//try的是异常的父类
                    ) {
                        return
                    }
                }
            }
            context.report(
                ColorParseDetector.ISSUE,
                node,
                context.getLocation(node),
                ColorParseDetector.REPORT_MESSAGE
            )
        }

    }

}
