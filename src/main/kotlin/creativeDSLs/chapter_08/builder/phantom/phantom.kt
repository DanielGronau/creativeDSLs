package creativeDSLs.chapter_08.builder.phantom

data class NameWithAlias(val name: String, val alias: String? = null) {
    override fun toString(): String = when (alias) {
        null -> name
        else -> "$name AS $alias"
    }
}

data class TableJoin(val nameWithAlias: NameWithAlias, val column1: String, val column2: String)

interface CanGroupBy
interface CanBuild

sealed interface State
interface SelectClause : State
interface FromClause : State, CanGroupBy, CanBuild
interface JoinClause : State
interface WhereClause : State, CanGroupBy, CanBuild
interface GroupByClause : State, CanBuild

data class QueryDTO<out State>(
    val columns: List<String>,
    val tableName: NameWithAlias = NameWithAlias(""),
    val joinTableName: NameWithAlias = NameWithAlias(""),
    val joinClauses: List<TableJoin> = emptyList(),
    val whereConditions: List<String> = emptyList(),
    val groupByColumns: List<String> = emptyList()
)

fun SELECT(vararg columns: String) = QueryDTO<SelectClause>(columns = columns.toList())

@Suppress("UNCHECKED_CAST")
private fun <S : State> QueryDTO<*>.cast(): QueryDTO<S> = this as QueryDTO<S>

fun QueryDTO<SelectClause>.FROM(table: String, alias: String?): QueryDTO<FromClause> =
    copy(tableName = NameWithAlias(table, alias)).cast()

fun QueryDTO<FromClause>.JOIN(tableName: String, alias: String?): QueryDTO<JoinClause> =
    copy(joinTableName = NameWithAlias(tableName, alias)).cast()

fun QueryDTO<FromClause>.WHERE(condition: String): QueryDTO<WhereClause> =
    copy(whereConditions = whereConditions + condition).cast()

fun QueryDTO<JoinClause>.ON(firstColumn: String, secondColumn: String): QueryDTO<FromClause> =
    copy(joinClauses = joinClauses + TableJoin(joinTableName, firstColumn, secondColumn)).cast()

fun QueryDTO<WhereClause>.AND(condition: String): QueryDTO<WhereClause> =
    copy(whereConditions = whereConditions + condition)

fun QueryDTO<CanGroupBy>.GROUP_BY(vararg groupByColumns: String): QueryDTO<GroupByClause> =
    copy(groupByColumns = groupByColumns.toList()).cast()

fun QueryDTO<CanBuild>.build(): String = with(StringBuilder()) {

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
    val query = SELECT("p.firstName", "p.lastName", "p.income")
        .FROM("Person", "p")
        .JOIN("Address", "a").ON("p.addressId", "a.id")
        .WHERE("p.age > 20")
        .AND("p.age <= 40")
        .AND("a.city = 'London'")
        .GROUP_BY("a.city")
    println(query.build())
}


