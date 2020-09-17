package com.example.lib.detector.LineEx

import org.jetbrains.uast.UCallExpression

/**
 * Create by huangfuruixin@immomo.com
 * 2020/9/17
 *
 */
fun UCallExpression.getQualifiedName(): String {
    return resolve()?.containingClass?.qualifiedName + "." + resolve()?.name
}