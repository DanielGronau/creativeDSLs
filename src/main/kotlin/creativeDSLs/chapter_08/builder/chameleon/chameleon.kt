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
        fun select(vararg columns: String): SelectClause = QueryBuilder(columns.asList())
    }

    // SelectClause
    override fun from(table: String, alias: String?): FromClause =
        this.apply { tableName = NameWithAlias(table, alias) }

    // FromClause
    override fun join(tableName: String, alias: String?): JoinClause =
        this.apply { joinTableName = NameWithAlias(tableName, alias) }

    override fun where(condition: String): WhereClause =
        this.apply { whereConditions += condition }

    // JoinClause
    override fun on(firstColumn: String, secondColumn: String): FromClause =
        this.apply { joinClauses += TableJoin(joinTableName, firstColumn, secondColumn) }

    // WhereClause
    override fun and(condition: String): WhereClause =
        this.apply { whereConditions += condition }

    // FromClause and WhereClause
    override fun groupBy(vararg groupByColumns: String): GroupByClause =
        this.apply { this.groupByColumns += groupByColumns.toList() }

    // FromClause, WhereClause and GroupByClause
    override fun build(): String {
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
}

interface SelectClause {
    fun from(table: String, alias: String? = null): FromClause
}

interface FromClause{
    fun join(tableName: String, alias: String? = null): JoinClause
    fun where(condition: String): WhereClause
    fun groupBy(vararg groupByColumns: String): GroupByClause
    fun build(): String
}

interface JoinClause {
    fun on(firstColumn: String, secondColumn: String): FromClause
}

interface WhereClause {
    fun and(condition: String): WhereClause
    fun groupBy(vararg groupByColumns: String): GroupByClause
    fun build(): String
}

interface GroupByClause {
    fun build(): String
}

fun main() {
    val query = QueryBuilder.select("p.firstName", "p.lastName", "p.income")
        .from("Person", "p")
        .join("Address", "a").on("p.addressId","a.id")
        .where("p.age > 20")
        .and("p.age <= 40")
        .and("a.city = 'London'")
        .groupBy("a.city")
    println(query.build())
}