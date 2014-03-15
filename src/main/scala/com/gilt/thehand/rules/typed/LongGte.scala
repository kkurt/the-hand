package com.gilt.thehand.rules.typed

import com.gilt.thehand.rules.conversions.ConvertsToLong
import com.gilt.thehand.rules.comparison.GreaterThanEqual
import com.gilt.thehand.rules.SingleValueRuleParser

/**
 * A typed implementation of the GreaterThanEqual trait, specific to Long. This can be used for any non-decimal datatype.
 */
case class LongGte(value: Long) extends GreaterThanEqual with ConvertsToLong {
  // This override is necessary because the native type Long is not Ordered (RichLong mixes this in).
  override def matchInnerType(v: Long) = v >= value
}

/**
 * Use this to differentiate GreaterThanEqual[Long] from other versions of GreaterThanEqual.
 */
object LongGteParser extends SingleValueRuleParser[LongGte] {
  def ruleConstructor(value: Long) = LongGte(value)
  def toValue(value: String) = value.toLong
}
