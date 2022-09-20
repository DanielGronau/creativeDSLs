package creativeDSLs.chapter_07.chained

fun SELECT(body: SelectBody.() -> Unit) =
    SelectClause(SelectBody().apply(body).columns)

class SelectBody {
    val columns = mutableListOf<String>()
    operator fun String.unaryPlus() { columns += this }
}

class SelectClause(val columns : List<String>) {
    fun FROM(body: FromBody.() -> Unit) =
        FromBody().apply(body).let{
            FromClause(columns, it.tableName, it.joinClauses)
        }
}

typealias NameWithAlias = Pair<String, String?>

class FromBody {
    var tableName: NameWithAlias = "" to null
    val joinClauses  = mutableListOf<Triple<NameWithAlias, String, String>>()

    operator fun String.unaryPlus() { tableName = this to null }
    infix fun String.AS(that: String) { tableName = this to that }

    fun JOIN(body: JoinBody.() -> Unit) {
        JoinBody().apply(body).also {
            joinClauses += Triple(it.tableName, it.firstColumn, it.secondColumn)
        }
    }
}

data class FromClause(
    val columns: List<String>,
    val tableName: NameWithAlias,
    val joinClauses: List<Triple<NameWithAlias, String, String>>
) {
    fun WHERE(body: WhereBody.() -> Unit) =
        WhereClause(columns, tableName, joinClauses, WhereBody().apply(body).conditions)
    fun build() = build(columns, tableName, joinClauses, listOf())
}

class JoinBody {
    var tableName: NameWithAlias = "" to null
    var firstColumn = ""
    var secondColumn = ""

    operator fun String.unaryPlus() { tableName = this to null }
    infix fun String.AS(that: String) { tableName = this to that }

    fun ON(firstColumn: String, secondColumn: String) {
        this.firstColumn = firstColumn
        this.secondColumn = secondColumn
    }
}

class WhereBody {
    val conditions = mutableListOf<String>()
    operator fun String.unaryPlus() { conditions += this }
}

data class WhereClause(
    val columns: List<String>,
    val tableName: NameWithAlias,
    val joinClauses: List<Triple<NameWithAlias, String, String>>,
    val conditions: List<String>
) {
    fun build() = build(columns, tableName, joinClauses, conditions)
}

private fun build(
    columns: List<String>,
    tableName: NameWithAlias,
    joinClauses: List<Triple<NameWithAlias, String, String>>,
    conditions: List<String>
): String {
    val sb = StringBuilder()
        .append("SELECT ${columns.joinToString(", ") { it }}")
        .append("\nFROM ")
        .append(nameWithAlias(tableName))
    joinClauses.forEach { (n, c1, c2) ->
        sb.append("\n JOIN ${nameWithAlias(n)} ON $c1 = $c2")
    }
    if (conditions.isNotEmpty()) {
        sb.append("\nWHERE ${conditions.joinToString("\n AND ")}")
    }
    sb.append(';')
    return sb.toString()
}

private fun nameWithAlias(name: NameWithAlias) = when (name.second) {
    null -> name.first
    else -> "${name.first} AS ${name.second}"
}

fun main() {
   val query = SELECT {
        +"p.firstName"
        +"p.lastName"
        +"p.income"
    }.FROM {
        "Person" AS "p"
        JOIN {
            "Address" AS "a"
            ON("p.addressId","a.id")
        }
    }.WHERE {
        +"p.age > 20"
        +"p.age <= 40"
        +"a.city = 'London'"
    }
    println(query.build())
}