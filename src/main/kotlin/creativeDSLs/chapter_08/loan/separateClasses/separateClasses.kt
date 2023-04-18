package creativeDSLs.chapter_08.loan.separateClasses

data class NameWithAlias(val name: String, val alias: String? = null) {
    override fun toString(): String = when (alias) {
        null -> name
        else -> "$name AS $alias"
    }
}

data class TableJoin(val nameWithAlias: NameWithAlias, val column1: String, val column2: String)

data class QueryData(
    val columns: List<String>,
    val tableName: NameWithAlias = NameWithAlias(""),
    val joinClauses: List<TableJoin> = emptyList(),
    val whereConditions: List<String> = emptyList(),
    val groupByColumns: List<String> = emptyList()
)

fun SELECT(body: SelectBody.() -> Unit) =
    SelectClause(QueryData(columns = SelectBody().apply(body).columns))

class SelectBody {
    val columns = mutableListOf<String>()
    operator fun String.unaryPlus() { columns += this }
}

class SelectClause(val queryData: QueryData) {
    fun FROM(body: FromBody.() -> Unit) =
        FromBody().apply(body).let{
            FromClause(queryData.copy(tableName = it.tableName, joinClauses = it.joinClauses))
        }
}

class FromBody {
    var tableName = NameWithAlias("")
    val joinClauses  = mutableListOf<TableJoin>()

    operator fun String.unaryPlus() { tableName = NameWithAlias(this) }
    infix fun String.AS(alias: String) { tableName = NameWithAlias(this, alias) }

    fun JOIN(body: JoinBody.() -> Unit) {
        JoinBody().apply(body).also {
            joinClauses += TableJoin(it.tableName, it.firstColumn, it.secondColumn)
        }
    }
}

data class FromClause(val queryData: QueryData) {
    fun WHERE(body: WhereBody.() -> Unit) =
        WhereClause(queryData.copy(whereConditions = WhereBody().apply(body).conditions))
    fun GROUP_BY(body: GroupByBody.() -> Unit) =
        GroupByClause(queryData.copy(groupByColumns = GroupByBody().apply(body).columns))
    fun build() = build(queryData)
}

class JoinBody {
    var tableName = NameWithAlias("")
    var firstColumn = ""
    var secondColumn = ""

    operator fun String.unaryPlus() { tableName = NameWithAlias(this) }
    infix fun String.AS(alias: String) { tableName = NameWithAlias(this, alias) }

    fun ON(firstColumn: String, secondColumn: String) {
        this.firstColumn = firstColumn
        this.secondColumn = secondColumn
    }
}

class WhereBody {
    val conditions = mutableListOf<String>()
    operator fun String.unaryPlus() { conditions += this }
}

data class WhereClause(val queryData: QueryData) {
    fun GROUP_BY(body: GroupByBody.() -> Unit) =
        GroupByClause(queryData.copy(groupByColumns = GroupByBody().apply(body).columns))
    fun build() = build(queryData)
}

class GroupByBody {
    val columns = mutableListOf<String>()
    operator fun String.unaryPlus() { columns += this }
}

data class GroupByClause(val queryData: QueryData) {
    fun build() = build(queryData)
}

private fun build(queryData: QueryData): String {
    val (columns, tableName, joinClauses, conditions, groupByColumns) = queryData
    val sb = StringBuilder()
        .append("SELECT ${columns.joinToString(", ")}")
        .append("\nFROM ")
        .append(tableName)
    joinClauses.forEach { (n, c1, c2) ->
        sb.append("\n  JOIN $n ON $c1 = $c2")
    }
    if (conditions.isNotEmpty()) {
        sb.append("\nWHERE ${conditions.joinToString("\n  AND ")}")
    }
    if (groupByColumns.isNotEmpty()) {
        sb.append("\nGROUP BY ${groupByColumns.joinToString(", ")}")
    }
    sb.append(';')
    return sb.toString()
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
    }.GROUP_BY {
        +"a.country"
        +"p.age"
    }
    println(query.build())
}