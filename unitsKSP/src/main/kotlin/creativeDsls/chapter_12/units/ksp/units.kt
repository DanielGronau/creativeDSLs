package creativeDsls.chapter_12.units.ksp

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import kotlin.reflect.KClass


private fun makeMultiplication(
    in1: KClass<*>,
    in2: KClass<*>,
    out: KClass<*>
) = FunSpec.builder("times")
    .addModifiers(KModifier.OPERATOR)
    .receiver(in1)
    .returns(out)
    .addParameter("that", in2)
    .addStatement("return %T(this.amount * that.amount)", out)
    .build()

private fun makeDivision(
    in1: KClass<*>,
    in2: KClass<*>,
    out: KClass<*>
) = FunSpec.builder("div")
    .addModifiers(KModifier.OPERATOR)
    .receiver(in1)
    .returns(out)
    .addParameter("that", in2)
    .addStatement("return %T(this.amount / that.amount)", out)
    .build()

typealias MultiplyClasses = Triple<KClass<*>, KClass<*>, KClass<*>>

private fun makeMultiplications(multiplyList: List<MultiplyClasses>) =
    multiplyList.flatMap { (in1, in2, out) ->
        when {
            in1 == in2 -> listOf(makeMultiplication(in1, in2, out))
            else -> listOf(makeMultiplication(in1, in2, out), makeMultiplication(in2, in1, out))
        }
    }

private fun makeDivisions(multiplyList: List<MultiplyClasses>) =
    multiplyList.flatMap { (in1, in2, out) ->
        when {
            in1 == in2 -> listOf(makeDivision(out, in1, in2))
            else -> listOf(makeDivision(out, in1, in2), makeDivision(out, in2, in1))
        }
    }

fun generate(
    multiplyList: List<MultiplyClasses>,
    fileSpec: FileSpec.Builder
) = fileSpec
    .addFunctions(makeMultiplications(multiplyList))
    .addFunctions(makeDivisions(multiplyList))
    .build()

private fun FileSpec.Builder.addProperties(properties: List<PropertySpec>) =
    this.also { properties.forEach { this.addProperty(it) } }

private fun FileSpec.Builder.addFunctions(functions: List<FunSpec>) =
    this.also { functions.forEach { this.addFunction(it) } }