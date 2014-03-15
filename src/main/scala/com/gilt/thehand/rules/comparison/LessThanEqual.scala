package com.gilt.thehand.rules.comparison

import com.gilt.thehand.rules.conversions.ConvertsTo
import com.gilt.thehand.rules.SingleValueRule

/**
 * A rule that tests less-than-or-equal.
 */
trait LessThanEqual extends SingleValueRule { self: ConvertsTo =>
  /**
   * Simple less-than-or-equal test for this rule.
   * @param v
   * @return
   */
  def matchInnerType(v: InnerType) = v.asInstanceOf[Ordered[InnerType]] <= value
}
