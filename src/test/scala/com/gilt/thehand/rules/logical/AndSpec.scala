package com.gilt.thehand.rules.logical

import com.gilt.thehand.{Context, AbstractRuleSpec}

/**
 * In these tests, any context could have been used; using Boolean here to prove that true/false doesn't matter in the
 * context when evaluating True/False rules.
 */
class AndSpec extends AbstractRuleSpec {
  val testCases = Map(
    And(True) -> (
      Set(Context(true), Context(false)),
      Set.empty[Context[Boolean]]
      ),
    And(False) -> (
      Set.empty[Context[Boolean]],
      Set(Context(true), Context(false))
      ),
    And(True, True) -> (
      Set(Context(true), Context(false)),
      Set.empty[Context[Boolean]]
      ),
    And(False, True) -> (
      Set.empty[Context[Boolean]],
      Set(Context(true), Context(false))
      ),
    And(True, False) -> (
      Set.empty[Context[Boolean]],
      Set(Context(true), Context(false))
      ),
    And(False, False) -> (
      Set.empty[Context[Boolean]],
      Set(Context(true), Context(false))
      ),
    And(True, And(True, True)) -> (
      Set(Context(true), Context(false)),
      Set.empty[Context[Boolean]]
      ),
    And(True, And(False, True)) -> (
      Set.empty[Context[Boolean]],
      Set(Context(true), Context(false))
      ),
    And(True, And(True, False)) -> (
      Set.empty[Context[Boolean]],
      Set(Context(true), Context(false))
      ),
    And(False, And(True, True)) -> (
      Set.empty[Context[Boolean]],
      Set(Context(true), Context(false))
      ),
    And(False, And(False, True)) -> (
      Set.empty[Context[Boolean]],
      Set(Context(true), Context(false))
      ),
    And(False, And(True, False)) -> (
      Set.empty[Context[Boolean]],
      Set(Context(true), Context(false))
      ),
    And(False, And(False, False)) -> (
      Set.empty[Context[Boolean]],
      Set(Context(true), Context(false))
      )
  )

  runTests(testCases)
}
