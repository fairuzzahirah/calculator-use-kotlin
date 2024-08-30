package com.example.kalkulator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import java.util.Stack
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var input: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupButtons()
    }

    private fun setupButtons() {
        // Menghubungkan tombol dengan logika
        findViewById<Button>(R.id.btn0).setOnClickListener { appendNumber("0") }
        findViewById<Button>(R.id.btn1).setOnClickListener { appendNumber("1") }
        findViewById<Button>(R.id.btn2).setOnClickListener { appendNumber("2") }
        findViewById<Button>(R.id.btn3).setOnClickListener { appendNumber("3") }
        findViewById<Button>(R.id.btn4).setOnClickListener { appendNumber("4") }
        findViewById<Button>(R.id.btn5).setOnClickListener { appendNumber("5") }
        findViewById<Button>(R.id.btn6).setOnClickListener { appendNumber("6") }
        findViewById<Button>(R.id.btn7).setOnClickListener { appendNumber("7") }
        findViewById<Button>(R.id.btn8).setOnClickListener { appendNumber("8") }
        findViewById<Button>(R.id.btn9).setOnClickListener { appendNumber("9") }
        findViewById<Button>(R.id.btn00).setOnClickListener { appendNumber("00") }
        findViewById<Button>(R.id.btnDots).setOnClickListener { appendNumber(".") }

        findViewById<Button>(R.id.btnPlus).setOnClickListener { appendOperator('+') }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { appendOperator('-') }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { appendOperator('*') }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { appendOperator('/') }

        findViewById<Button>(R.id.btnleftB).setOnClickListener { appendOperator('(') }
        findViewById<Button>(R.id.btnRightB).setOnClickListener { appendOperator(')') }

        findViewById<Button>(R.id.btnEqual).setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.btnC).setOnClickListener { clearInput() }
    }

    private fun appendNumber(number: String) {
        input += number
        findViewById<TextView>(R.id.tvInput).text = input
    }

    private fun appendOperator(op: Char) {
        input += op
        findViewById<TextView>(R.id.tvInput).text = input
    }

    private fun calculateResult() {
        try {
            val result = evaluateExpression(input)
            findViewById<TextView>(R.id.tvOutput).text = result.toString()
            input = result.toString() // Menampilkan hasil sebagai input baru

            // Tampilkan hasil dalam toast
            Toast.makeText(this, "Hasil: $result", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            findViewById<TextView>(R.id.tvOutput).text = "Error"

            // Tampilkan pesan error dalam toast
            Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearInput() {
        input = ""
        findViewById<TextView>(R.id.tvInput).text = ""
        findViewById<TextView>(R.id.tvOutput).text = ""
    }

    private fun evaluateExpression(expression: String): Double {
        val operators = Stack<Char>()
        val values = Stack<Double>()

        var i = 0
        while (i < expression.length) {
            when (val c = expression[i]) {
                ' ' -> i++
                in '0'..'9', '.' -> {
                    val sb = StringBuilder()
                    while (i < expression.length && (expression[i] in '0'..'9' || expression[i] == '.')) {
                        sb.append(expression[i++])
                    }
                    values.push(sb.toString().toDouble())
                    i--
                }
                '(' -> operators.push(c)
                ')' -> {
                    while (operators.peek() != '(') {
                        values.push(applyOp(operators.pop(), values.pop(), values.pop()))
                    }
                    operators.pop()
                }
                '+', '-', '*', '/' -> {
                    while (operators.isNotEmpty() && hasPrecedence(c, operators.peek())) {
                        values.push(applyOp(operators.pop(), values.pop(), values.pop()))
                    }
                    operators.push(c)
                }
            }
            i++
        }

        while (operators.isNotEmpty()) {
            values.push(applyOp(operators.pop(), values.pop(), values.pop()))
        }

        return values.pop()
    }

    private fun hasPrecedence(op1: Char, op2: Char): Boolean {
        return if (op2 == '(' || op2 == ')') {
            false
        } else if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            false
        } else {
            true
        }
    }

    private fun applyOp(op: Char, b: Double, a: Double): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> a / b
            else -> throw UnsupportedOperationException("Unknown operator: $op")
        }
    }
}
