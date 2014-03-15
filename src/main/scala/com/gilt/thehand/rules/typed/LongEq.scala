package com.gilt.thehand.rules.typed

import com.gilt.thehand.rules.conversions.ConvertsToLong
import com.gilt.thehand.rules.comparison.Eq
import com.gilt.thehand.rules.SingleValueRuleParser

/**
 * A typed implementation of the Eq trait, specific to Long. This can be used for any non-decimal datatype.
 */
case class LongEq(value: Long) extends Eq with ConvertsToLong

/**
 * Use this to differentiate Eq[Long] from other versions of Eq.
 */
object LongEqParser extends SingleValueRuleParser[LongEq] {
  def ruleConstructor(value: Long) = LongEq(value)
  def toValue(value: String) = value.toLong
}
