package creativeDSLs.chapter_12.patterns.ksp

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class PatternProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = PatternSymbolProcessor(
        codeGenerator = environment.codeGenerator,
        logger = environment.logger,
        options = environment.options
    )
}