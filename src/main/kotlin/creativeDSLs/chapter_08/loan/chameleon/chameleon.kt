package creativeDSLs.chapter_08.loan.chameleon

data class NameWithAlias(val name: String, val alias: String? = null) {
    override fun toString(): String = when (alias) {
        null -> name
        else -> "$name AS $alias"
    }
}

data class TableJoin(val nameWithAlias: NameWithAlias, val column1: String, val column2: String)

interface SelectClause {
    fun FROM(body: FromBody.() -> Unit): FromClause
}

interface FromClause {
    fun WHERE(body: WhereBody.() -> Unit): WhereClause
    fun GROUP_BY(body: GroupByBody.() -> Unit): GroupByClause
    fun build(): String
}

interface WhereClause {
    fun GROUP_BY(body: GroupByBody.() -> Unit): GroupByClause
    fun build(): String
}

interface GroupByClause {
    fun build(): String
}


class SelectBody {
    val columns = mutableListOf<String>()
    operator fun String.unaryPlus() {
        columns += this
    }
}

class FromBody {
    var tableName = NameWithAlias("")
    val joinClauses = mutableListOf<TableJoin>()

    operator fun String.unaryPlus() {
        tableName = NameWithAlias(this)
    }

    infix fun String.AS(alias: String) {
        tableName = NameWithAlias(this, alias)
    }

    fun JOIN(body: JoinBody.() -> Unit) {
        JoinBody().apply(body).also {
            joinClauses += TableJoin(it.tableName, it.firstColumn, it.secondColumn)
        }
    }
}

class JoinBody {
    var tableName = NameWithAlias("")
    var firstColumn = ""
    var secondColumn = ""

    operator fun String.unaryPlus() {
        tableName = NameWithAlias(this)
    }

    infix fun String.AS(alias: String) {
        tableName = NameWithAlias(this, alias)
    }

    fun ON(firstColumn: String, secondColumn: String) {
        this.firstColumn = firstColumn
        this.secondColumn = secondColumn
    }
}

class WhereBody {
    val conditions = mutableListOf<String>()
    operator fun String.unaryPlus() {
        conditions += this
    }
}

class GroupByBody {
    val columns = mutableListOf<String>()
    operator fun String.unaryPlus() {
        columns += this
    }
}

data class QueryBuilder private constructor(val columns: List<String>) :
    SelectClause, FromClause, WhereClause, GroupByClause {
    var tableName = NameWithAlias("")
    val joinClauses = mutableListOf<TableJoin>()
    val whereConditions = mutableListOf<String>()
    val groupByColumns = mutableListOf<String>()

    companion object {
        fun SELECT(body: SelectBody.() -> Unit): SelectClause =
            QueryBuilder(columns = SelectBody().apply(body).columns)
    }

    override fun FROM(body: FromBody.() -> Unit): FromClause =
        this.apply {
            val fromBody = FromBody().apply(body)
            tableName = fromBody.tableName
            joinClauses += fromBody.joinClauses
        }

    override fun WHERE(body: WhereBody.() -> Unit): WhereClause =
        this.apply {
            whereConditions += WhereBody().apply(body).conditions
        }

    override fun GROUP_BY(body: GroupByBody.() -> Unit): GroupByClause =
        this.apply {
            groupByColumns += GroupByBody().apply(body).columns
        }

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

fun main() {
    val query = QueryBuilder.SELECT {
        +"p.firstName"
        +"p.lastName"
        +"p.income"
    }.FROM {
        "Person" AS "p"
        JOIN {
            "Address" AS "a"
            ON("p.addressId", "a.id")
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