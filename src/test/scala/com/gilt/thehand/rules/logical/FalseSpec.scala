package com.gilt.thehand.rules.logical

import com.gilt.thehand.{Context, RuleSpec}

class FalseSpec extends RuleSpec {
  def testCases = Map(
    False -> (
        Set.empty,
        Set(Context(1), Context("2"), Context(true))
      )
  )

  runTests(testCases)
}
