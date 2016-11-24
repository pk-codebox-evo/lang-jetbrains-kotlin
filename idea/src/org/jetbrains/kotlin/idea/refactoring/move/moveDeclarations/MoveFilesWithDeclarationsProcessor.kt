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

package org.jetbrains.kotlin.idea.refactoring.move.moveDeclarations

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.EmptyRunnable
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.refactoring.move.MoveCallback
import com.intellij.refactoring.move.moveFilesOrDirectories.MoveFilesOrDirectoriesProcessor
import com.intellij.usageView.UsageInfo
import com.intellij.usageView.UsageViewUtil
import com.intellij.util.containers.MultiMap
import com.intellij.util.text.UniqueNameGenerator
import org.jetbrains.kotlin.psi.KtFile

class MoveFilesWithDeclarationsProcessor(
        project: Project,
        private val sourceFiles: List<KtFile>,
        targetDirectory: PsiDirectory,
        private val targetFileName: String?,
        searchInComments: Boolean,
        searchInNonJavaFiles: Boolean,
        moveCallback: MoveCallback?
) : MoveFilesOrDirectoriesProcessor(project,
                                    sourceFiles.toTypedArray<PsiElement>(),
                                    targetDirectory,
                                    true,
                                    searchInComments,
                                    searchInNonJavaFiles,
                                    MoveCallbackImpl(sourceFiles, targetFileName, moveCallback),
                                    EmptyRunnable.INSTANCE) {
    class MoveCallbackImpl(
            private val sourceFiles: List<KtFile>,
            private val targetFileName: String?,
            private val nextCallback: MoveCallback?
    ) : MoveCallback {
        override fun refactoringCompleted() {
            try {
                if (targetFileName != null) {
                    sourceFiles.single().name = targetFileName
                }
            }
            finally {
                nextCallback?.refactoringCompleted()
            }
        }
    }

    override fun getCommandName(): String {
        return if (targetFileName != null) "Move " + sourceFiles.single().name else "Move"
    }

    override fun preprocessUsages(refUsages: Ref<Array<UsageInfo>>): Boolean {
        val usages = refUsages.get()

        val distinctConflictUsages = UsageViewUtil.removeDuplicatedUsages(usages.filterIsInstance<ConflictUsageInfo>().toTypedArray())
        val conflicts = MultiMap<PsiElement, String>()
        for (conflictUsage in distinctConflictUsages) {
            conflicts.putValues(conflictUsage.element, (conflictUsage as ConflictUsageInfo).messages)
        }

        return showConflicts(conflicts, usages)
    }

    // Assign a temporary name to file-under-move to avoid naming conflict during the refactoring
    private fun renameFileTemporarily() {
        if (targetFileName != null) {
            val sourceFile = sourceFiles.single()
            //noinspection ConstantConditions
            val temporaryName = UniqueNameGenerator.generateUniqueName(
                    "temp",
                    "",
                    ".kt",
                    sourceFile.containingDirectory!!.files.map { file -> file.name })
            sourceFile.name = temporaryName
        }
    }

    override fun performRefactoring(usages: Array<UsageInfo>) {
        renameFileTemporarily()

        super.performRefactoring(usages)
    }
}
