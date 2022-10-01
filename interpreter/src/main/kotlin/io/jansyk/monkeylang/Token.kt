package io.jansyk.monkeylang

internal data class Token(
    internal val type: TokenType,
    internal val literal: String,
) {
    constructor(type: TokenType, literal: Char) : this(type, literal.toString())
    constructor(type: TokenType) : this(type, type.value)
}

internal enum class TokenType(internal val value: String) {
    ASSIGN("="),
    PLUS("+"),
    MINUS("-"),
    BANG("!"),
    ASTERISK("*"),
    SLASH("/"),
    LT("<"),
    GT(">"),
    EQ("=="),
    NOT_EQ("!="),
    COMMA(","),
    SEMICOLON(";"),

    LPAREN("("),
    RPAREN(")"),
    LBRACE("{"),
    RBRACE("}"),

    INT("INT"),
    ILLEGAL("ILLEGAL"),
    EOF("EOF"),
    IDENT("IDENT"),
    FUNCTION("FUNCTION"),
    LET("LET"),
    TRUE("TRUE"),
    FALSE("FALSE"),
    IF("IF"),
    ELSE("ELSE"),
    RETURN("RETURN"),
}
