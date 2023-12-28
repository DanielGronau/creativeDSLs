package creativeDSLs.chapter_14

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.KModifier.VARARG
import kotlin.reflect.KClass

class FileSpecBuilder(val builder: FileSpec.Builder) {
    fun clazz(className: String, body: TypeSpecBuilder.() -> Unit) {
        builder.addType(TypeSpecBuilder(TypeSpec.classBuilder(className)).apply(body).builder.build())
    }

    fun function(name: String, body: FunSpecBuilder.() -> Unit) {
        builder.addFunction(FunSpecBuilder(FunSpec.builder(name)).apply(body).builder.build())
    }
}

class TypeSpecBuilder(val builder: TypeSpec.Builder) {

    fun function(name: String, body: FunSpecBuilder.() -> Unit) {
        builder.addFunction(FunSpecBuilder(FunSpec.builder(name)).apply(body).builder.build())
    }

    var primaryConstructor: FunSpec? = null
        set(value) {
            builder.primaryConstructor(value)
        }

    fun constructor(body: FunSpecBuilder.() -> Unit): FunSpec =
        FunSpecBuilder(FunSpec.constructorBuilder()).apply(body).builder.build()

    fun property(name: String, type: KClass<*>, body: PropertySpecBuilder.() -> Unit) {
       builder.addProperty(PropertySpecBuilder(PropertySpec.builder(name, type)).apply(body).builder.build())
    }
}

class FunSpecBuilder(val builder: FunSpec.Builder) {

    fun parameter(name: String, type: KClass<*>, vararg modifiers: KModifier) {
        builder.addParameter(ParameterSpec.builder(name, type, *modifiers).build())
    }

    fun statement(line: String, vararg args: Any) {
        builder.addStatement(line, *args)
    }
}

class PropertySpecBuilder(val builder: PropertySpec.Builder) {
    fun initializer(name: String, vararg args: Any?) {
        builder.initializer(name, *args)
    }
}

fun file(packageName: String, fileName: String, body: FileSpecBuilder.() -> Unit): FileSpec =
    FileSpecBuilder(FileSpec.builder(packageName, fileName)).apply(body).builder.build()


val greeterClass = ClassName("", "Greeter")
val exampleFile = file("", "HelloWorld") {
    clazz("Greeter") {
        primaryConstructor = constructor {
            parameter("name", String::class)
        }
        property("name", String::class) {
            initializer("name")
        }
        function("greet") {
            statement("println(%P)", "Hello, \$name")
        }
    }
    function("main") {
        parameter("args", String::class, VARARG)
        statement("%T(args[0]).greet()", greeterClass)
    }
}

fun main() {
    exampleFile.writeTo(System.out)
}
