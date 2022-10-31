package creativeDSLs.chapter_07.chameleon

fun SELECT(body: ListBody.() -> Unit): SelectClause =
    QueryBuilder().select(body)

private class QueryBuilder : SelectClause, FromBody, FromClause, WhereClause {
    private val columns = mutableListOf<String>()
    private val conditions = mutableListOf<String>()

    fun select(body: ListBody.() -> Unit) = this.apply { ListBody(columns).apply(body) }

    //SelectClause
    override fun FROM(body: FromBody.() -> Unit) =
        apply(body)

    //FromBody
    override var tableName: NameWithAlias = "" to null
    override val joinClauses = mutableListOf<Triple<NameWithAlias, String, String>>()
    override infix fun String.AS(that: String) {
        tableName = this to that
    }

    override fun JOIN(body: JoinBody.() -> Unit) =
        JoinBody().apply(body).also {
            joinClauses += Triple(it.tableName, it.firstColumn, it.secondColumn)
        }

    //FromClause
    override fun WHERE(body: ListBody.() -> Unit) =
        this.apply { ListBody(conditions).apply(body) }

    //FromClause and WhereClause
    override fun build() = build(columns, tableName, joinClauses, conditions)
}

class ListBody(val list:MutableList<String>) {
    operator fun String.unaryPlus() {
        list += this
    }
}

interface SelectClause {
    fun FROM(body: FromBody.() -> Unit): FromClause
}

typealias NameWithAlias = Pair<String, String?>

interface FromBody {
    var tableName: NameWithAlias
    val joinClauses: List<Triple<NameWithAlias, String, String>>
    infix fun String.AS(that: String)
    fun JOIN(body: JoinBody.() -> Unit): JoinBody
}

interface FromClause {
    fun WHERE(body: ListBody.() -> Unit): WhereClause
    fun build(): String
}

class JoinBody {
    var tableName: NameWithAlias = "" to null
    var firstColumn = ""
    var secondColumn = ""

    operator fun String.unaryPlus() {
        tableName = this to null
    }

    infix fun String.AS(that: String) {
        tableName = this to that
    }

    fun ON(firstColumn: String, secondColumn: String) {
        this.firstColumn = firstColumn
        this.secondColumn = secondColumn
    }
}

interface WhereClause {
    fun build(): String
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
    val query = SELECT {
        + "p.firstName"
        + "p.lastName"
        + "p.income"
    }.FROM {
        "Person" AS "p"
        JOIN {
            "Address" AS "a"
            ON("p.addressId", "a.id")
        }
    }.WHERE {
        + "p.age > 20"
        + "p.age <= 40"
        + "a.city = 'London'"
    }
    println(query.build())
}