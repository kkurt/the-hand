package com.gilt.thehand.rules.typed

import com.gilt.thehand.rules.conversions.ConvertsToLong
import com.gilt.thehand.rules.comparison.LessThanEqual
import com.gilt.thehand.rules.SingleValueRuleParser

/**
 * A typed implementation of the LessThanEqual trait, specific to Long. This can be used for any non-decimal datatype.
 */
case class LongLte(value: Long) extends LessThanEqual with ConvertsToLong {
  // This override is necessary because the native type Long is not Ordered (RichLong mixes this in).
  override def matchInnerType(v: Long) = v <= value
}

/**
 * Use this to differentiate LessThanEqual[Long] from other versions of LessThanEqual.
 */
object LongLteParser extends SingleValueRuleParser[LongLte] {
  def ruleConstructor(value: Long) = LongLte(value)
  def toValue(value: String) = value.toLong
}
