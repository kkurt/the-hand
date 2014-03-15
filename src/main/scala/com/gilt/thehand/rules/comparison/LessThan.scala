package com.gilt.thehand.rules.comparison

import com.gilt.thehand.rules.conversions.ConvertsTo
import com.gilt.thehand.rules.SingleValueRule

/**
 * A rule that tests less-than.
 */
trait LessThan extends SingleValueRule { self: ConvertsTo =>
  /**
   * Simple less-than test for this rule.
   * @param v
   * @return
   */
  def matchInnerType(v: InnerType) = v.asInstanceOf[Ordered[InnerType]] < value
}
