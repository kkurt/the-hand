package com.gilt.thehand.rules

import com.gilt.thehand.rules.conversions.ConvertsTo

/**
 * A rule that tests equality.
 */
trait Eq extends SingleValueRule { self: ConvertsTo =>
  /**
   * Simple equality test for this rule.
   * @param v
   * @return
   */
  def matchInnerType(v: InnerType) = value == v
}
