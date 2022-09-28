package io.jansyk.monkeylang

internal class Lexer(
    internal val input: String,
    internal var position: Int = 0,
    internal var readPosition: Int = 0,
    internal var char: Char? = null,
) {
    init {
        readChar()
    }

    internal fun readChar() {
        if (readPosition >= input.length) {
            char = null
        } else {
            char = input[readPosition]
        }
        position = readPosition
        readPosition++
    }

    fun nextToken(): Token {
        char.let {
            val token = when(it) {
                '=' -> Token(TokenType.ASSIGN, it)
                ';' -> Token(TokenType.SEMICOLON, it)
                '(' -> Token(TokenType.LPAREN, it)
                ')' -> Token(TokenType.RPAREN, it)
                ',' -> Token(TokenType.COMMA, it)
                '+' -> Token(TokenType.PLUS, it)
                '{' -> Token(TokenType.LBRACE, it)
                '}' -> Token(TokenType.RBRACE, it)
                else -> Token(TokenType.EOF, "")
            }
            readChar()
            return token
        }
    }
}