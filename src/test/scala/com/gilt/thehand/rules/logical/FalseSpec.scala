package com.gilt.thehand.rules.logical

import com.gilt.thehand.{Context, AbstractRuleSpec}

class FalseSpec extends AbstractRuleSpec {
  def testCases = Map(
    False -> (
        Set.empty,
        Set(Context(1), Context("2"), Context(true))
      )
  )

  runTests(testCases)
}
