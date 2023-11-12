package creativeDSLs.chapter_09.fen

import creativeDSLs.chapter_09.fen.Piece.*

enum class Piece(val symbol: String) {
    WhitePawn("P"),
    WhiteRook("R"),
    WhiteKnight("N"),
    WhiteBishop("B"),
    WhiteQueen("Q"),
    WhiteKing("K"),
    BlackPawn("p"),
    BlackRook("r"),
    BlackKnight("n"),
    BlackBishop("b"),
    BlackQueen("q"),
    BlackKing("k"),
}

enum class Color(val symbol: String) {
    Black("b"),
    White("w")
}

data class Position(
    val pieces: Map<String, Piece>,
    val toMove: Color,
    val castling: List<Piece>,
    val enPassant: String,
    val fiftyMoves: Int,
    val move: Int
) {

    private fun boardFen() =
        (8 downTo 1).joinToString("/") { row ->
            ('a'..'h').joinToString("") { col ->
                pieces["$col$row"]?.symbol ?: "1"
            }
        }.fold("") { acc, ch ->
            if (acc.isNotEmpty() && acc.last().isDigit() && ch == '1')
                acc.dropLast(1) + (acc.last() + 1)
            else acc + ch
        }

    private fun castlingFen() = when {
        castling.isEmpty() -> "-"
        else -> castling.joinToString("") { it.symbol }
    }

    fun FEN() = "${boardFen()} ${toMove.symbol} " +
            "${castlingFen()} $enPassant $fiftyMoves $move"
}

fun readFEN(fenString: String): Position = fenString
    .split(" ")
    .let { part ->
        Position(
            pieces = getPieces(part[0]),
            toMove = getToMove(part[1]),
            castling = getCastling(part[2]),
            enPassant = part[3],
            fiftyMoves = part[4].toInt(),
            move = part[5].toInt()
        )
    }

private fun getPieces(piecesStr: String) = piecesStr
    .fold("") { acc, ch ->
        acc + if (ch.isDigit()) ".".repeat(ch.toString().toInt()) else ch
    }
    .split("/")
    .reversed()
    .flatMapIndexed { rowIndex, row ->
        row.mapIndexedNotNull { colIndex, ch ->
            values().find { it.symbol == ch.toString() }
                ?.let { "${'a' + colIndex}${rowIndex + 1}" to it }
        }
    }
    .toMap()

private fun getToMove(toMoveStr: String) = when (toMoveStr) {
    "w" -> Color.White
    "b" -> Color.Black
    else -> error("unknown color '$toMoveStr' for player to move")
}

private fun getCastling(castlingStr: String) = castlingStr
    .mapNotNull { ch ->
        when (ch) {
            'K' -> WhiteKing
            'k' -> BlackKing
            'Q' -> WhiteQueen
            'q' -> BlackQueen
            else -> null
        }
    }


fun main() {
    val p = Position(mapOf("a1" to WhiteKing, "a2" to WhitePawn, "c7" to BlackKing), Color.White, listOf(), "-", 0, 42)
    println(p.FEN())

    val q = readFEN("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2")
    println(q)
    println("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2")
    println(q.FEN())
}