package creativeDSLs.chapter_07.autoDsl

//import com.faendir.kotlin.autodsl.AutoDsl

interface JsonElement

object Null : JsonElement
object True : JsonElement
object False : JsonElement

//@AutoDsl
data class JsonString(val value : String) : JsonElement
//@AutoDsl
data class JsonNumber(val value : Double) : JsonElement
//@AutoDsl
data class JsonArray(val elements: List<JsonElement>)
//@AutoDsl
data class JsonDocument(val elements: Map<String, JsonElement>)