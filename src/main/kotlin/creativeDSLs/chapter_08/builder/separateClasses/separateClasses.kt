package creativeDSLs.chapter_08.builder.separateClasses

data class NameWithAlias(val name: String, val alias: String? = null) {
    override fun toString(): String = when (alias) {
        null -> name
        else -> "$name AS $alias"
    }
}

data class TableJoin(val nameWithAlias: NameWithAlias, val column1: String, val column2: String)

data class QueryData(
    val columns: List<String>,
    val tableName: NameWithAlias,
    val joinClauses: List<TableJoin> = emptyList(),
    val whereConditions: List<String> = emptyList(),
    val groupByColumns: List<String> = emptyList()
)

fun select(vararg columns: String) = SelectClause(*columns)

class SelectClause(vararg val columns: String) {

    fun from(tableName: String, alias: String? = null) =
        FromClause(QueryData(columns.asList(), NameWithAlias(tableName, alias)))
}

data class FromClause(val queryData: QueryData) {
    fun join(tableName: String, alias: String? = null) =
        JoinClause(queryData, NameWithAlias(tableName, alias))

    fun where(condition: String) =
        WhereClause(queryData.copy(whereConditions =  listOf(condition)))

    fun groupBy(vararg groupByColumns: String) =
        GroupByClause(queryData.copy(groupByColumns = groupByColumns.toList()))

    fun build() = build(queryData)
}

data class JoinClause(val queryData: QueryData, val tableName: NameWithAlias) {
    fun on(firstColumn: String, secondColumn: String) =
        FromClause(queryData.copy(
            joinClauses = queryData.joinClauses + TableJoin(tableName, firstColumn, secondColumn)
        ))
}

data class WhereClause(val queryData: QueryData) {
    fun and(condition: String) = copy(queryData = queryData.copy(whereConditions = queryData.whereConditions + condition))

    fun groupBy(vararg groupByColumns: String) =
        GroupByClause(queryData.copy(groupByColumns = groupByColumns.toList()))

    fun build() = build(queryData)
}

data class GroupByClause(val queryData: QueryData) {
    fun build() = build(queryData)
}

private fun build(queryData: QueryData): String {
    val (columns, tableName, joinClauses, whereConditions, groupByColumns) = queryData
    val sb = StringBuilder()
        .append("SELECT ${columns.joinToString(", ")}")
        .append("\nFROM ")
        .append(tableName)
    joinClauses.forEach { (n, c1, c2) ->
        sb.append("\n  JOIN $n ON $c1 = $c2")
    }
    if (whereConditions.isNotEmpty()) {
        sb.append("\nWHERE ${whereConditions.joinToString("\n  AND ")}")
    }
    if (groupByColumns.isNotEmpty()) {
        sb.append("\nGROUP BY ${groupByColumns.joinToString(", ")}")
    }
    sb.append(';')
    return sb.toString()
}

fun main() {
    val query = select("a.city", "avg(p.age)", "avg(p.income)")
        .from("Person", "p")
        .join("Address", "a").on("p.addressId", "a.id")
        .where("p.age > 20")
        .and("p.age <= 40")
        .groupBy("a.city")
    println(query.build())
}


