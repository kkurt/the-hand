package com.gilt.thehand.rules.typed

import com.gilt.thehand.{Context, AbstractRuleSpec}

class LongInSpec extends AbstractRuleSpec {
  val testCases = Map(
    LongIn(0, 3, 102, 54) -> (
        Set(Context(0), Context(BigDecimal("102.0")), Context(54.0), Context(false), Context('3'), Context("54")),
        // '6' is important here because its char code is 54.
        Set(Context(2), Context(BigDecimal("102.001")), Context(102.01), Context(true), Context('1'), Context("4"), Context('6'))
      )
  )

  runTests(testCases)
}
