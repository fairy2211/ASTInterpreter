import org.example.Scanner
import org.example.Token
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import java.io.File

class TestScanner {

    companion object {
        var path1 = "/Users/david/IdeaProjects/ASTInterpreter/src/main/resources/sourcecode1"
        var path2 = "/Users/david/IdeaProjects/ASTInterpreter/src/main/resources/sourcecode2"
        var source_code1 = File(path1).bufferedReader().readLines()
        var source_code2 = File(path2).bufferedReader().readLines()
        var scanner:Scanner = Scanner(source_code1, mutableListOf<Token>())
        @JvmStatic
        @BeforeAll
        fun setup(): Unit {
        }

        fun get_scanner(): Scanner {
            return scanner
        }
    }

    @Test
    fun test_scanner_advance(){
        var scanner:Scanner = get_scanner()
        for (i in 0..source_code1.size-1) {
            for (j in 0..source_code1[i].length) {
                val scanned:Char = scanner.advance()
                if (i == source_code1.size-1 && j == source_code1[i].length) {
                    assertEquals('\u0000', scanned)
                }
                else if (j == source_code1[i].length) {
                    assertEquals('\n', scanned)
                }
                else {
                assertEquals(source_code1[i][j], scanned)
                }
            }
        }
        scanner.reset()
    }

    @Test
    fun test_scanner_peek(){
        val scanner:Scanner = get_scanner()
        scanner.set_source_code(source_code2)
        var source = scanner.get_source_code()
        for (k in 0..source.size-1) {
            for (i in 0..source[k].length) {
                val peeked: Char = scanner.peek()
                scanner.advance()
                if (i == source[k].length) {
                    if (source.size == k+1) {
                        assertEquals('\u0000', peeked)
                        continue
                    }
                    else {
                        assertEquals('\n', peeked)
                        continue
                    }
                }
                if (i < source[k].length) {
                    assertEquals(source[k][i], peeked)
                }
            }
        }
        scanner.reset()
    }

    @Test
    fun test_scan_token(){
        val scanner:Scanner = get_scanner()
        scanner.set_source_code(source_code1)
        val tokens = scanner.scan_tokens()
        for (token in tokens) {
            println(token.toString())
        }
    }
}