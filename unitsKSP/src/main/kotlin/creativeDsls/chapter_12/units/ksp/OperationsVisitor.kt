package creativeDsls.chapter_12.units.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

class OperationsVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

        val shortName = classDeclaration.simpleName.getShortName()

        if (Modifier.SEALED !in classDeclaration.modifiers) {
            logger.error("Can't generator operations, <$shortName> is not a sealed class.")
        }

        val subclasses: Sequence<ClassName> =
            classDeclaration.getSealedSubclasses().map { it.toClassName() }

        val fileSpec =
            FileSpec.builder(
                packageName = classDeclaration.packageName.asString(),
                fileName = shortName.lowercase() + "Operations"
            ).run {
                subclasses.forEach { subclass ->
                    addFunction(makeAddition(subclass))
                    addFunction(makeSubtraction(subclass))
                    addFunction(makeNegation(subclass))
                    addFunction(makeScalarMultiplication(subclass))
                }
                build()
            }

        fileSpec.writeTo(codeGenerator, false)
    }

    private fun makeAddition(className: ClassName) =
        FunSpec.builder("plus")
            .addModifiers(KModifier.OPERATOR)
            .receiver(className)
            .returns(className)
            .addParameter("that", className)
            .addStatement("return copy(this.component1() + that.component1())")
            .build()

    private fun makeSubtraction(className: ClassName) =
        FunSpec.builder("minus")
            .addModifiers(KModifier.OPERATOR)
            .receiver(className)
            .returns(className)
            .addParameter("that", className)
            .addStatement("return copy(this.component1() - that.component1())")
            .build()

    private fun makeNegation(className: ClassName) =
        FunSpec.builder("unaryMinus")
            .addModifiers(KModifier.OPERATOR)
            .receiver(className)
            .returns(className)
            .addStatement("return copy(-this.component1())")
            .build()

    private fun makeScalarMultiplication(className: ClassName) =
        FunSpec.builder("times")
            .addModifiers(KModifier.OPERATOR)
            .receiver(Double::class)
            .returns(className)
            .addParameter("that", className)
            .addStatement("return that.copy(this * that.component1())")
            .build()
}