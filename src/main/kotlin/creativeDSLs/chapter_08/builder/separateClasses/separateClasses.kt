package creativeDSLs.chapter_08.builder.separateClasses

data class NameWithAlias(val name: String, val alias: String? = null) {
    override fun toString(): String = when (alias) {
        null -> name
        else -> "$name AS $alias"
    }
}

data class TableJoin(val nameWithAlias: NameWithAlias, val column1: String, val column2: String)

data class QueryDTO(
    val columns: List<String>,
    val tableName: NameWithAlias,
    val joinClauses: List<TableJoin> = emptyList(),
    val whereConditions: List<String> = emptyList(),
    val groupByColumns: List<String> = emptyList()
)

fun SELECT(vararg columns: String) = SelectClause(*columns)

class SelectClause(vararg val columns: String) {

    fun FROM(tableName: String, alias: String? = null) =
        FromClause(QueryDTO(columns.asList(), NameWithAlias(tableName, alias)))
}

data class FromClause(val queryDTO: QueryDTO) {

    fun JOIN(tableName: String, alias: String? = null) =
        JoinClause(queryDTO, NameWithAlias(tableName, alias))

    fun WHERE(condition: String) =
        WhereClause(queryDTO.copy(whereConditions =  listOf(condition)))

    fun GROUP_BY(vararg groupByColumns: String) =
        GroupByClause(queryDTO.copy(groupByColumns = groupByColumns.toList()))

    fun build() = build(queryDTO)
}

data class JoinClause(val queryDTO: QueryDTO, val tableName: NameWithAlias) {

    fun ON(firstColumn: String, secondColumn: String) =
        FromClause(queryDTO.copy(
            joinClauses = queryDTO.joinClauses + TableJoin(tableName, firstColumn, secondColumn)
        ))
}

data class WhereClause(val queryDTO: QueryDTO) {

    fun AND(condition: String) = copy(queryDTO = queryDTO.copy(whereConditions = queryDTO.whereConditions + condition))

    fun GROUP_BY(vararg groupByColumns: String) =
        GroupByClause(queryDTO.copy(groupByColumns = groupByColumns.toList()))

    fun build() = build(queryDTO)
}

data class GroupByClause(val queryDTO: QueryDTO) {

    fun build() = build(queryDTO)
}

private fun build(queryDTO: QueryDTO): String = with(StringBuilder()){

    val (columns, tableName, joinClauses, whereConditions, groupByColumns) = queryDTO

        append("SELECT ${columns.joinToString(", ")}")
        append("\nFROM $tableName")

    joinClauses.forEach { (n, c1, c2) ->
        append("\n  JOIN $n ON $c1 = $c2")
    }

    if (whereConditions.isNotEmpty())
        append("\nWHERE ${whereConditions.joinToString("\n  AND ")}")

    if (groupByColumns.isNotEmpty())
        append("\nGROUP BY ${groupByColumns.joinToString(", ")}")

    append(';')
}.toString()

fun main() {
    val query = SELECT("a.city", "avg(p.age)", "avg(p.income)")
        .FROM("Person", "p")
        .JOIN("Address", "a").ON("p.addressId", "a.id")
        .WHERE("p.age > 20")
        .AND("p.age <= 40")
        .GROUP_BY("a.city")
    println(query.build())
}


