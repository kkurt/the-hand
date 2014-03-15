package com.gilt.thehand.rules

import com.gilt.thehand.{Context, RuleSpec}
import com.gilt.thehand.rules.logical.False

class FalseSpec extends RuleSpec {
  def testCases = Map(
    False -> (
        Set.empty,
        Set(Context(1), Context("2"), Context(true))
      )
  )

  runTests(testCases)
}
