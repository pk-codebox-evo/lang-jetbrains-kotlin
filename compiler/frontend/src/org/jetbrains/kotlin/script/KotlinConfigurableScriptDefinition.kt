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

package org.jetbrains.kotlin.script

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.descriptors.ScriptDescriptor
import org.jetbrains.kotlin.descriptors.ScriptExternalParameters
import org.jetbrains.kotlin.descriptors.ScriptValueParameter
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.parsing.KotlinParserDefinition
import org.jetbrains.kotlin.psi.KtScript
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.utils.KotlinPaths
import org.jetbrains.kotlin.utils.KotlinPathsFromHomeDir
import org.jetbrains.kotlin.utils.PathUtil
import java.io.File


data class KotlinConfigurableScriptDefinition(val config: KotlinScriptConfig, val environmentVars: Map<String, List<String>>?) : KotlinScriptDefinition {
    override val name = config.name

    override fun getScriptExternalParameters(scriptDescriptor: ScriptDescriptor) = object: ScriptExternalParameters {
        override val superclassConstructorParametersToScriptParametersMap: List<Pair<Name, KotlinType>>
            get() = config.superclassParamsMapping.map { Pair(Name.identifier(it.scriptParamName), getKotlinTypeByFqName(scriptDescriptor, it.superclassParamType)) }
        override val valueParameters: List<ScriptValueParameter>
            get() = config.parameters.map { ScriptValueParameter(Name.identifier(it.name), getKotlinTypeByFqName(scriptDescriptor, it.type)) }
        override val supertypes: List<KotlinType>
            get() = config.supertypes.map { getKotlinTypeByFqName(scriptDescriptor, it) }
    }

    override fun isScript(file: VirtualFile): Boolean =
            Regex(config.fileNameMatch).matches(file.name)

    override fun isScript(file: PsiFile): Boolean =
        Regex(config.fileNameMatch).matches(file.name)

    override fun getScriptName(script: KtScript): Name = ScriptNameUtil.fileNameWithExtensionStripped(script, KotlinParserDefinition.STD_SCRIPT_EXT)

    private val evaluatedClasspath by lazy { config.classpath.evalWithVars(environmentVars).distinct() }

    override fun getScriptDependenciesClasspath(): List<String> = evaluatedClasspath
}


// return all combination of replacements of vars in the strings
// if corresponding list of replacements is empty, all strings containing the reference to the var are removed
// TODO: fix and tests
// TODO: move to some utils
internal fun List<String>.evalWithVars(varsMap: Map<String, List<String>>?): List<String> =
        if (varsMap == null || varsMap.isEmpty()) this
        else this.flatMap { cpentry ->
            varsMap.entries.fold(listOf(cpentry)) { p, v ->
                if (cpentry.contains("\${${v.key}}")) {
                    if (v.value.isEmpty()) emptyList()
                    else v.value.flatMap { valListElement ->
                        p.map { it.replace("\${${v.key}}", valListElement) }
                    }
                }
                else p
            }
        }

fun generateKotlinScriptClasspathEnvVars(project: Project, kotlinHomeDir: File? = null): Map<String, List<String>> =
        generateKotlinScriptClasspathEnvVarsFromPaths(
                project,
                kotlinHomeDir?.let { KotlinPathsFromHomeDir(it) } ?: tryFindKotlinPathsForScriptClasspathEnvVars(project))

private fun tryFindKotlinPathsForScriptClasspathEnvVars(project: Project): KotlinPaths? =
    sequenceOf( { PathUtil.getKotlinPathsForIdeaPlugin() },
                { PathUtil.getKotlinPathsForJpsPlugin() },
                { PathUtil.getKotlinPathsForCompiler() },
                { KotlinPathsFromHomeDir(File(project.basePath ?: ".")) },
                // note: these are only usable when debugging kotlin project
                // TODO: replace with more reliable mechanism
                { KotlinPathsFromHomeDir(File(PathUtil.getPathUtilJar().parentFile.parentFile.parentFile, "dist/kotlinc")) })
    .map { it() }
    .firstOrNull { it.runtimePath.exists() }

private fun generateKotlinScriptClasspathEnvVarsFromPaths(project: Project, paths: KotlinPaths?): Map<String, List<String>> =
        mapOf("kotlin-runtime" to (paths?.run { listOf(runtimePath.canonicalPath) } ?: emptyList()),
              "kotlin-reflect" to (paths?.run { listOf(reflectPath.canonicalPath) } ?: emptyList()),
              "project-root" to listOf(project.basePath ?: "."),
              "jdk" to PathUtil.getJdkClassesRoots().map { it.canonicalPath })
        .filterNot { it.value.isEmpty() }
