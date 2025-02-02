package org.example

class Token(private val type: TokenType, private val lexeme: String, private val literal: Any, val line: Int) {
    override fun toString(): String {
        return "$type $lexeme $literal"
    }
}