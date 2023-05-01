package creativeDSLs.chapter_08.builder.chameleon

data class NameWithAlias(val name: String, val alias: String? = null) {
    override fun toString(): String = when (alias) {
        null -> name
        else -> "$name AS $alias"
    }
}

data class TableJoin(val nameWithAlias: NameWithAlias, val column1: String, val column2: String)

class QueryBuilder private constructor(val columns: List<String>):
    SelectClause, FromClause, JoinClause, WhereClause, GroupByClause {
    var tableName = NameWithAlias("", null)
    var joinTableName = NameWithAlias("", null)
    val joinClauses = mutableListOf<TableJoin>()
    val whereConditions = mutableListOf<String>()
    val groupByColumns = mutableListOf<String>()

    companion object {
        fun SELECT(vararg columns: String): SelectClause = QueryBuilder(columns.asList())
    }

    // SelectClause
    override fun FROM(table: String, alias: String?): FromClause =
        this.apply { tableName = NameWithAlias(table, alias) }

    // FromClause
    override fun JOIN(tableName: String, alias: String?): JoinClause =
        this.apply { joinTableName = NameWithAlias(tableName, alias) }

    override fun WHERE(condition: String): WhereClause =
        this.apply { whereConditions += condition }

    // JoinClause
    override fun ON(firstColumn: String, secondColumn: String): FromClause =
        this.apply { joinClauses += TableJoin(joinTableName, firstColumn, secondColumn) }

    // WhereClause
    override fun AND(condition: String): WhereClause =
        this.apply { whereConditions += condition }

    // FromClause and WhereClause
    override fun GROUP_BY(vararg groupByColumns: String): GroupByClause =
        this.apply { this.groupByColumns += groupByColumns.toList() }

    // FromClause, WhereClause and GroupByClause
    override fun build(): String = with(StringBuilder()) {

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
}

interface SelectClause {
    fun FROM(table: String, alias: String? = null): FromClause
}

interface FromClause{
    fun JOIN(tableName: String, alias: String? = null): JoinClause
    fun WHERE(condition: String): WhereClause
    fun GROUP_BY(vararg groupByColumns: String): GroupByClause
    fun build(): String
}

interface JoinClause {
    fun ON(firstColumn: String, secondColumn: String): FromClause
}

interface WhereClause {
    fun AND(condition: String): WhereClause
    fun GROUP_BY(vararg groupByColumns: String): GroupByClause
    fun build(): String
}

interface GroupByClause {
    fun build(): String
}

fun main() {
    val query = QueryBuilder.SELECT("p.firstName", "p.lastName", "p.income")
        .FROM("Person", "p")
        .JOIN("Address", "a").ON("p.addressId","a.id")
        .WHERE("p.age > 20")
        .AND("p.age <= 40")
        .AND("a.city = 'London'")
        .GROUP_BY("a.city")
    println(query.build())
}