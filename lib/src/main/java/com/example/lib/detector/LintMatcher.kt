package com.example.lib.detector

import java.util.regex.Pattern

/**
 * Create by huangfuruixin@immomo.com
 * 2020/9/17
 *
 */

class LintMatcher{
    companion object {
        /**
         * name是完全匹配，nameRegex是正则匹配，匹配优先级上name > nameRegex
         * inClassName是当前需要匹配的方法所在类
         * exclude是要排除匹配的类（目前以类的粒度去排除）
         */
        fun match(
            name: String?,
            nameRegex: String?,
            qualifiedName: String?,
            inClassName: String? = null,
            exclude: List<String>? = null,
            excludeRegex: String? = null
        ): Boolean {
            qualifiedName ?: return false

            //排除
            if (inClassName != null && inClassName.isNotEmpty()) {
                if (exclude != null && exclude.contains(inClassName)) return false

                if (excludeRegex != null &&
                    excludeRegex.isNotEmpty() &&
                    Pattern.compile(excludeRegex).matcher(inClassName).find()
                ) {
                    return false
                }
            }

            if (name != null && name.isNotEmpty() && name == qualifiedName) {//优先匹配name
                return true
            }
            if (nameRegex != null && nameRegex.isNotEmpty() &&
                Pattern.compile(nameRegex).matcher(qualifiedName).find()
            ) {//在匹配nameRegex
                return true
            }
            return false
        }
    }
}