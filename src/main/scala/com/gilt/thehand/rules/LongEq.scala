package com.gilt.thehand.rules

import com.gilt.thehand.rules.conversions.ConvertsToLong
import com.gilt.thehand.RuleParser

/**
 * A typed implementation of the Eq trait, specific to Long. This can be used for any non-decimal datatype.
 */
case class LongEq(value: Long) extends Eq with ConvertsToLong

/**
 * Use this to differentiate Eq[Long] from other versions of Eq.
 */
object LongEqParser extends SingleValueRuleParser[LongEq] {
  def ruleConstructor(value: Long) = LongEq(value)
  def toValue(value: String)(implicit parser: RuleParser) = value.toLong
}
