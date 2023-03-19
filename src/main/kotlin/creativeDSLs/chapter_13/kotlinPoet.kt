package creativeDSLs.chapter_13

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.KModifier.VARARG
import kotlin.reflect.KClass

class FileSpecBuilder(val builder: FileSpec.Builder) {
    operator fun TypeSpec.unaryPlus() {
        builder.addType(this)
    }
    operator fun FunSpec.unaryPlus() {
        builder.addFunction(this)
    }
}

class TypeSpecBuilder(val builder: TypeSpec.Builder) {
    operator fun FunSpec.unaryPlus() {
        builder.addFunction(this)
    }
    operator fun PropertySpec.unaryPlus() {
        builder.addProperty(this)
    }
    var primaryConstructor: FunSpec? = null
        set(value) {
            builder.primaryConstructor(value)
        }
}

class FunSpecBuilder(val builder: FunSpec.Builder) {
    operator fun ParameterSpec.unaryPlus() {
        builder.addParameter(this)
    }
    operator fun Statement.unaryPlus() {
        builder.addStatement(this.line, *this.args)
    }
}

class PropertySpecBuilder(val builder: PropertySpec.Builder) {
    fun initializer(name: String, vararg args: Any?) {
        builder.initializer(name, *args)
    }
}

class Statement(val line: String, vararg val args: Any)

fun fileSpec(packageName: String, fileName: String, body: FileSpecBuilder.() -> Unit): FileSpec =
    FileSpecBuilder(FileSpec.builder(packageName, fileName)).apply(body).builder.build()

fun classSpec(className: String, body: TypeSpecBuilder.() -> Unit): TypeSpec =
    TypeSpecBuilder(TypeSpec.classBuilder(className)).apply(body).builder.build()

fun constructorSpec(body: FunSpecBuilder.() -> Unit): FunSpec =
    FunSpecBuilder(FunSpec.constructorBuilder()).apply(body).builder.build()

fun functionSpec(name: String, body: FunSpecBuilder.() -> Unit): FunSpec =
    FunSpecBuilder(FunSpec.builder(name)).apply(body).builder.build()

fun propertySpec(name: String, type: KClass<*>, body: PropertySpecBuilder.() -> Unit): PropertySpec =
    PropertySpecBuilder(PropertySpec.builder(name, type)).apply(body).builder.build()

fun parameter(name: String, type: KClass<*>, vararg modifiers: KModifier) =
    ParameterSpec.builder(name, type, *modifiers).build()

fun statement(line: String, vararg args: Any) = Statement(line, *args)

val greeterClass = ClassName("", "Greeter")
val file = fileSpec("", "HelloWorld") {
    +classSpec("Greeter") {
        primaryConstructor = constructorSpec {
            +parameter("name", String::class)
        }
        +propertySpec("name", String::class) {
            initializer("name")
        }
        +functionSpec("greet") {
            +statement("println(%P)", "Hello, \$name")
        }
    }
    +functionSpec("main") {
        +parameter("args", String::class, VARARG)
        +statement("%T(args[0]).greet()", greeterClass)
    }
}

fun main() {
    file.writeTo(System.out)
}
