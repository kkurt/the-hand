package com.gilt.thehand.rules.logical

import com.gilt.thehand.{Context, AbstractRuleSpec}

/**
 * In these tests, any context could have been used; using Boolean here to prove that true/false doesn't matter in the
 * context when evaluating True/False rules.
 */
class OrSpec extends AbstractRuleSpec {
  val testCases = Map(
    Or(True) -> (
      Set(Context(true), Context(false)),
      Set.empty[Context[Boolean]]
      ),
    Or(False) -> (
      Set.empty[Context[Boolean]],
      Set(Context(true), Context(false))
      ),
    Or(True, True) -> (
      Set(Context(true), Context(false)),
      Set.empty[Context[Boolean]]
      ),
    Or(False, True) -> (
      Set(Context(true), Context(false)),
      Set.empty[Context[Boolean]]
      ),
    Or(True, False) -> (
      Set(Context(true), Context(false)),
      Set.empty[Context[Boolean]]
      ),
    Or(False, False) -> (
      Set.empty[Context[Boolean]],
      Set(Context(true), Context(false))
      ),
    Or(True, Or(True, True)) -> (
      Set(Context(true), Context(false)),
      Set.empty[Context[Boolean]]
      ),
    Or(True, Or(False, True)) -> (
      Set(Context(true), Context(false)),
      Set.empty[Context[Boolean]]
      ),
    Or(True, Or(True, False)) -> (
      Set(Context(true), Context(false)),
      Set.empty[Context[Boolean]]
      ),
    Or(False, Or(True, True)) -> (
      Set(Context(true), Context(false)),
      Set.empty[Context[Boolean]]
      ),
    Or(False, Or(False, True)) -> (
      Set(Context(true), Context(false)),
      Set.empty[Context[Boolean]]
      ),
    Or(False, Or(True, False)) -> (
      Set(Context(true), Context(false)),
      Set.empty[Context[Boolean]]
      ),
    Or(False, Or(False, False)) -> (
      Set.empty[Context[Boolean]],
      Set(Context(true), Context(false))
      )
  )

  runTests(testCases)
}
