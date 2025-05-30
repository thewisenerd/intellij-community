// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.

package org.jetbrains.kotlin.idea.k2.search;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.idea.base.plugin.KotlinPluginMode;
import org.jetbrains.kotlin.idea.base.test.TestRoot;
import org.jetbrains.kotlin.idea.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.idea.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

/**
 * This class is generated by {@link org.jetbrains.kotlin.testGenerator.generator.TestGenerator}.
 * DO NOT MODIFY MANUALLY.
 */
@SuppressWarnings("all")
@TestRoot("kotlin.searching/kotlin.searching.test.k2")
@TestDataPath("$CONTENT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
@TestMetadata("../../idea/tests/testData/search/annotations")
public class FirAnnotatedMembersSearchTestGenerated extends AbstractFirAnnotatedMembersSearchTest {
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public final KotlinPluginMode getPluginMode() {
        return KotlinPluginMode.K2;
    }

    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
    }

    @TestMetadata("annotationAliased.kt")
    public void testAnnotationAliased() throws Exception {
        runTest("../../idea/tests/testData/search/annotations/annotationAliased.kt");
    }

    @TestMetadata("testAmbiguousNestedNonAnnotationClass.kt")
    public void testTestAmbiguousNestedNonAnnotationClass() throws Exception {
        runTest("../../idea/tests/testData/search/annotations/testAmbiguousNestedNonAnnotationClass.kt");
    }

    @TestMetadata("testAmbiguousNestedPrivateAnnotationClass.kt")
    public void testTestAmbiguousNestedPrivateAnnotationClass() throws Exception {
        runTest("../../idea/tests/testData/search/annotations/testAmbiguousNestedPrivateAnnotationClass.kt");
    }

    @TestMetadata("testAnnotationsOnClass.kt")
    public void testTestAnnotationsOnClass() throws Exception {
        runTest("../../idea/tests/testData/search/annotations/testAnnotationsOnClass.kt");
    }

    @TestMetadata("testAnnotationsOnFunction.kt")
    public void testTestAnnotationsOnFunction() throws Exception {
        runTest("../../idea/tests/testData/search/annotations/testAnnotationsOnFunction.kt");
    }

    @TestMetadata("testAnnotationsOnPropertiesAndParameters.kt")
    public void testTestAnnotationsOnPropertiesAndParameters() throws Exception {
        runTest("../../idea/tests/testData/search/annotations/testAnnotationsOnPropertiesAndParameters.kt");
    }

    @TestMetadata("testAnnotationsOnPropertyAccessor.kt")
    public void testTestAnnotationsOnPropertyAccessor() throws Exception {
        runTest("../../idea/tests/testData/search/annotations/testAnnotationsOnPropertyAccessor.kt");
    }

    @TestMetadata("testAnnotationsWithParameters.kt")
    public void testTestAnnotationsWithParameters() throws Exception {
        runTest("../../idea/tests/testData/search/annotations/testAnnotationsWithParameters.kt");
    }

    @TestMetadata("testDefaultImport.kt")
    public void testTestDefaultImport() throws Exception {
        runTest("../../idea/tests/testData/search/annotations/testDefaultImport.kt");
    }

    @TestMetadata("testNestedClassAsAnnotation.kt")
    public void testTestNestedClassAsAnnotation() throws Exception {
        runTest("../../idea/tests/testData/search/annotations/testNestedClassAsAnnotation.kt");
    }

    @TestMetadata("testNestedPrivateAnnotationClass.kt")
    public void testTestNestedPrivateAnnotationClass() throws Exception {
        runTest("../../idea/tests/testData/search/annotations/testNestedPrivateAnnotationClass.kt");
    }

    @TestMetadata("testTypeAlias.kt")
    public void testTestTypeAlias() throws Exception {
        runTest("../../idea/tests/testData/search/annotations/testTypeAlias.kt");
    }

    @TestMetadata("useTarget.kt")
    public void testUseTarget() throws Exception {
        runTest("../../idea/tests/testData/search/annotations/useTarget.kt");
    }
}
