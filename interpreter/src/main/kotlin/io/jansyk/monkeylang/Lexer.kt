package io.jansyk.monkeylang

internal class Lexer(
    private val input: String,
    private var index: Int = 0,
    private var char: Char? = null,
) {

    fun nextToken(): Token {
        readChar()
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
            return token
        }
        return Token(TokenType.EOF, "")
    }

    private fun readChar() {
        char = peekChar()
        index++
    }

    private fun peekChar(): Char? {
        return if (index + 1 > input.length) {
            null
        } else {
            input[index]
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
        var start = this.index - 1
        while (continuation.invoke(peekChar())) {
            readChar()
        }
        return input.subSequence(start, index).toString()
    }

    private fun skipWhitespace() {
        while (char?.isWhitespace() == true) {
            readChar()
        }
    }

    private fun Char.isWhitespace(): Boolean  {
        return this == ' ' || this == '\t' || this == '\n' || this == '\r'
    }

    private fun Char.isLetter(): Boolean {
        return this in 'a'..'z' || this in 'A'..'Z' || this == '_'
    }

    private fun Char.isNumber(): Boolean {
        return this in '0'..'9'
    }
}
