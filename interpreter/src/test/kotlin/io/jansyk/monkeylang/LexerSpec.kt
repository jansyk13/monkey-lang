package io.jansyk.monkeylang

import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class LexerSpec : StringSpec({
    "lexer provides correct token" {
        forAll(
            row(
                "=",
                listOf(Token(TokenType.ASSIGN, "="))
            ),
            row(
                "",
                listOf(Token(TokenType.EOF, ""))
            ),
            row(
                "=+(){},;",
                listOf(
                    Token(TokenType.ASSIGN, "="),
                    Token(TokenType.PLUS, "+"),
                    Token(TokenType.LPAREN, "("),
                    Token(TokenType.RPAREN, ")"),
                    Token(TokenType.LBRACE, "{"),
                    Token(TokenType.RBRACE, "}"),
                    Token(TokenType.COMMA, ","),
                    Token(TokenType.SEMICOLON, ";"),
                )
            ),
        ) { input, tokens ->
            val lexer = Lexer(input)
            tokens.forEach {
                lexer.nextToken() shouldBe it
            }
        }
    }
})
