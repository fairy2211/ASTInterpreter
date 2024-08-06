package org.example


class Scanner(var source_code: List<String>, private var tokens: MutableList<Token>, var start:Int, var current:Int, var line:Int,
    var keywords:MutableMap<String, TokenType>) {

    init {
        start = 0
        current = 0
        line = 0

        keywords = mutableMapOf(
        "and" to TokenType.AND,
        "class" to TokenType.CLASS,
        "else" to TokenType.ELSE,
        "false" to TokenType.FALSE,
        "for" to TokenType.FOR,
        "fun" to TokenType.FUN,
        "if" to TokenType.IF,
        "nil" to TokenType.NIL,
        "or" to TokenType.OR,
        "print" to TokenType.PRINT,
        "return" to TokenType.RETURN,
        "super" to TokenType.SUPER,
        "this" to TokenType.THIS,
        "true" to TokenType.TRUE,
        "var" to TokenType.VAR,
        "while" to TokenType.WHILE
        )
    }




    fun get_source_code(): List<String>{
        return source_code
    }


    fun identifier(){
        while (is_alpha_numeric(peek()))
            advance()
        val word:String = source_code[line].substring(start,current)
        val type: TokenType = keywords[word] ?: TokenType.IDENTIFIER
        addToken(type)
    }

    private fun is_alpha(c: Char): Boolean {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') || (
                c == '_')
    }

    private fun is_alpha_numeric(c: Char): Boolean {
        return is_alpha(c) || is_digit(c)
    }

    fun change_source_code(source_code: List<String>) {
        this.source_code = source_code
    }
    
    fun peek(): Char {
        if (is_at_end())
            return '\u0000'
        if (is_at_line_end())
            return '\n'
        return source_code[line][current]
    }


    fun peek_next():Char {
        if (is_at_end()) {
            Lox.report_error(line,current,"Peeked at the end of file.")
            return '\u0000'
        }
        if (is_at_line_end()) {
            Lox.report_information(line,current,"Reached end of line. Returning a newline char as lookahead.")
            return '\n'
        }
        return source_code.get(line).get(current+1)
    }

    private fun scan_token() {
        val c = advance()
        when {
            c.equals('(') -> addToken(TokenType.LEFT_PAREN)
            c.equals(')') -> addToken(TokenType.RIGHT_PAREN)
            c.equals('{') -> addToken(TokenType.LEFT_BRACE)
            c.equals('}') -> addToken(TokenType.RIGHT_BRACE)
            c.equals(',') -> addToken(TokenType.COMMA)
            c.equals('.')-> addToken(TokenType.DOT)
            c.equals('-')-> addToken(TokenType.MINUS)
            c.equals('+') -> addToken(TokenType.PLUS)
            c.equals(';') -> addToken(TokenType.SEMICOLON)
            c.equals('*') -> addToken(TokenType.STAR)

            c.equals('!') ->
            addToken(if (match('=')) TokenType.BANG_EQUAL else TokenType.BANG)
            c.equals('=') ->
            addToken(if (match('=')) TokenType.EQUAL_EQUAL else TokenType.EQUAL)
            c.equals('<') ->
            addToken(if (match('=')) TokenType.LESS_EQUAL else TokenType.LESS)
            c.equals('>') ->
            addToken(if (match('=')) TokenType.GREATER_EQUAL else TokenType.GREATER)

            c.equals('/') ->
            if (match('/')) {
                // A comment goes until the end of the line.
                while (peek() != '\n' && peek() != '\u0000')
                    advance()

                addToken(TokenType.COMMENT)

            } else {
                addToken(TokenType.SLASH)
            }

            c.equals(' ') -> {}
            c.equals('\r') -> {}
            c.equals('\t') -> {}
            // Ignore whitespace.

            c.equals('\n') -> "pass"

            c.equals('"') -> string()

            is_digit(c) -> number()

            is_alpha_numeric(c) -> identifier()

            else -> {
                Lox.report_error(line, current, "Unexpected character encountered while scanning input.")
            }
        }
    }

    private fun is_digit(c: Char): Boolean {
        return c >= '0' && c <= '9'
    }

    private fun number() {
        while (is_digit(peek())) advance()

        // Look for a fractional part.
        if (peek() == '.' && is_digit(peek_next())) {
            // Consume the "."
            advance()

            while (is_digit(peek())) advance()
        }

        addToken(
            TokenType.NUMBER,
            source_code[line].substring(start, current).toDouble()
        )
    }

    private fun string() {
        while (peek() != '"' && !is_at_end()) {
            if (peek() == '\n') line++
            advance()
        }

        if (is_at_end()) {
            Lox.report_error(line, current,"Unterminated string.")
            return
        }

        // The closing ".
        advance()

        // Trim the surrounding quotes.
        val value: String = source_code.get(line).substring(start + 1, current - 1)
        addToken(TokenType.STRING, value)
    }

    private fun match(expected: Char): Boolean {
        if (is_at_line_end()) return false
        if (source_code.get(line).get(current) != expected) return false

        current++
        return true
    }

    fun set_source_code(source_code: List<String>){
        this.source_code = source_code
    }
    private fun addToken(type: TokenType) {
        addToken(type, null)
    }

    private fun addToken(type: TokenType, literal: Any?) {
        val text: String = source_code.get(line).substring(start, current)
        tokens.add(Token(type, text, Any(), line))
    }

    fun scan_tokens(): MutableList<Token> {
        while(!is_at_end()) {
            start = current
            scan_token()
        }
        tokens.add(Token(TokenType.EOF,"",Any(),line))
        return tokens
    }
    
    fun is_at_line_end():Boolean {
        return source_code[line].length <= current
    }

    fun advance():Char {
        if (is_at_end()) {
            Lox.report_error(line, current,"Reached end of file.")
            return '\u0000'
        }

        if (is_at_line_end()) {
            val c:Char = '\n'
            line++
            current = 0
            start = 0
            return c
        }
        else {
            val c:Char = source_code.get(line).get(current++)
            return c
        }
    }

    fun is_at_end(): Boolean {
        if (line+1 == source_code.size && is_at_line_end())
            return true
        return false
    }

    fun reset(){
        current = 0
        line = 0
        start = 0
    }
}
