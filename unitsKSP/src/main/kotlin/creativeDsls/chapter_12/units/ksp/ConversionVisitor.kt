package creativeDsls.chapter_12.units.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import creativeDSLs.chapter_12.units.Conversion

class ConversionVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : KSVisitorVoid() {

    @OptIn(KspExperimental::class)
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

        val shortName = classDeclaration.simpleName.getShortName()

        val annotations: List<Conversion> = classDeclaration
            .getAnnotationsByType(Conversion::class).toList()

        val fileSpec =
            FileSpec.builder(
                packageName = classDeclaration.packageName.asString(),
                fileName = shortName.lowercase() + "Conversions"
            ).run {
                annotations.forEach { conversion ->
                    addProperty(
                        makeDoubleToQuantity(
                            conversion.derivedUnit,
                            classDeclaration.toClassName(),
                            conversion.factor
                        )
                    )
                    addProperty(
                        makeQuantityToDouble(
                            conversion.derivedUnit,
                            classDeclaration.toClassName(),
                            conversion.factor
                        )
                    )
                }
                build()
            }

        fileSpec.writeTo(codeGenerator, false)
    }

    // don't use %L until https://github.com/square/kotlinpoet/issues/1919 is solved
    fun makeDoubleToQuantity(unit: String, className: ClassName, factor: Double) =
        PropertySpec.builder(unit, className)
            .receiver(Double::class)
            .getter(
                FunSpec.getterBuilder()
                    .addStatement("return %T(this * $factor)", className)
                    .build()
            )
            .build()

    // don't use %L until https://github.com/square/kotlinpoet/issues/1919 is solved
    fun makeQuantityToDouble(unit: String, className: ClassName, factor: Double) =
        PropertySpec.builder(unit, Double::class)
            .receiver(className)
            .getter(
                FunSpec.getterBuilder()
                    .addStatement("return this.component1() / $factor")
                    .build()
            )
            .build()
}