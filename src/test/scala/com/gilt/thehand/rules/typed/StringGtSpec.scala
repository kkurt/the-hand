package com.gilt.thehand.rules.typed

import com.gilt.thehand.{AbstractContext, Context, Rule, AbstractRuleSpec}

class StringGtSpec extends AbstractRuleSpec {
  val testCases: Map[Rule, (Set[AbstractContext], Set[AbstractContext])] = Map(
    StringGt("0") -> (
      Set(Context(1), Context(0.001), Context(0.0), Context(BigDecimal("1.00")), Context(true)),
      Set(Context('0'), Context("-1"))
      ),
    StringGt("a") -> (
      Set(Context('b'), Context("aa"), Context(true)),
      Set(Context("a"), Context(98))
      ),
    StringGt("ab") -> (
      Set(Context("abb"), Context(true)),
      Set(Context("ab"), Context("a"), Context(98))
      )
  )

  runTests(testCases)
}
