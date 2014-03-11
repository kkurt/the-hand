package com.gilt.thehand

import com.gilt.thehand.rules._

/**
 * A place to drop general tests that cross class lines.
 */
class RuleParserSpec extends RuleSpec {

  val testCases = Map(
    And(Or(StringIn("1", "2"), LongIn(10, 31)), And(StringIn("2", "3"), Not(LongIn(3, 4)))) -> (
        Set(Context(2)),
        Set(Context(3), Context("1"))
      )
  )

  runTests(testCases)

  "DefaultParser" should "parse a nested rule correctly" in {
    val rule = DefaultParser.fromString("And(Or(Not(False),False), LongIn(2, 3, 4), True)")
    assert(!rule.matches(Context(1)))
    assert(rule.matches(Context(2)))
    assert(rule.matches(Context(3)))
    assert(rule.matches(Context("4")))
  }

}
