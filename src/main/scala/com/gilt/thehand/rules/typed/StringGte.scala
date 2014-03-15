package com.gilt.thehand.rules.typed

import com.gilt.thehand.rules.conversions.ConvertsToString
import com.gilt.thehand.rules.comparison.GreaterThanEqual
import com.gilt.thehand.rules.SingleValueRuleParser

/**
 * A typed implementation of the GreaterThanEqual trait, specific to String.
 */
case class StringGte(value: String) extends GreaterThanEqual with ConvertsToString {
  // This override is necessary because the native type String is not Ordered (RichString mixes this in).
  override def matchInnerType(v: String) = v >= value
}

/**
 * Use this to differentiate GreaterThanEqual[String] from other versions of GreaterThanEqual.
 */
object StringGteParser extends SingleValueRuleParser[StringGte] {
  def ruleConstructor(value: String) = StringGte(value)
  def toValue(value: String) = value
}
