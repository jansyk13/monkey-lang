package io.jansyk.monkeylang

class Repl {
    companion object {
        fun start() {
            while(true) {
                print(">> ")
                val line = readln()
                val lexer = Lexer(line)
                var token = lexer.nextToken()
                while (token.type != TokenType.EOF) {
                    println(token)
                    token = lexer.nextToken()
                }
            }
        }
    }

}

fun main(args: Array<String>) {
    val user = System.getProperty("user.name")

    println("Hello $user! This is the Monkey programming language!")
    println("Feel fee to type in commands")
    Repl.start()
}