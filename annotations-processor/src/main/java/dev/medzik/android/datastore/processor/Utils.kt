package dev.medzik.android.datastore.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import kotlin.reflect.KClass

fun Resolver.getSymbolsWithAnnotation(kClass: KClass<*>) = getSymbolsWithAnnotation(kClass.qualifiedName!!)
fun Resolver.getClassesWithAnnotation(kClass: KClass<*>) = getSymbolsWithAnnotation(kClass)
    .filterIsInstance<KSClassDeclaration>()
