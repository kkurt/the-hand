package com.gilt.thehand.rules.logical

import com.gilt.thehand.{Context, RuleSpec}

/**
 * In these tests, any context could have been used; using Boolean here to prove that true/false doesn't matter in the
 * context when evaluating True/False rules.
 */
class NotSpec extends RuleSpec {
  val testCases = Map(
    Not(True) -> (
        Set.empty[Context[Boolean]],
        Set(Context(true), Context(false))
      ),
    Not(False) -> (
        Set(Context(true), Context(false)),
        Set.empty[Context[Boolean]]
      ),
    Not(Not(True)) -> (
        Set(Context(true), Context(false)),
        Set.empty[Context[Boolean]]
      ),
    Not(Not(False)) -> (
        Set.empty[Context[Boolean]],
        Set(Context(true), Context(false))
      ),
    Not(Not(Not(True))) -> (
        Set.empty[Context[Boolean]],
        Set(Context(true), Context(false))
      ),
    Not(Not(Not(False))) -> (
        Set(Context(true), Context(false)),
        Set.empty[Context[Boolean]]
      )
  )

  runTests(testCases)
}
