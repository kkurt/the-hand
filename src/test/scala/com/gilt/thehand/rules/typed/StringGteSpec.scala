package com.gilt.thehand.rules.typed

import com.gilt.thehand.{AbstractContext, Context, Rule, AbstractRuleSpec}

class StringGteSpec extends AbstractRuleSpec {
  val testCases: Map[Rule, (Set[AbstractContext], Set[AbstractContext])] = Map(
    StringGte("0") -> (
      Set(Context('0'), Context(1), Context(0.001), Context(0.0), Context(BigDecimal("1.00")), Context(true)),
      Set(Context("-1"))
      ),
    StringGte("a") -> (
      Set(Context("a"), Context('b'), Context("aa"), Context(true)),
      Set(Context(98))
      ),
    StringGte("ab") -> (
      Set(Context("ab"), Context("abb"), Context(true)),
      Set(Context("a"), Context(98))
      )
  )

  runTests(testCases)
}
