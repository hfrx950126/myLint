package com.rocketzly.checks

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.example.lib.detector.ColorParseDetector
import com.example.lib.detector.SerializableClassDetector
import java.io.File

/**
 * Create by huangfuruixin@immomo.com
 * 2020/9/17
 *
 */
class ColorParseDetectorTest: LintDetectorTest(){
    override fun getDetector(): Detector {
        return ColorParseDetector()
    }

    override fun getIssues(): MutableList<Issue> {
       return  mutableListOf(ColorParseDetector.ISSUE)
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