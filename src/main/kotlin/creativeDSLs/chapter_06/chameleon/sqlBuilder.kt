package creativeDSLs.chapter_06.chameleon

fun select(vararg columns: String): SelectClause = QueryBuilder(columns.asList())

class QueryBuilder(val columns: List<String>): SelectClause, FromClause, JoinClause, WhereClause {
    var tableName : NameWithAlias = "" to null
    var joinTableName : NameWithAlias = "" to null
    val joinClauses = mutableListOf<Triple<NameWithAlias, String, String>>()
    val conditions = mutableListOf<String>()

    //SelectClause
    override fun from(table: String): FromClause =
        this.apply { tableName = table to null }
    override fun from(table: String, alias: String): FromClause =
        this.apply { tableName = table to alias }
    //FromClause
    override fun join(table: String): JoinClause =
        this.apply { joinTableName = table to null }
    override fun join(table: String, alias: String): JoinClause =
        this.apply { joinTableName = table to alias }
    override fun where(condition: String): WhereClause =
        this.apply { conditions += condition }
    //JoinClause
    override fun on(firstColumn: String, secondColumn: String): FromClause =
        this.apply { joinClauses += Triple(joinTableName, firstColumn, secondColumn) }
    //WhereClause
    override fun and(condition: String): WhereClause =
        this.apply { conditions += condition }
    //FromClause and WhereClause
    override fun build(): String {
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
}

interface SelectClause {
    fun from(table: String): FromClause
    fun from(table: String, alias: String): FromClause
}

typealias NameWithAlias = Pair<String, String?>

interface FromClause{
    fun join(tableName: String): JoinClause
    fun join(tableName: String, alias: String): JoinClause
    fun where(condition: String): WhereClause
    fun build(): String
}

interface JoinClause {
    fun on(firstColumn: String, secondColumn: String): FromClause
}

interface WhereClause {
    fun and(condition: String): WhereClause
    fun build(): String
}

fun main() {
    val query = select("p.firstName", "p.lastName", "p.income")
        .from("Person", "p")
        .join("Address", "a").on("p.addressId","a.id")
        .where("p.age > 20")
        .and("p.age <= 40")
        .and("a.city = 'London'")
    println(query.build())
}