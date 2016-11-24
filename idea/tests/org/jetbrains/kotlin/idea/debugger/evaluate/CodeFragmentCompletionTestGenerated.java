/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.idea.debugger.evaluate;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("idea/idea-completion/testData/basic/codeFragments")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class CodeFragmentCompletionTestGenerated extends AbstractCodeFragmentCompletionTest {
    public void testAllFilesPresentInCodeFragments() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/idea-completion/testData/basic/codeFragments"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
    }

    @TestMetadata("blockCodeFragment.kt")
    public void testBlockCodeFragment() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/codeFragments/blockCodeFragment.kt");
        doTest(fileName);
    }

    @TestMetadata("classHeader.kt")
    public void testClassHeader() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/codeFragments/classHeader.kt");
        doTest(fileName);
    }

    @TestMetadata("elementAt.kt")
    public void testElementAt() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/codeFragments/elementAt.kt");
        doTest(fileName);
    }

    @TestMetadata("elementAtFirstInBlock.kt")
    public void testElementAtFirstInBlock() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/codeFragments/elementAtFirstInBlock.kt");
        doTest(fileName);
    }

    @TestMetadata("localVal.kt")
    public void testLocalVal() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/codeFragments/localVal.kt");
        doTest(fileName);
    }

    @TestMetadata("localVariables.kt")
    public void testLocalVariables() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/codeFragments/localVariables.kt");
        doTest(fileName);
    }

    @TestMetadata("localVariablesOnReturn.kt")
    public void testLocalVariablesOnReturn() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/codeFragments/localVariablesOnReturn.kt");
        doTest(fileName);
    }

    @TestMetadata("noDuplicatesForSyntheticProperties.kt")
    public void testNoDuplicatesForSyntheticProperties() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/codeFragments/noDuplicatesForSyntheticProperties.kt");
        doTest(fileName);
    }

    @TestMetadata("privatesInSecondPressCompletion.kt")
    public void testPrivatesInSecondPressCompletion() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/codeFragments/privatesInSecondPressCompletion.kt");
        doTest(fileName);
    }

    @TestMetadata("topLevel.kt")
    public void testTopLevel() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/codeFragments/topLevel.kt");
        doTest(fileName);
    }

    @TestMetadata("idea/idea-completion/testData/basic/codeFragments/runtimeType")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class RuntimeType extends AbstractCodeFragmentCompletionTest {
        public void testAllFilesPresentInRuntimeType() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/idea-completion/testData/basic/codeFragments/runtimeType"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
        }

        @TestMetadata("castWithGenerics.kt")
        public void testCastWithGenerics() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/codeFragments/runtimeType/castWithGenerics.kt");
            doTest(fileName);
        }

        @TestMetadata("complexHierarchy.kt")
        public void testComplexHierarchy() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/codeFragments/runtimeType/complexHierarchy.kt");
            doTest(fileName);
        }

        @TestMetadata("extensionMethod.kt")
        public void testExtensionMethod() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/codeFragments/runtimeType/extensionMethod.kt");
            doTest(fileName);
        }

        @TestMetadata("notImportedExtension.kt")
        public void testNotImportedExtension() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/codeFragments/runtimeType/notImportedExtension.kt");
            doTest(fileName);
        }

        @TestMetadata("runtimeCast.kt")
        public void testRuntimeCast() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/codeFragments/runtimeType/runtimeCast.kt");
            doTest(fileName);
        }

        @TestMetadata("smartCompletion.kt")
        public void testSmartCompletion() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/codeFragments/runtimeType/smartCompletion.kt");
            doTest(fileName);
        }
    }
}
