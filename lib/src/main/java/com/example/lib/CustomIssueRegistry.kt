package com.example.lib;

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue
import com.example.lib.detector.*

class CustomIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(
                SerializableClassDetector.ISSUE,
                LogDetector.ISSUE,
                ActivityFragmentLayoutNameDetector.ACTIVITY_ISSUE,
                ActivityFragmentLayoutNameDetector.FRAGMENT_ISSUE,
                ThreadDetector.ISSUE,
                PngDetector.ISSUE_PNG_IN_CODE,
                PngDetector.ISSUE_PNG_IN_XML,
                ColorParseDetector.ISSUE
        )

    override val api: Int
        get() = CURRENT_API
}
