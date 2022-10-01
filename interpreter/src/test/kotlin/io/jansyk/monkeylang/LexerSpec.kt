package io.jansyk.monkeylang

import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class LexerSpec : StringSpec({
    "lexer reads single token" {
        forAll(
            row("=", Token(TokenType.ASSIGN, "=")),
            row("+", Token(TokenType.PLUS, "+")),
            row("-", Token(TokenType.MINUS, "-")),
            row("!", Token(TokenType.BANG, "!")),
            row("*", Token(TokenType.ASTERISK, "*")),
            row("/", Token(TokenType.SLASH, "/")),
            row("(", Token(TokenType.LPAREN, "(")),
            row(")", Token(TokenType.RPAREN, ")")),
            row("{", Token(TokenType.LBRACE, "{")),
            row("}", Token(TokenType.RBRACE, "}")),
            row(",", Token(TokenType.COMMA, ",")),
            row(";", Token(TokenType.SEMICOLON, ";")),
            row("<", Token(TokenType.LT, "<")),
            row(">", Token(TokenType.GT, ">")),
            row("let", Token(TokenType.LET, "let")),
            row("fn", Token(TokenType.FUNCTION, "fn")),
            row("foo", Token(TokenType.IDENT, "foo")),
            row("true", Token(TokenType.TRUE, "true")),
            row("false", Token(TokenType.FALSE, "false")),
            row("if", Token(TokenType.IF, "if")),
            row("else", Token(TokenType.ELSE, "else")),
            row("return", Token(TokenType.RETURN, "return")),
            row("", Token(TokenType.EOF, "")),
        ) { input, token ->
            val lexer = Lexer(input)
            lexer.nextToken() shouldBe token
        }
    }

    "lexer reads variable declaration with assignment" {
        val input = """let five = 5;""".trimIndent()
        val lexer = Lexer(input)

        val tokens = listOf(
            Token(TokenType.LET, "let"),
            Token(TokenType.IDENT, "five"),
            Token(TokenType.ASSIGN, "="),
            Token(TokenType.INT, "5"),
            Token(TokenType.SEMICOLON, ";"),
            Token(TokenType.EOF, "")
        )

        val results = mutableListOf<Token>()
        var token = lexer.nextToken()
        while (token.type != TokenType.EOF) {
            results.add(token)
            token = lexer.nextToken()
        }
        results.add(token)

        results shouldBe tokens
    }

    "lexer reads function declaration with assignment to variable" {
        val input = """let add = fn(x,y) {
          x + y;
        };""".trimIndent()
        val lexer = Lexer(input)

        val tokens = listOf(
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
            Token(TokenType.EOF, "")
        )

        val results = mutableListOf<Token>()
        var token = lexer.nextToken()
        while (token.type != TokenType.EOF) {
            results.add(token)
            token = lexer.nextToken()
        }
        results.add(token)

        results shouldBe tokens
    }

    "lexer reads function invocation with assignment to variable" {
        val input = """let result = add(five, ten);""".trimIndent()
        val lexer = Lexer(input)

        val tokens = listOf(
            Token(TokenType.LET, "let"),
            Token(TokenType.IDENT, "result"),
            Token(TokenType.ASSIGN, "="),
            Token(TokenType.IDENT, "add"),
            Token(TokenType.LPAREN, "("),
            Token(TokenType.IDENT, "five"),
            Token(TokenType.COMMA, ","),
            Token(TokenType.IDENT, "ten"),
            Token(TokenType.RPAREN, ")"),
            Token(TokenType.SEMICOLON, ";"),
            Token(TokenType.EOF, "")
        )

        val results = mutableListOf<Token>()
        var token = lexer.nextToken()
        while (token.type != TokenType.EOF) {
            results.add(token)
            token = lexer.nextToken()
        }
        results.add(token)

        results shouldBe tokens
    }

    "lexer reads math expression" {
        val input = """4 + 1 - 10 / 3 * 5""".trimIndent()
        val lexer = Lexer(input)

        val tokens = listOf(
            Token(TokenType.INT, "4"),
            Token(TokenType.PLUS, "+"),
            Token(TokenType.INT, "1"),
            Token(TokenType.MINUS, "-"),
            Token(TokenType.INT, "10"),
            Token(TokenType.SLASH, "/"),
            Token(TokenType.INT, "3"),
            Token(TokenType.ASTERISK, "*"),
            Token(TokenType.INT, "5"),
            Token(TokenType.EOF, "")
        )

        val results = mutableListOf<Token>()
        var token = lexer.nextToken()
        while (token.type != TokenType.EOF) {
            results.add(token)
            token = lexer.nextToken()
        }
        results.add(token)

        results shouldBe tokens
    }

    "lexer reads condition" {
        val input = """if (!true) {
                return true;
            } else {
                return false;
            }
        """.trimIndent()
        val lexer = Lexer(input)

        val tokens = listOf(
            Token(TokenType.IF, "if"),
            Token(TokenType.LPAREN, "("),
            Token(TokenType.BANG, "!"),
            Token(TokenType.TRUE, "true"),
            Token(TokenType.RPAREN, ")"),
            Token(TokenType.LBRACE, "{"),
            Token(TokenType.RETURN, "return"),
            Token(TokenType.TRUE, "true"),
            Token(TokenType.SEMICOLON, ";"),
            Token(TokenType.RBRACE, "}"),
            Token(TokenType.ELSE, "else"),
            Token(TokenType.LBRACE, "{"),
            Token(TokenType.RETURN, "return"),
            Token(TokenType.FALSE, "false"),
            Token(TokenType.SEMICOLON, ";"),
            Token(TokenType.RBRACE, "}"),
            Token(TokenType.EOF, "")
        )

        val results = mutableListOf<Token>()
        var token = lexer.nextToken()
        while (token.type != TokenType.EOF) {
            results.add(token)
            token = lexer.nextToken()
        }
        results.add(token)

        results shouldBe tokens
    }


    "lexer reads equal check" {
        val input = """10 == 10;""".trimIndent()
        val lexer = Lexer(input)

        val tokens = listOf(
            Token(TokenType.INT, "10"),
            Token(TokenType.EQ, "=="),
            Token(TokenType.INT, "10"),
            Token(TokenType.SEMICOLON, ";"),
            Token(TokenType.EOF, "")
        )

        val results = mutableListOf<Token>()
        var token = lexer.nextToken()
        while (token.type != TokenType.EOF) {
            results.add(token)
            token = lexer.nextToken()
        }
        results.add(token)

        results shouldBe tokens
    }

    "lexer reads not equality check" {
        val input = """10 != 5;""".trimIndent()
        val lexer = Lexer(input)

        val tokens = listOf(
            Token(TokenType.INT, "10"),
            Token(TokenType.NOT_EQ, "!="),
            Token(TokenType.INT, "5"),
            Token(TokenType.SEMICOLON, ";"),
            Token(TokenType.EOF, "")
        )

        val results = mutableListOf<Token>()
        var token = lexer.nextToken()
        while (token.type != TokenType.EOF) {
            results.add(token)
            token = lexer.nextToken()
        }
        results.add(token)

        results shouldBe tokens
    }

    "lexer reads complex input" {
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
            Token(TokenType.LPAREN, "("),
            Token(TokenType.IDENT, "five"),
            Token(TokenType.COMMA, ","),
            Token(TokenType.IDENT, "ten"),
            Token(TokenType.RPAREN, ")"),
            Token(TokenType.SEMICOLON, ";"),
            Token(TokenType.EOF, "")
        )

        val results = mutableListOf<Token>()
        var token = lexer.nextToken()
        while (token.type != TokenType.EOF) {
            results.add(token)
            token = lexer.nextToken()
        }
        results.add(token)

        results shouldBe tokens
    }
})
