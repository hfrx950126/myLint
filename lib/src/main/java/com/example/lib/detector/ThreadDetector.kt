package com.example.lib.detector

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.getQualifiedName
import org.jetbrains.uast.util.isConstructorCall
import java.util.*


/**
 * Create by huangfuruixin@immomo.com
 * 2020/9/17
 *
 */
class ThreadDetector : Detector(), Detector.UastScanner {

    companion object {
        private const val NEW_THREAD: String = "java.lang.Thread"
        private const val ISSUE_THREAD_DESCRIPTION = "FBI WARING!:You should use MomoTaskExecutor or ThreadPool"

        public val ISSUE: Issue = Issue.create(
                "ThreadUsage",
                "ThreadUsage",
                ISSUE_THREAD_DESCRIPTION,
                Category.CORRECTNESS,
                6,
                Severity.ERROR,
                Implementation(ThreadDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

    }

    override fun getApplicableUastTypes(): List<Class<out UElement>>? {
        return Collections.singletonList(UCallExpression::class.java)
    }


    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {
            override fun visitCallExpression(node: UCallExpression) {
                if (!node.isConstructorCall()) {
                    return
                }
                val className: String?
                val classRef = node.classReference
                if (classRef != null) {
                    className = classRef.getQualifiedName()
                    if (NEW_THREAD == className && context.project.isAndroidProject) {
                        context.report(
                                ThreadDetector.ISSUE,
                                node,
                                context.getLocation(node),
                                "\u21E2 Avoid call new Thread() directly"
                        )
                    }
                }
            }
        }
    }
}