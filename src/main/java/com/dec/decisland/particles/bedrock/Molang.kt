package com.dec.decisland.particles.bedrock

import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class MolangContext(
    val particleAge: Double = 0.0,
    val particleLifetime: Double = 0.0,
    val particleRandom1: Double = Math.random(),
    val particleRandom2: Double = Math.random(),
    val variables: Map<String, Double> = emptyMap(),
)

object Molang {
    fun evalDouble(expr: String?, ctx: MolangContext): Double? {
        if (expr == null) return null
        val trimmed = expr.trim()
        if (trimmed.isEmpty()) return null

        return try {
            Parser(trimmed).parseExpression(ctx)
        } catch (_: Exception) {
            trimmed.toDoubleOrNull()
        }
    }

    private class Parser(private val input: String) {
        private var pos: Int = 0

        fun parseExpression(ctx: MolangContext): Double {
            val value = parseAddSub(ctx)
            skipWs()
            if (pos != input.length) {
                throw IllegalArgumentException("Unexpected trailing input")
            }
            return value
        }

        private fun parseAddSub(ctx: MolangContext): Double {
            var value = parseMulDiv(ctx)
            while (true) {
                skipWs()
                when (peek()) {
                    '+' -> {
                        pos++
                        value += parseMulDiv(ctx)
                    }
                    '-' -> {
                        pos++
                        value -= parseMulDiv(ctx)
                    }
                    else -> return value
                }
            }
        }

        private fun parseMulDiv(ctx: MolangContext): Double {
            var value = parseUnary(ctx)
            while (true) {
                skipWs()
                when (peek()) {
                    '*' -> {
                        pos++
                        value *= parseUnary(ctx)
                    }
                    '/' -> {
                        pos++
                        value /= parseUnary(ctx)
                    }
                    else -> return value
                }
            }
        }

        private fun parseUnary(ctx: MolangContext): Double {
            skipWs()
            return when (peek()) {
                '+' -> {
                    pos++
                    parseUnary(ctx)
                }
                '-' -> {
                    pos++
                    -parseUnary(ctx)
                }
                else -> parsePrimary(ctx)
            }
        }

        private fun parsePrimary(ctx: MolangContext): Double {
            skipWs()
            return when (val ch = peek()) {
                '(' -> {
                    pos++
                    val value = parseAddSub(ctx)
                    expect(')')
                    value
                }
                null -> throw IllegalArgumentException("Unexpected end")
                else -> when {
                    ch.isDigit() || ch == '.' -> parseNumber()
                    ch.isLetter() || ch == '_' -> parseIdentifierOrCall(ctx)
                    else -> throw IllegalArgumentException("Unexpected token")
                }
            }
        }

        private fun parseIdentifierOrCall(ctx: MolangContext): Double {
            val name = parseQualifiedIdentifier()
            skipWs()
            if (peek() == '(') {
                pos++
                val args = ArrayList<Double>()
                skipWs()
                if (peek() != ')') {
                    while (true) {
                        args += parseAddSub(ctx)
                        skipWs()
                        if (peek() == ',') {
                            pos++
                            continue
                        }
                        break
                    }
                }
                expect(')')
                return callFunction(name, args)
            }
            return resolveVariable(name, ctx)
        }

        private fun resolveVariable(raw: String, ctx: MolangContext): Double {
            val name = raw.lowercase()
                .removePrefix("variable.")
                .removePrefix("v.")
            return when (name) {
                "particle_age", "particle.age" -> ctx.particleAge
                "particle_lifetime", "particle.lifetime" -> ctx.particleLifetime
                "particle_random_1", "particle.random_1", "particlerandom1" -> ctx.particleRandom1
                "particle_random_2", "particle.random_2", "particlerandom2" -> ctx.particleRandom2
                else -> ctx.variables[name] ?: 0.0
            }
        }

        private fun callFunction(raw: String, args: List<Double>): Double {
            return when (raw.lowercase()) {
                "math.random", "math.randomf", "random", "math.rand" -> {
                    when (args.size) {
                        0 -> Math.random()
                        1 -> Math.random() * args[0]
                        else -> args[0] + (args[1] - args[0]) * Math.random()
                    }
                }
                "math.pow", "pow" -> (args.getOrNull(0) ?: 0.0).pow(args.getOrNull(1) ?: 0.0)
                "math.sin", "sin" -> sin(args.getOrNull(0) ?: 0.0)
                "math.cos", "cos" -> cos(args.getOrNull(0) ?: 0.0)
                else -> 0.0
            }
        }

        private fun parseQualifiedIdentifier(): String {
            val start = pos
            while (true) {
                val ch = peek() ?: break
                if (ch.isLetterOrDigit() || ch == '_' || ch == '.') {
                    pos++
                } else {
                    break
                }
            }
            return input.substring(start, pos)
        }

        private fun parseNumber(): Double {
            val start = pos
            while (true) {
                val ch = peek() ?: break
                if (ch.isDigit() || ch == '.' || ch == 'e' || ch == 'E' || ch == '+' || ch == '-') {
                    pos++
                } else {
                    break
                }
            }
            return input.substring(start, pos).toDouble()
        }

        private fun skipWs() {
            while (pos < input.length && input[pos].isWhitespace()) pos++
        }

        private fun peek(): Char? = if (pos < input.length) input[pos] else null

        private fun expect(ch: Char) {
            skipWs()
            if (peek() != ch) throw IllegalArgumentException("Expected $ch")
            pos++
        }
    }
}
