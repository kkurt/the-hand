package com.gilt.thehand.rules.typed

import com.gilt.thehand.{Context, RuleSpec}

class LongGteSpec extends RuleSpec {
  val testCases = Map(
    LongGte(54) -> (
      Set(Context(54), Context(55), Context(BigDecimal("55.00")), Context("54.0"), Context(54.00), Context(55.0)),
      Set(Context(BigDecimal("54.001")), Context(BigDecimal("53.99")), Context("53"), Context('6'), Context(true))
      ),
    LongGte(1) -> (
      Set(Context(BigDecimal("1.000")), Context(1.00), Context(true), Context(1), Context('2')),
      Set(Context(BigDecimal("1.001")), Context(1.01), Context(false), Context("-11"), Context('0'))
      ),
    LongGte(2147483648L) -> ( // This is Int.MaxValue.toLong + 1
      Set(Context(2147483649L), Context(BigDecimal("2147483649")), Context(BigDecimal("2147483649.00")), Context(2147483649.0), Context("2147483648")),
      Set(Context(Int.MaxValue), Context(BigDecimal("2147483648.001")), Context(2147483648.01))
      )
  )

  runTests(testCases)
}
