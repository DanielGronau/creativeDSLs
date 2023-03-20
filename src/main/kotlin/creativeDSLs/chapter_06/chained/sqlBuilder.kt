package creativeDSLs.chapter_06.chained

fun select(vararg columns: String) = SelectClause(*columns)

class SelectClause(vararg val columns: String) {
    fun from(tableName: String, alias: String? = null) =
        FromClause(columns.asList(), tableName to alias)
}

typealias NameWithAlias = Pair<String, String?>

data class FromClause(
    val columns: List<String>,
    val tableName: NameWithAlias,
    val joinClauses: List<TableJoin> = emptyList()
) {
    fun join(tableName: String, alias: String? = null) =
        JoinClause(this, tableName to alias)

    fun where(condition: String) =
        WhereClause(columns, tableName, joinClauses, listOf(condition))

    fun build() = build(columns, tableName, joinClauses, emptyList())
}

data class JoinClause(val fromClause: FromClause, val tableName: NameWithAlias) {
    fun on(firstColumn: String, secondColumn: String) =
        fromClause.copy(joinClauses =
           fromClause.joinClauses + TableJoin(tableName, firstColumn, secondColumn))
}

data class WhereClause(
    val columns: List<String>,
    val tableName: NameWithAlias,
    val joinClauses: List<TableJoin>,
    val conditions: List<String>
) {
    fun and(condition: String) = copy(conditions = conditions + condition)
    fun build() = build(columns, tableName, joinClauses, conditions)
}

typealias TableJoin = Triple<NameWithAlias, String, String>

private fun build(
    columns: List<String>,
    tableName: NameWithAlias,
    joinClauses: List<TableJoin>,
    conditions: List<String>
): String {
    val sb = StringBuilder()
        .append("SELECT ${columns.joinToString(", ")}")
        .append("\nFROM ")
        .append(nameWithAlias(tableName))
    joinClauses.forEach { (n, c1, c2) ->
        sb.append("\n  JOIN ${nameWithAlias(n)} ON $c1 = $c2")
    }
    if (conditions.isNotEmpty()) {
        sb.append("\nWHERE ${conditions.joinToString("\n  AND ")}")
    }
    sb.append(';')
    return sb.toString()
}

private fun nameWithAlias(name: NameWithAlias) = when (name.second) {
    null -> name.first
    else -> "${name.first} AS ${name.second}"
}

fun main() {
    val query = select("p.firstName", "p.lastName", "p.income")
        .from("Person", "p")
        .join("Address", "a").on("p.addressId","a.id")
        .where("p.age > 20")
        .and("p.age <= 40")
        .and("a.city = 'London'")
    println(query.build())
}