package dev.medzik.android.datastore.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import dev.medzik.android.datastore.DataStore

class Processor(codeGenerator: CodeGenerator) : SymbolProcessor {
    private val preferencesProcessor = PreferencesProcessor(codeGenerator)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val preferencesClasses = resolver.getClassesWithAnnotation(DataStore::class)
        if (!preferencesClasses.iterator().hasNext()) return emptyList()

        for (preferencesClass in preferencesClasses) {
            preferencesProcessor.process(preferencesClass)
        }

        return emptyList()
    }

    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
            return Processor(
                codeGenerator = environment.codeGenerator
            )
        }
    }
}
