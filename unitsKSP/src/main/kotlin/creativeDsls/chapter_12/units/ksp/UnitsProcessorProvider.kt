package creativeDsls.chapter_12.units.ksp

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class UnitsProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor = UnitsSymbolProcessor(
        codeGenerator = environment.codeGenerator,
        logger = environment.logger,
        options = environment.options
    )
}