package ru.otus.otuskotlin.m1l5

import ru.otus.otuskotlin.m1l5.dsl.UserDsl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

// Реализуйте dsl для составления sql запроса, чтобы все тесты стали зелеными.
class Hw1Sql {
    private fun checkSQL(expected: String, sql: SqlSelectBuilder) {
        assertEquals(expected, sql.build())
    }

    @Test
    fun `simple select all from table`() {
        val expected = "select * from table"

        val real = query {
            from("table")
        }

        checkSQL(expected, real)
    }

    @Test
    fun `check that select can't be used without table`() {
        assertFailsWith<Exception> {
            query {
                select("col_a")
            }.build()
        }
    }

    @Test
    fun `select certain columns from table`() {
        val expected = "select col_a, col_b from table"

        val real = query {
            select("col_a", "col_b")
            from("table")
        }

        checkSQL(expected, real)
    }

    @Test
    fun `select certain columns from table 1`() {
        val expected = "select col_a, col_b from table"

        val real = query {
            select("col_a", "col_b")
            from("table")
        }

        checkSQL(expected, real)
    }

    /**
     * __eq__ is "equals" function. Must be one of char:
     *  - for strings - "="
     *  - for numbers - "="
     *  - for null - "is"
     */
    @Test
    fun `select with complex where condition with one condition`() {
        val expected = "select * from table where col_a = 'id'"

        val real = query {
            from("table")
            where { "col_a" eq "id" } // col_a.eq(id)
        }

        checkSQL(expected, real)
    }

    /**
     * __nonEq__ is "non equals" function. Must be one of chars:
     *  - for strings - "!="
     *  - for numbers - "!="
     *  - for null - "!is"
     */
    @Test
    fun `select with complex where condition with two conditions`() {
        val expected = "select * from table where col_a != 0"

        val real = query {
            from("table")
            where {
                "col_a" nonEq 0
            }
        }

        checkSQL(expected, real)
    }

    @Test
    fun `when 'or' conditions are specified then they are respected`() {
        val expected = "select * from table where (col_a = 4 or col_b !is null)"

        val real = query {
            from("table")
            where {
                or {
                    "col_a" eq 4
                    "col_b" nonEq null
                }
            }
        }

        checkSQL(expected, real)
    }
}


fun query(block: SqlSelectBuilder.() -> Unit) = SqlSelectBuilder().apply(block)

class SqlSelectBuilder {
    private var s = ""
    private var f = ""
    private var w = ""

    fun select(vararg columns: String) {
        s = columns.joinToString(", ").trim()
    }

    fun from(table: String) {
        f = table
    }

    fun where(block: WhereContext.() -> Unit) {
        val ctx = WhereContext().apply(block)
        w = ctx.conditionsString
    }

    fun build(): String {
        if (s == "") {
            s = "*"
        }

        if (f == "") {
            throw Exception()
        }
        var result = "select $s from $f"
        if (w != "") {
            result += " where $w"
        }
        return result
    }
}

@UserDsl
open class WhereContext {
    open val conditions: MutableList<String> = mutableListOf()

    open val conditionsString: String
        get() = conditions.joinToString(" ")

    fun or(block: OrContext.() -> Unit) {
        val ctx = OrContext().apply(block)
        conditions.addLast("(${ctx.conditionsString})")
    }

    open fun add(cond: Any?, value: String, o: String) {

        val res = when {
            (cond is String) -> "$value $o '$cond'"
            (cond == null) -> "$value $o null"
            else -> {
                "$value $o $cond"
            }
        }

        conditions.addLast(res)
    }
    open infix fun String.eq(a: Any?) {
        val oper = if (a == null) {
            "is"
        } else {
            "="
        }
        add(a, this, oper)
    }
    open infix fun String.nonEq(a: Any?) {
        val oper = if (a == null) {
            "!is"
        } else {
            "!="
        }
        add(a, this, oper)
    }
}
class OrContext : WhereContext() {

    override val conditionsString: String
        get() = conditions.joinToString(" or ")
}
