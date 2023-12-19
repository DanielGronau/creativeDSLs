package creativeDsls.chapter_12.units.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import creativeDSLs.chapter_12.units.MultiplicationResult
import kotlin.reflect.KClass

class MultiplicationVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

        val shortName = classDeclaration.simpleName.getShortName()

        val factorPairs: List<Pair<ClassName, ClassName>> = classDeclaration
            .getAnnotations(MultiplicationResult::class)
            .map(KSAnnotation::arguments)
            .map { args ->
                val factor1 = args.first { arg -> arg.name?.getShortName() == "factor1" }.value as KSType
                val factor2 = args.first { arg -> arg.name?.getShortName() == "factor2" }.value as KSType
                factor1.toClassName() to factor2.toClassName()
            }

        val fileSpec =
            FileSpec.builder(
                packageName = classDeclaration.packageName.asString(),
                fileName = shortName.lowercase() + "Multiplications"
            ).run {
                factorPairs.forEach { (factor1, factor2) ->
                    addFunctions(factor1, factor2, classDeclaration.toClassName())
                    if (factor1.toString() != factor2.toString()) {
                        addFunctions(factor2, factor1, classDeclaration.toClassName())
                    }
                }
                build()
            }

        fileSpec.writeTo(codeGenerator, false)
    }

    private fun FileSpec.Builder.addFunctions(
        factor1: ClassName,
        factor2: ClassName,
        result: ClassName
    ) {
        addFunction(makeMultiplication(factor1, factor2, result))
        addFunction(makeDivision(result, factor1, factor2))
    }

    private fun makeMultiplication(
        in1: ClassName,
        in2: ClassName,
        out: ClassName
    ) = FunSpec.builder("times")
        .addModifiers(KModifier.OPERATOR)
        .receiver(in1)
        .returns(out)
        .addParameter("that", in2)
        .addStatement("return %T(this.component1() * that.component1())", out)
        .build()

    private fun makeDivision(
        in1: ClassName,
        in2: ClassName,
        out: ClassName
    ) = FunSpec.builder("div")
        .addModifiers(KModifier.OPERATOR)
        .receiver(in1)
        .returns(out)
        .addParameter("that", in2)
        .addStatement("return %T(this.component1() / that.component1())", out)
        .build()

    private fun KSClassDeclaration.getAnnotations(annotationClass: KClass<*>): List<KSAnnotation> =
        annotations.filter { it.shortName.getShortName() == annotationClass.simpleName }.toList()
}