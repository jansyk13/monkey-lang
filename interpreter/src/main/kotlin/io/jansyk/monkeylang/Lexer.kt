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

    fun nextToken(): Token {
        char?.let { ch ->
            val token = when {
                ch == '=' -> Token(TokenType.ASSIGN, ch)
                ch == ';' -> Token(TokenType.SEMICOLON, ch)
                ch == '(' -> Token(TokenType.LPAREN, ch)
                ch == ')' -> Token(TokenType.RPAREN, ch)
                ch == ',' -> Token(TokenType.COMMA, ch)
                ch == '+' -> Token(TokenType.PLUS, ch)
                ch == '{' -> Token(TokenType.LBRACE, ch)
                ch == '}' -> Token(TokenType.RBRACE, ch)
                ch.isLetter() -> {
                    val literal = readIdentifier()
                    val type = lookupType(literal)
                    Token(type, literal)
                }
                else -> Token(TokenType.ILLEGAL, ch)
            }
            readChar()
            return token
        }
        return Token(TokenType.EOF, "")
    }

    private fun readChar() {
        if (readPosition >= input.length) {
            char = null
        } else {
            char = input[readPosition]
        }
        position = readPosition
        readPosition++
    }

    private fun lookupType(literal: String): TokenType = when(literal) {
        "fn" -> TokenType.FUNCTION
        "let" -> TokenType.LET
        else -> TokenType.IDENT
    }

    private fun readIdentifier(): String {
        var currentPosition = this.position
        while (char?.isLetter() == true) {
            readChar()
        }
        return input.subSequence(currentPosition, position).toString()
    }

    private fun Char.isLetter(): Boolean {
        return this in 'a'..'z' || this in 'A'..'Z' || this == '_'
    }
}
