package org.example

class Test(var a:Int, var b:String) {
    init {
        a = 2
        b = "hello"
    }
    fun test_parameters():Int {
        return a * 2
    }
}