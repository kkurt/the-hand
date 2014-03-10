package com.gilt.thehand.rules

import com.gilt.thehand.{Context, RuleSpec}

class StringInSpec extends RuleSpec {
  val testCases = Map(
    StringIn("a", "bb", "ccc", "2.1", "2.2", "10", "false") -> (
        Set(Context("a"), Context("bb"), Context("ccc"), Context(2.1), Context(BigDecimal("2.2")), Context(10), Context(false)),
        Set(Context(1), Context("aa"), Context("b"), Context(true))
      )
  )

  runTests(testCases)
}
