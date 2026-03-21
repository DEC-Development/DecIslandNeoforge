package com.dec.decisland.particles.bedrock

import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin

class MolangContext(
    val emitterAge: Double = 0.0,
    val particleAge: Double = 0.0,
    val particleLifetime: Double = 0.0,
    val particleRandom1: Double = Math.random(),
    val particleRandom2: Double = Math.random(),
    val particleRandom3: Double = Math.random(),
    val particleRandom4: Double = Math.random(),
    val emitterRandom1: Double = Math.random(),
    val emitterRandom2: Double = Math.random(),
    val emitterRandom3: Double = Math.random(),
    val emitterRandom4: Double = Math.random(),
    val queryFrameAlpha: Double = 0.0,
    val variables: Map<String, Double> = emptyMap(),
) {
    fun copyWithVariables(variables: Map<String, Double>): MolangContext =
        MolangContext(
            emitterAge = emitterAge,
            particleAge = particleAge,
            particleLifetime = particleLifetime,
            particleRandom1 = particleRandom1,
            particleRandom2 = particleRandom2,
            particleRandom3 = particleRandom3,
            particleRandom4 = particleRandom4,
            emitterRandom1 = emitterRandom1,
            emitterRandom2 = emitterRandom2,
            emitterRandom3 = emitterRandom3,
            emitterRandom4 = emitterRandom4,
            queryFrameAlpha = queryFrameAlpha,
            variables = variables,
        )
}

object Molang {
    fun evalDouble(expr: String?, ctx: MolangContext): Double? = evalDouble(expr, ctx, null)

    fun evalDouble(expr: String?, ctx: MolangContext, mutableVariables: MutableMap<String, Double>?): Double? {
        if (expr == null) return null
        val trimmed = expr.trim()
        if (trimmed.isEmpty()) return null

        return try {
            Evaluator(trimmed, ctx, mutableVariables).execute()
        } catch (_: Exception) {
            trimmed.toDoubleOrNull()
        }
    }

    private class Evaluator(
        private val input: String,
        private val ctx: MolangContext,
        private val mutableVariables: MutableMap<String, Double>?,
    ) {
        fun execute(): Double {
            var result = 0.0
            for (statement in splitStatements(input)) {
                val trimmed = statement.trim()
                if (trimmed.isEmpty()) continue

                if (trimmed.startsWith("return", ignoreCase = true) && trimmed.drop(6).firstOrNull()?.isWhitespace() != false) {
                    return Parser(trimmed.removePrefix("return").trim(), ctx, mutableVariables).parseExpression()
                }

                val assignmentIndex = findAssignmentIndex(trimmed)
                if (assignmentIndex >= 0) {
                    val rawName = trimmed.substring(0, assignmentIndex).trim()
                    val normalizedName = normalizeVariableName(rawName) ?: throw IllegalArgumentException("Invalid assignment")
                    val value = Parser(trimmed.substring(assignmentIndex + 1), ctx, mutableVariables).parseExpression()
                    mutableVariables?.set(normalizedName, value)
                    result = value
                } else {
                    result = Parser(trimmed, ctx, mutableVariables).parseExpression()
                }
            }
            return result
        }

        private fun splitStatements(source: String): List<String> =
            source.split(';')

        private fun findAssignmentIndex(source: String): Int {
            for (index in source.indices) {
                if (source[index] != '=') continue
                val prev = source.getOrNull(index - 1)
                val next = source.getOrNull(index + 1)
                if (prev == '=' || prev == '<' || prev == '>' || prev == '!' || next == '=') continue
                return index
            }
            return -1
        }
    }

    private class Parser(
        private val input: String,
        private val ctx: MolangContext,
        private val mutableVariables: MutableMap<String, Double>?,
    ) {
        private var pos: Int = 0

        fun parseExpression(): Double {
            val value = parseTernary()
            skipWs()
            if (pos != input.length) {
                throw IllegalArgumentException("Unexpected trailing input")
            }
            return value
        }

        private fun parseTernary(): Double {
            val condition = parseLogicalOr()
            skipWs()
            if (peek() != '?') {
                return condition
            }
            pos++
            val ifTrue = parseTernary()
            expect(':')
            val ifFalse = parseTernary()
            return if (condition != 0.0) ifTrue else ifFalse
        }

        private fun parseLogicalOr(): Double {
            var value = parseLogicalAnd()
            while (true) {
                skipWs()
                if (!match("||")) return value
                val right = parseLogicalAnd()
                value = if (value != 0.0 || right != 0.0) 1.0 else 0.0
            }
        }

        private fun parseLogicalAnd(): Double {
            var value = parseEquality()
            while (true) {
                skipWs()
                if (!match("&&")) return value
                val right = parseEquality()
                value = if (value != 0.0 && right != 0.0) 1.0 else 0.0
            }
        }

        private fun parseEquality(): Double {
            var value = parseComparison()
            while (true) {
                skipWs()
                value = when {
                    match("==") -> if (value == parseComparison()) 1.0 else 0.0
                    match("!=") -> if (value != parseComparison()) 1.0 else 0.0
                    else -> return value
                }
            }
        }

        private fun parseComparison(): Double {
            var value = parseAddSub()
            while (true) {
                skipWs()
                value = when {
                    match("<=") -> if (value <= parseAddSub()) 1.0 else 0.0
                    match(">=") -> if (value >= parseAddSub()) 1.0 else 0.0
                    match("<") -> if (value < parseAddSub()) 1.0 else 0.0
                    match(">") -> if (value > parseAddSub()) 1.0 else 0.0
                    else -> return value
                }
            }
        }

        private fun parseAddSub(): Double {
            var value = parseMulDiv()
            while (true) {
                skipWs()
                when (peek()) {
                    '+' -> {
                        pos++
                        value += parseMulDiv()
                    }
                    '-' -> {
                        pos++
                        value -= parseMulDiv()
                    }
                    else -> return value
                }
            }
        }

        private fun parseMulDiv(): Double {
            var value = parseUnary()
            while (true) {
                skipWs()
                when (peek()) {
                    '*' -> {
                        pos++
                        value *= parseUnary()
                    }
                    '/' -> {
                        pos++
                        value /= parseUnary()
                    }
                    else -> return value
                }
            }
        }

        private fun parseUnary(): Double {
            skipWs()
            return when (peek()) {
                '+' -> {
                    pos++
                    parseUnary()
                }
                '-' -> {
                    pos++
                    -parseUnary()
                }
                else -> parsePrimary()
            }
        }

        private fun parsePrimary(): Double {
            skipWs()
            return when (val ch = peek()) {
                '(' -> {
                    pos++
                    val value = parseTernary()
                    expect(')')
                    value
                }
                null -> throw IllegalArgumentException("Unexpected end")
                else -> when {
                    ch.isDigit() || ch == '.' -> parseNumber()
                    ch.isLetter() || ch == '_' -> parseIdentifierOrCall()
                    else -> throw IllegalArgumentException("Unexpected token")
                }
            }
        }

        private fun parseIdentifierOrCall(): Double {
            val name = parseQualifiedIdentifier()
            skipWs()
            if (peek() == '(') {
                pos++
                val args = ArrayList<Double>()
                skipWs()
                if (peek() != ')') {
                    while (true) {
                        args += parseTernary()
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
            return resolveVariable(name)
        }

        private fun resolveVariable(raw: String): Double {
            val name = normalizeVariableName(raw) ?: raw.lowercase()
            return when (name) {
                "emitter_age", "emitter.age" -> ctx.emitterAge
                "particle_age", "particle.age" -> ctx.particleAge
                "particle_lifetime", "particle.lifetime" -> ctx.particleLifetime
                "particle_random_1", "particle.random_1", "particlerandom1" -> ctx.particleRandom1
                "particle_random_2", "particle.random_2", "particlerandom2" -> ctx.particleRandom2
                "particle_random_3", "particle.random_3", "particlerandom3" -> ctx.particleRandom3
                "particle_random_4", "particle.random_4", "particlerandom4" -> ctx.particleRandom4
                "emitter_random_1", "emitter.random_1", "emitterrandom1" -> ctx.emitterRandom1
                "emitter_random_2", "emitter.random_2", "emitterrandom2" -> ctx.emitterRandom2
                "emitter_random_3", "emitter.random_3", "emitterrandom3" -> ctx.emitterRandom3
                "emitter_random_4", "emitter.random_4", "emitterrandom4" -> ctx.emitterRandom4
                "query.frame_alpha", "frame_alpha" -> ctx.queryFrameAlpha
                else -> mutableVariables?.get(name) ?: ctx.variables[name] ?: 0.0
            }
        }

        private fun callFunction(raw: String, args: List<Double>): Double =
            when (raw.lowercase()) {
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
                "math.abs", "abs" -> abs(args.getOrNull(0) ?: 0.0)
                "math.min", "min" -> min(args.getOrNull(0) ?: 0.0, args.getOrNull(1) ?: 0.0)
                "math.max", "max" -> max(args.getOrNull(0) ?: 0.0, args.getOrNull(1) ?: 0.0)
                "math.floor", "floor" -> kotlin.math.floor(args.getOrNull(0) ?: 0.0)
                "math.ceil", "ceil" -> kotlin.math.ceil(args.getOrNull(0) ?: 0.0)
                "math.round", "round" -> kotlin.math.round(args.getOrNull(0) ?: 0.0)
                "math.sqrt", "sqrt" -> kotlin.math.sqrt(args.getOrNull(0) ?: 0.0)
                else -> 0.0
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

        private fun match(token: String): Boolean {
            if (!input.regionMatches(pos, token, 0, token.length)) return false
            pos += token.length
            return true
        }

        private fun peek(): Char? = if (pos < input.length) input[pos] else null

        private fun expect(ch: Char) {
            skipWs()
            if (peek() != ch) throw IllegalArgumentException("Expected $ch")
            pos++
        }
    }

    private fun normalizeVariableName(raw: String): String? {
        val lowered = raw.trim().lowercase()
        if (lowered.isEmpty()) return null
        return lowered
            .removePrefix("variable.")
            .removePrefix("v.")
    }
}
