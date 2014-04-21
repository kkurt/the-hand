package com.gilt.thehand.rules.logical

import com.gilt.thehand.{Context, AbstractRuleSpec}

class TrueSpec extends AbstractRuleSpec {
  def testCases = Map(
    True -> (
        Set(Context(1), Context("2"), Context(false)),
        Set.empty
      )
  )

  runTests(testCases)
}
