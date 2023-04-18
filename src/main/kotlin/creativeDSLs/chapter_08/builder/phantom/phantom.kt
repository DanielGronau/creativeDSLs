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

data class QueryData<out State>(
    val columns: List<String>,
    val tableName: NameWithAlias = NameWithAlias(""),
    val joinTableName: NameWithAlias = NameWithAlias(""),
    val joinClauses: List<TableJoin> = emptyList(),
    val whereConditions: List<String> = emptyList(),
    val groupByColumns: List<String> = emptyList()
)

fun select(vararg columns: String) = QueryData<SelectClause>(columns = columns.toList())

@Suppress("UNCHECKED_CAST")
private fun <S : State> QueryData<*>.cast(): QueryData<S> = this as QueryData<S>

fun QueryData<SelectClause>.from(table: String, alias: String?): QueryData<FromClause> =
    copy(tableName = NameWithAlias(table, alias)).cast()

fun QueryData<FromClause>.join(tableName: String, alias: String?): QueryData<JoinClause> =
    copy(joinTableName = NameWithAlias(tableName, alias)).cast()

fun QueryData<FromClause>.where(condition: String): QueryData<WhereClause> =
    copy(whereConditions = whereConditions + condition).cast()

fun QueryData<JoinClause>.on(firstColumn: String, secondColumn: String): QueryData<FromClause> =
    copy(joinClauses = joinClauses + TableJoin(joinTableName, firstColumn, secondColumn)).cast()

fun QueryData<WhereClause>.and(condition: String): QueryData<WhereClause> =
    copy(whereConditions = whereConditions + condition)

fun QueryData<CanGroupBy>.groupBy(vararg groupByColumns: String): QueryData<GroupByClause> =
    copy(groupByColumns = groupByColumns.toList()).cast()

fun QueryData<CanBuild>.build(): String {
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
    val query = select("p.firstName", "p.lastName", "p.income")
        .from("Person", "p")
        .join("Address", "a").on("p.addressId", "a.id")
        .where("p.age > 20")
        .and("p.age <= 40")
        .and("a.city = 'London'")
        .groupBy("a.city")
    println(query.build())
}


