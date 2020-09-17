package com.rocketzly.checks

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.example.lib.detector.SerializableClassDetector
import java.io.File

class SerializableDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector {
        return SerializableClassDetector()
    }

    override fun getIssues(): MutableList<Issue> {
        return mutableListOf(SerializableClassDetector.ISSUE)
    }

    fun test() {
        lint()
            .files(
                kotlin(File("./src/test/java/com/rocketzly/checks/SerializableBean.kt").readText())
            )
            .run()
            .expect(
                "No warnings."
            )
    }

}