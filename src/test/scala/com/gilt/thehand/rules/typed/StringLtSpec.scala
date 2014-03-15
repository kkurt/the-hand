package com.gilt.thehand.rules.typed

import com.gilt.thehand.{AbstractContext, Context, Rule, RuleSpec}

class StringLtSpec extends RuleSpec {
  val testCases: Map[Rule, (Set[AbstractContext], Set[AbstractContext])] = Map(
    StringLt("1") -> (
      Set(Context(0), Context(0.001)),
      Set(Context(1), Context(1.0), Context(BigDecimal("1.00")), Context(true))
      ),
    StringLt("b") -> (
      Set(Context('a'), Context("aa"), Context(98)),
      Set(Context("b"), Context(true))
      ),
    StringLt("ab") -> (
      Set(Context("aa"), Context(98)),
      Set(Context("b"), Context("abb"), Context(true))
      )
  )

  runTests(testCases)
}
