package com.gilt.thehand.rules.typed

import com.gilt.thehand.rules.conversions.ConvertsToString
import com.gilt.thehand.rules.comparison.GreaterThan
import com.gilt.thehand.rules.SingleValueRuleParser

/**
 * A typed implementation of the GreaterThan trait, specific to String.
 */
case class StringGt(value: String) extends GreaterThan with ConvertsToString {
  // This override is necessary because the native type String is not Ordered (RichString mixes this in).
  override def matchInnerType(v: String) = v > value
}

/**
 * Use this to differentiate GreaterThan[String] from other versions of GreaterThan.
 */
object StringGtParser extends SingleValueRuleParser[StringGt] {
  def ruleConstructor(value: String) = StringGt(value)
  def toValue(value: String) = value
}
