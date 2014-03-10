package com.gilt.thehand.rules

import com.gilt.thehand.{Context, RuleSpec}

class TrueSpec extends RuleSpec {
  def testCases = Map(
    True -> (
        Set(Context(1), Context("2"), Context(false)),
        Set.empty
      )
  )

  runTests(testCases)
}
