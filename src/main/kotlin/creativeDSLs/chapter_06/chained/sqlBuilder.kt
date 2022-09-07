package creativeDSLs.chapter_06.chained

object SqlQuery {
    fun select(vararg columns: String) = SelectClause(*columns)
}

class SelectClause(vararg val columns: String) {
    fun from(tableName: String) =
        FromClause(columns.asList(), tableName to null)
    fun from(tableName: String, alias: String) =
        FromClause(columns.asList(), tableName to alias)
}

typealias NameWithAlias = Pair<String, String?>

data class FromClause(
    val columns: List<String>,
    val tableName: NameWithAlias,
    val joinClauses: List<Triple<NameWithAlias, String, String>> = listOf()
) {
    fun join(tableName: String) =
        JoinClause(this, tableName to null)
    fun join(tableName: String, alias: String) =
        JoinClause(this, tableName to alias)

    fun where(condition: String) =
        WhereClause(columns, tableName, joinClauses, listOf(condition))

    fun build() = build(columns, tableName, joinClauses, listOf())
}

data class JoinClause(val fromClause: FromClause, val tableName: NameWithAlias) {
    fun on(firstColumn: String, secondColumn: String) =
        fromClause.copy(joinClauses =
           fromClause.joinClauses + Triple(tableName, firstColumn, secondColumn))
}

data class WhereClause(
    val columns: List<String>,
    val tableName: NameWithAlias,
    val joinClauses: List<Triple<NameWithAlias, String, String>>,
    val conditions: List<String>
) {
    fun and(condition: String) = copy(conditions = conditions + condition)
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
    val query = SqlQuery
        .select("p.firstName", "p.lastName", "p.income")
        .from("Person", "p")
        .join("Address", "a").on("p.addressId","a.id")
        .where("p.age > 20")
        .and("p.age <= 40")
        .and("a.city = 'London'")
    println(query.build())
}