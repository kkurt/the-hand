package com.gilt.thehand.rules.typed

import com.gilt.thehand.rules.conversions.ConvertsToLong
import com.gilt.thehand.rules.comparison.LessThan
import com.gilt.thehand.rules.SingleValueRuleParser

/**
 * A typed implementation of the LessThan trait, specific to Long. This can be used for any non-decimal datatype.
 */
case class LongLt(value: Long) extends LessThan with ConvertsToLong {
  // This override is necessary because the native type Long is not Ordered (RichLong mixes this in).
  override def matchInnerType(v: Long) = v < value
}

/**
 * Use this to differentiate LessThan[Long] from other versions of LessThan.
 */
object LongLtParser extends SingleValueRuleParser[LongLt] {
  def ruleConstructor(value: Long) = LongLt(value)
  def toValue(value: String) = value.toLong
}
