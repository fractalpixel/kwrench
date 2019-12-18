package org.kwrench.generator

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.parser.Parser
import org.kwrench.generator.num.Bool
import org.kwrench.generator.num.Num
import org.kwrench.generator.table.FlatSelectionStrategy
import org.kwrench.generator.table.TableGenerator
import org.kwrench.strings.toSymbol


/**
 * Parses generator description from a text string.
 */
class GeneratorParser : Grammar<Generators>() {

    /*
        private val LPAR by token("\\(")
        private val RPAR by token("\\)")
    */
    private val LSQ by token("\\[")
    private val RSQ by token("]")

    private val LT by token("<")
    private val GT by token(">")

    private val LBRC by token("\\{")
    private val RBRC by token("}")

    /*
        private val PLUS by token("\\+")
        private val MINUS by token("-")
     */
    private val DIV by token("/")
/*
    private val MOD by token("%")
    private val TIMES by token("\\*")

 */

    /*
        private val OR by token("or\\b")
        private val AND by token("and\\b")

        private val EQU by token("==")
        private val NEQ by token("!=")
        private val LEQ by token("<=")
        private val GEQ by token(">=")
        private val LT by token("<")
        private val GT by token(">")

        private val NOT by token("not\\b")

        private val COMMA by token(",")
    */
    private val SEMI by token(";")
    private val ASSIGN by token("=")
    //private val PIPE by token("|")

/*

private val IF by token("if\\b")
private val THEN by token("then\\b")
private val ELSE by token("else\\b")

private val REPEAT by token("repeat\\b")

private val FUN by token("fun\\b")

private val TRUE by token("true\\b")
private val FALSE by token("false\\b")

private val NUMBER by token("(\\d*\\.)?\\d+")
private val STRINGLIT by token("\".*?\"")
*/

    //    private val ID by token("[A-Za-z]\\w*")
    private val STRING_CONTENT by token("[^\\[\\]{}|;/=<>]+")

    private val WS by token("\\s+", ignore = true)
    private val NEWLINE by token("[\r\n]+", ignore = true)

/*
private val numConst by
    (optional(MINUS) map { if (it == null) 1 else -1 }) and  // Negative sign
    NUMBER map { (sign, it) -> Num(sign * it.text.toDouble()) } // Number

private val const by
    numConst or
    (TRUE asJust Bool(true)) or
    (FALSE asJust Bool(false))

*/

    private val table by
    -LSQ and separatedTerms(parser(::zeroOrMoreGenerators), DIV, acceptZero = false) * -RSQ map {
        TableGenerator(String::class, FlatSelectionStrategy, *(it as List<out Generator<String>>).toTypedArray())
    }

    val num by token("-?\\d+")
    val lpar by token("\\(")
    val rpar by token("\\)")
    val mul by token("\\*")
    val pow by token("\\^")
    val div by token("/")
    val minus by token("-")
    val plus by token("\\+")
    val ws by token("\\s+", ignore = true)


    private val textContent: Parser<Generator<*>> by STRING_CONTENT.map { ConstantGenerator(it.text, String::class) }
    private val reference: Parser<Generator<*>> by (-LT and STRING_CONTENT and -GT) .map { ParameterGenerator(it.text.toSymbol(), it.text.trim(), String::class) }

    private val generator: Parser<Generator<*>> by textContent or table or reference

    private val zeroOrMoreGenerators: Parser<Generator<*>> by zeroOrMore(generator).map { StringConcatenateGenerator(it) }
    private val oneOrMoreGenerators: Parser<Generator<*>> by oneOrMore(generator).map { StringConcatenateGenerator(it) }

    private val generatorDeclaration: Parser<Pair<String, Generator<*>>> by (STRING_CONTENT and -ASSIGN and oneOrMoreGenerators and -SEMI).map { (id, gen) -> id.text.trim() to gen }

    override val rootParser: Parser<Generators> by oneOrMore(generatorDeclaration).map { generators ->
        Generators(
            generators.associate { it })
    }

}