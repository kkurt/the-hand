package com.gilt.thehand.rules.typed

import com.gilt.thehand.{AbstractContext, Context, Rule, AbstractRuleSpec}

class StringLteSpec extends AbstractRuleSpec {
  val testCases: Map[Rule, (Set[AbstractContext], Set[AbstractContext])] = Map(
    StringLte("1") -> (
      Set(Context(1), Context(0), Context(0.001)),
      Set(Context(1.0), Context(BigDecimal("1.00")), Context(true))
      ),
    StringLte("b") -> (
      Set(Context('a'), Context("b"), Context("aa"), Context(98)),
      Set(Context(true))
      ),
    StringLte("ab") -> (
      Set(Context("aa"), Context("ab"), Context(98)),
      Set(Context("b"), Context("abb"), Context(true))
      )
  )

  runTests(testCases)
}

