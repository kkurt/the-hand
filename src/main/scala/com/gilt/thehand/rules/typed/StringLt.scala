package com.gilt.thehand.rules.typed

import com.gilt.thehand.rules.conversions.ConvertsToString
import com.gilt.thehand.rules.comparison.LessThan
import com.gilt.thehand.rules.SingleValueRuleParser

/**
 * A typed implementation of the LessThan trait, specific to String.
 */
case class StringLt(value: String) extends LessThan with ConvertsToString {
  // This override is necessary because the native type String is not Ordered (RichString mixes this in).
  override def matchInnerType(v: String) = v < value
}

/**
 * Use this to differentiate LessThan[String] from other versions of LessThan.
 */
object StringLtParser extends SingleValueRuleParser[StringLt] {
  def ruleConstructor(value: String) = StringLt(value)
  def toValue(value: String) = value
}
