package com.gilt.thehand.rules.typed

import com.gilt.thehand.rules.conversions.ConvertsToLong
import com.gilt.thehand.rules.comparison.GreaterThan
import com.gilt.thehand.rules.SingleValueRuleParser

/**
 * A typed implementation of the GreaterThan trait, specific to Long. This can be used for any non-decimal datatype.
 */
case class LongGt(value: Long) extends GreaterThan with ConvertsToLong {
  // This override is necessary because the native type Long is not Ordered (RichLong mixes this in).
  override def matchInnerType(v: Long) = v > value
}

/**
 * Use this to differentiate GreaterThan[Long] from other versions of GreaterThan.
 */
object LongGtParser extends SingleValueRuleParser[LongGt] {
  def ruleConstructor(value: Long) = LongGt(value)
  def toValue(value: String) = value.toLong
}
