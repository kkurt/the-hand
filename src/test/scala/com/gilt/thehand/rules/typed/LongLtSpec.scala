package com.gilt.thehand.rules.typed

import com.gilt.thehand.{Context, AbstractRuleSpec}

class LongLtSpec extends AbstractRuleSpec {
  val testCases = Map(
    LongLt(54) -> (
      Set(Context(53), Context(BigDecimal("53")), Context(53.0), Context('6'), Context(true)),
      Set(Context(54), Context(55), Context(BigDecimal("54.001")), Context(BigDecimal("53.99")), Context(54.00), Context("54.0"))
      ),
    LongLt(1) -> (
      Set(Context(BigDecimal("0.000")), Context(0.00), Context(false), Context(-1), Context('0')),
      Set(Context(BigDecimal("0.001")), Context(0.01), Context(true), Context("11"), Context('1'), Context('2'))
      ),
    LongLt(2147483649L) -> ( // This is Int.MaxValue.toLong + 1
      Set(Context(Int.MaxValue), Context(2147483648L), Context(BigDecimal("2147483648")), Context(BigDecimal("2147483648.00")), Context(2147483648.0)),
      Set(Context(BigDecimal("2147483648.001")), Context(2147483648.01), Context("2147483649"), Context("2147483650"))
      )
  )

  runTests(testCases)
}
