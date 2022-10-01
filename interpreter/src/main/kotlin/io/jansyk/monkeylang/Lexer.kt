package io.jansyk.monkeylang

internal class Lexer(
    private val input: String,
    private var position: Int = 0,
    private var readPosition: Int = 0,
    private var char: Char? = null,
) {
    init {
        readChar()
    }

    fun nextToken(): Token {
        skipWhitespace()
        char?.let { ch ->
            val token = when {
                ch == '=' -> {
                    if (peekChar() == '=') {
                        readChar()
                        Token(TokenType.EQ)
                    } else {
                        Token(TokenType.ASSIGN)
                    }
                }

                ch == ';' -> Token(TokenType.SEMICOLON)
                ch == '(' -> Token(TokenType.LPAREN)
                ch == ')' -> Token(TokenType.RPAREN)
                ch == ',' -> Token(TokenType.COMMA)
                ch == '+' -> Token(TokenType.PLUS)
                ch == '-' -> Token(TokenType.MINUS)
                ch == '!' -> {
                    if (peekChar() == '=') {
                        readChar()
                        Token(TokenType.NOT_EQ)
                    } else {
                        Token(TokenType.BANG)
                    }
                }

                ch == '*' -> Token(TokenType.ASTERISK)
                ch == '/' -> Token(TokenType.SLASH)
                ch == '{' -> Token(TokenType.LBRACE)
                ch == '}' -> Token(TokenType.RBRACE)
                ch == '<' -> Token(TokenType.LT)
                ch == '>' -> Token(TokenType.GT)
                ch.isLetter() -> {
                    val literal = readBlock { c -> c?.isLetter() == true }
                    val type = lookupType(literal)
                    Token(type, literal)
                }

                ch.isNumber() -> {
                    val literal = readBlock { c -> c?.isNumber() == true }
                    val type = TokenType.INT
                    Token(type, literal)
                }

                else ->
                    Token(TokenType.ILLEGAL, ch)
            }
            readChar()
            return token
        }
        return Token(TokenType.EOF, "")
    }

    private fun skipWhitespace() {
        while (char == ' ' || char == '\t' || char == '\n' || char == '\r') {
            readChar()
        }
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

    private fun peekChar(): Char? {
        return if (readPosition >= input.length) {
            null
        } else {
            input[readPosition]
        }
    }

    private fun lookupType(literal: String): TokenType = when (literal) {
        "fn" -> TokenType.FUNCTION
        "let" -> TokenType.LET
        "true" -> TokenType.TRUE
        "false" -> TokenType.FALSE
        "if" -> TokenType.IF
        "else" -> TokenType.ELSE
        "return" -> TokenType.RETURN
        else -> TokenType.IDENT
    }

    private fun readBlock(continuation: (Char?) -> Boolean): String {
        var currentPosition = this.position
        while (continuation.invoke(peekChar())) {
            readChar()
        }
        return input.subSequence(currentPosition, readPosition).toString()
    }

    private fun Char.isLetter(): Boolean {
        return this in 'a'..'z' || this in 'A'..'Z' || this == '_'
    }

    private fun Char.isNumber(): Boolean {
        return this in '0'..'9'
    }
}
