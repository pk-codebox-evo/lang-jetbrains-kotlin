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
package org.jetbrains.kotlin.js.translate.declaration

import com.google.dart.compiler.backend.js.ast.JsExpression
import com.google.dart.compiler.backend.js.ast.JsFunction
import com.google.dart.compiler.backend.js.ast.JsName
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.js.translate.context.TranslationContext
import org.jetbrains.kotlin.js.translate.general.Translation
import org.jetbrains.kotlin.js.translate.utils.BindingUtils
import org.jetbrains.kotlin.js.translate.utils.JsAstUtils
import org.jetbrains.kotlin.js.translate.utils.JsDescriptorUtils
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.resolve.BindingContext

class FileDeclarationVisitor(private val context: TranslationContext) : AbstractDeclarationVisitor() {
    override fun visitProperty(expression: KtProperty, context: TranslationContext) {
        val propertyDescriptor = BindingUtils.getPropertyDescriptor(context.bindingContext(), expression)

        val innerName = context.getInnerNameForDescriptor(propertyDescriptor)
        val backingFieldRequired = context.bindingContext()[BindingContext.BACKING_FIELD_REQUIRED, propertyDescriptor] ?: false
        if (backingFieldRequired || expression.delegateExpression != null) {
            val initializer = expression.delegateExpressionOrInitializer?.let { Translation.translateAsExpression(it, context) }
            context.addDeclarationStatement(JsAstUtils.newVar(innerName, null))
            if (initializer != null) {
                context.addTopLevelStatement(JsAstUtils.assignment(innerName.makeRef(), initializer).makeStmt())
            }
        }

        super.visitProperty(expression, context)
    }

    override fun addClass(descriptor: ClassDescriptor) {
        context.export(descriptor)
    }

    override fun addFunction(descriptor: FunctionDescriptor, expression: JsExpression) {
        addFunctionButNotExport(descriptor, expression)
        context.export(descriptor)
    }

    override fun addProperty(descriptor: PropertyDescriptor, getter: JsExpression, setter: JsExpression?) {
        if (!JsDescriptorUtils.isSimpleFinalProperty(descriptor)) {
            addFunctionButNotExport(descriptor.getter!!, getter)
            if (setter != null) {
                addFunctionButNotExport(descriptor.setter!!, setter)
            }
        }
        context.export(descriptor)
    }

    override fun getBackingFieldReference(descriptor: PropertyDescriptor): JsExpression {
        return context.getInnerReference(descriptor)
    }

    private fun addFunctionButNotExport(descriptor: FunctionDescriptor, expression: JsExpression): JsName {
        val name = context.getInnerNameForDescriptor(descriptor)
        when (expression) {
            is JsFunction -> {
                expression.name = name
                context.addDeclarationStatement(expression.makeStmt())
            }
            else -> {
                context.addDeclarationStatement(JsAstUtils.newVar(name, expression))
            }
        }
        return name
    }
}
