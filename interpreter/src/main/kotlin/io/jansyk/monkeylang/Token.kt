package io.jansyk.monkeylang

internal data class Token(
    internal val type: TokenType,
    internal val literal: String,
) {
    constructor( type: TokenType, literal: Char): this(type,literal.toString() ){
    }
}

internal enum class TokenType(internal val value: String) {
    ILLEGAL("ILLEGAL"),
    EOF("EOF"),
    IDENT("IDENT"),
    INT("INT"),
    ASSIGN("="),
    PLUS("+"),
    COMMA(","),
    SEMICOLON(";"),
    LPAREN("("),
    RPAREN(")"),
    LBRACE("{"),
    RBRACE("}"),
    FUNCTION("function"),
    LET("LET")
}
