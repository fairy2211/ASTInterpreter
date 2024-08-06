package org.example

import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader


object Lox {
    var had_error: Boolean = false


    @JvmStatic
    fun main(args: Array<String>) {
        println(args.get(0))
        if (args.size > 1) {
            println("To use a script specify it via the command line.")
        }
        else if (args.size == 1) {
            runFile(args.get(0))
        }
        else {
            runPrompt()
        }
    }

    fun runFile(path: String) {
        try {
            var file = File(path).bufferedReader().readLines()
            for (line:String in file) {
                println(line)
            }
        }
        catch (e: IOException) {
            println(e.stackTrace)
        }

    }

    fun run(line:String ) {
        val scanner: Scanner = Scanner(listOf(line), mutableListOf())

        if (had_error) {
            System.exit(65)
        }
    }

    fun runPrompt(){
        val input: InputStreamReader = InputStreamReader(System.`in`)
        val reader = BufferedReader(input)

        while (true) {
            print("> ")
            val line = reader.readLine() ?: break
            run(line)
        }
        had_error = false
    }

    fun report_error(line: Int, index: Int, message: String) {
        println("Error scanning input: line ${line}, index ${index}. ${message}")
        had_error = true
    }

    fun report_information(line: Int, index: Int, message: String) {
        println("Line ${line}, index ${index}. ${message}")
    }

}