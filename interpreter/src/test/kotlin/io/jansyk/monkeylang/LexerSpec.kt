package io.jansyk.monkeylang

import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class LexerSpec : StringSpec({
    "lexer provides simple tokens" {
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

    "lexer provides tokens on complex input" {
        val input = """let five = 5;
        let ten = 10;
        
        let add = fn(x,y) {
          x + y;
        };
        
        let result = add(five, ten);
        """.trimIndent()
        val lexer = Lexer(input)

        val tokens = listOf(
            Token(TokenType.LET, "let"),
            Token(TokenType.IDENT, "five"),
            Token(TokenType.ASSIGN, "="),
            Token(TokenType.INT, "5"),
            Token(TokenType.SEMICOLON, ";"),
            Token(TokenType.LET, "let"),
            Token(TokenType.IDENT, "ten"),
            Token(TokenType.ASSIGN, "="),
            Token(TokenType.INT, "10"),
            Token(TokenType.SEMICOLON, ";"),
            Token(TokenType.LET, "let"),
            Token(TokenType.IDENT, "add"),
            Token(TokenType.ASSIGN, "="),
            Token(TokenType.FUNCTION, "fn"),
            Token(TokenType.LPAREN, "("),
            Token(TokenType.IDENT, "x"),
            Token(TokenType.COMMA, ","),
            Token(TokenType.IDENT, "y"),
            Token(TokenType.RPAREN, ")"),
            Token(TokenType.LBRACE, "{"),
            Token(TokenType.IDENT, "x"),
            Token(TokenType.PLUS, "+"),
            Token(TokenType.IDENT, "y"),
            Token(TokenType.SEMICOLON, ";"),
            Token(TokenType.RBRACE, "}"),
            Token(TokenType.SEMICOLON, ";"),
            Token(TokenType.LET, "let"),
            Token(TokenType.IDENT, "result"),
            Token(TokenType.ASSIGN, "="),
            Token(TokenType.IDENT, "add"),
            Token(TokenType.IDENT, "five"),
            Token(TokenType.COMMA, ","),
            Token(TokenType.IDENT, "ten"),
            Token(TokenType.RPAREN, ")"),
            Token(TokenType.SEMICOLON, ";"),
            Token(TokenType.EOF, "")
        )
        tokens.forEach {
            lexer.nextToken() shouldBe it
        }
    }
})
