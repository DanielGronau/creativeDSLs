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
    private fun piecesFen() =
        (8 downTo 1).map { row ->
            var count = 0
            ('a'..'h').map { col ->
                val piece = pieces["$col$row"]
                when {
                    piece != null && count == 0 -> piece.symbol
                    piece != null && count != 0 ->
                        "$count${piece.symbol}".also { count = 0 }

                    col == 'h' -> "${count + 1}"
                    else -> "".also { count++ }
                }
            }.joinToString("")
        }.joinToString("/")

    private fun castlingFen() = when {
        castling.isEmpty() -> "-"
        else -> castling.map { it.symbol }.joinToString("")
    }

    fun FEN() = "${piecesFen()} ${toMove.symbol} " +
            "${castlingFen()} $enPassant $fiftyMoves $move"
}

fun readFEN(s: String): Position {
    operator fun <E> List<E>.component6(): E = this[5]
    val (piecesStr,
        toMoveStr,
        castlingStr,
        enPassantStr,
        fiftyMovesStr,
        movesStr) = s.split(" ")
    val pieces = mutableMapOf<String, Piece>()
    piecesStr.split("/").mapIndexed { index, row ->
        var count = 0
        row.forEach { ch ->
            when {
                ch.isDigit() -> count += ch.toString().toInt()
                else -> pieces["${"abcdefgh"[count]}${8 - index}"] =
                    Piece.values().find {
                        it.symbol == ch.toString()
                    }!!.also { count++ }
            }
        }
    }
    val toMove = Color.values().find { it.symbol == toMoveStr }!!
    val castling = when (castlingStr) {
        "-" -> emptyList()
        else -> castlingStr
            .map { ch ->
                Piece.values().find {
                    it.symbol == ch.toString()
                }!!
            }
    }
    return Position(
        pieces,
        toMove,
        castling,
        enPassantStr,
        fiftyMovesStr.toInt(),
        movesStr.toInt()
    )
}


fun main() {
    val p = Position(mapOf("a1" to WhiteKing, "a2" to WhitePawn, "c7" to BlackKing), Color.White, listOf(), "-", 0, 42)
    println(p.FEN())

    val q = readFEN("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2")
    println(q)
    println("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2")
    println(q.FEN())
}