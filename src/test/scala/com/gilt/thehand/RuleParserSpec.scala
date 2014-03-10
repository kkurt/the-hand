package com.gilt.thehand

import org.scalatest.{FlatSpec, Matchers}

class RuleParserSpec extends FlatSpec with Matchers {

  "DefaultParser" should "parse a nested rule correctly" in {
    val rule = DefaultParser.fromString("And(Or(True,False), LongIn(2, 3, 4), True)")
    assert(!rule.matches(Context(1)))
    assert(rule.matches(Context(2)))
    assert(rule.matches(Context(3)))
    assert(rule.matches(Context("4")))
  }

}
