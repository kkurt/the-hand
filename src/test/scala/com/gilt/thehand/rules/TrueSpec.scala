package com.gilt.thehand.rules

import com.gilt.thehand.{Context, RuleSpec}
import com.gilt.thehand.rules.logical.True

class TrueSpec extends RuleSpec {
  def testCases = Map(
    True -> (
        Set(Context(1), Context("2"), Context(false)),
        Set.empty
      )
  )

  runTests(testCases)
}
