package com.gilt.thehand.rules.conversions

/**
 * Inherit from this to implement a rule that converts from other types to a String.
 */
trait ConvertsToString extends ConvertsTo {
  type InnerType = String

  /**
   * Accepts all types of contexts.
   */
  override def toRuleType = {
    case o => o.toString
  }
}
