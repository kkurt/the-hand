package com.gilt.thehand.rules

import com.gilt.thehand.{Context, RuleSpec}

class LongEqSpec extends RuleSpec {
  val testCases = Map(
    LongEq(54) -> (
      Set(Context(54), Context(BigDecimal("54")), Context(54.0)),
      // '6' is important here because its char code is 54.
      Set(Context(55), Context(BigDecimal("54.001")), Context(54.01), Context(true), Context('1'), Context("544"), Context('6'))
      ),
    LongEq(1) -> (
        Set(Context(BigDecimal("1.000")), Context(1.00), Context(true), Context('1')),
        Set(Context(0), Context(BigDecimal("1.001")), Context(1.01), Context(false), Context("11"), Context('2'))
      ),
    LongEq(2147483648L) -> ( // This is Int.MaxValue.toLong + 1
      Set(Context(2147483648L), Context(BigDecimal("2147483648")), Context(BigDecimal("2147483648.00")), Context(2147483648.0)),
      Set(Context(Int.MaxValue), Context(BigDecimal("2147483648.001")), Context(2147483648.01), Context(true), Context("2147483649"))
      )
  )

  runTests(testCases)
}
