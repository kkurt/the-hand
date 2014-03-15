package com.gilt.thehand.rules.comparison

import com.gilt.thehand.rules.SingleValueRule
import com.gilt.thehand.rules.conversions.ConvertsTo

/**
 * A rule that tests greater-than.
 */
trait GreaterThan extends SingleValueRule { self: ConvertsTo =>
  /**
   * Simple greater-than test for this rule.
   * @param v
   * @return
   */
  def matchInnerType(v: InnerType) = v.asInstanceOf[Ordered[InnerType]] > value
}
