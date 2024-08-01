package dev.medzik.android.datastore.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import dev.medzik.android.datastore.DataStore
import kotlinx.serialization.Serializable
import java.io.InputStream
import java.io.OutputStream

class PreferencesProcessor(private val codeGenerator: CodeGenerator) {
    private val androidContext = ClassName("android.content", "Context")

    fun process(clazz: KSClassDeclaration) {
        val className = clazz.toClassName()

        // check if class is annotated with @Serializable
        annotatedSerializable(clazz)

        FileSpec.builder(className.packageName, "${className.simpleName}Generated")
            .addFileComment("Generated code for ${className.simpleName} class")
            .addType(
                TypeSpec.objectBuilder("${className.simpleName}Generated")
                    .addSerializer(className)
                    .addDatastore(clazz)
                    .addReadFunction(className)
                    .addWriteFunction(className)
                    .build()
            )
            .build()
            .writeTo(codeGenerator, Dependencies(false))
    }

    private fun annotatedSerializable(clazz: KSClassDeclaration) {
        val serializationAnnotation = clazz.annotations
            .filter { it.annotationType.toTypeName() == Serializable::class.asTypeName() }
        if (!serializationAnnotation.iterator().hasNext())
            throw IllegalArgumentException("Missing @Serializable annotation for ${clazz.packageName}.${clazz.simpleName} class")
    }

    private fun getDataStoreName(clazz: KSClassDeclaration) = clazz.annotations
        .first { it.annotationType.toTypeName() == DataStore::class.asTypeName() }
        .arguments
        .first()
        .value as String

    private fun TypeSpec.Builder.addSerializer(className: ClassName) = addType(
        TypeSpec.objectBuilder("Serializer")
            .addKdoc("Serializer for the [$className] datastore.")
            .addModifiers(KModifier.PRIVATE)
            .addSuperinterface(
                ClassName("androidx.datastore.core", "Serializer").parameterizedBy(
                    className
                )
            )
            .addProperty(
                PropertySpec.builder("defaultValue", className)
                    .addModifiers(KModifier.OVERRIDE)
                    .getter(
                        FunSpec.getterBuilder()
                            .addStatement("return %T()", className)
                            .build()
                    )
                    .build()
            )
            .addFunction(
                FunSpec.builder("readFrom")
                    .addAnnotation(
                        AnnotationSpec.builder(ClassName("kotlin", "OptIn"))
                            .addMember(
                                "%M::class",
                                MemberName("kotlinx.serialization", "ExperimentalSerializationApi")
                            )
                            .build()
                    )
                    .addModifiers(KModifier.OVERRIDE, KModifier.SUSPEND)
                    .addParameter("input", InputStream::class)
                    .addStatement(
                        "return %M.%M(input)",
                        MemberName("kotlinx.serialization.json", "Json"),
                        MemberName("kotlinx.serialization.json", "decodeFromStream")
                    )
                    .returns(className)
                    .build()
            )
            .addFunction(
                FunSpec.builder("writeTo")
                    .addAnnotation(
                        AnnotationSpec.builder(ClassName("kotlin", "OptIn"))
                            .addMember(
                                "%M::class",
                                MemberName("kotlinx.serialization", "ExperimentalSerializationApi")
                            )
                            .build()
                    )
                    .addModifiers(KModifier.OVERRIDE, KModifier.SUSPEND)
                    .addParameter("t", className)
                    .addParameter("output", OutputStream::class)
                    .addStatement(
                        "%M.%M(t, output)",
                        MemberName("kotlinx.serialization.json", "Json"),
                        MemberName("kotlinx.serialization.json", "encodeToStream")
                    )
                    .build()
            )
            .build()
    )

    private fun TypeSpec.Builder.addDatastore(clazz: KSClassDeclaration): TypeSpec.Builder {
        val type = ClassName("androidx.datastore.core", "DataStore")
            .parameterizedBy(clazz.toClassName())

        return addProperty(
            PropertySpec.builder("dataStore", type)
                .addModifiers(KModifier.PRIVATE)
                .receiver(androidContext)
                .delegate(
                    "%M(%S, Serializer)",
                    MemberName("androidx.datastore", "dataStore"),
                    getDataStoreName(clazz)
                )
                .build()
        )
    }

    private fun TypeSpec.Builder.addReadFunction(className: ClassName) = addFunction(
        FunSpec.builder("readFrom${className.simpleName}")
            .addKdoc("Reads data from the [$className] datastore.")
            .receiver(androidContext)
            .addStatement("return dataStore.data")
            .returns(
                ClassName("kotlinx.coroutines.flow", "Flow")
                    .parameterizedBy(
                        className
                    )
            )
            .build()
    )

    private fun TypeSpec.Builder.addWriteFunction(className: ClassName) = addFunction(
        FunSpec.builder("writeTo${className.simpleName}")
            .addKdoc("Writes data to the [$className] datastore.")
            .receiver(androidContext)
            .addParameter("t", className)
            .addModifiers(KModifier.SUSPEND)
            .addStatement("return dataStore.updateData { t }")
            .returns(className)
            .build()
    )
}
