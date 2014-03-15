package com.gilt.thehand.rules.typed

import com.gilt.thehand.rules.conversions.ConvertsToString
import com.gilt.thehand.rules.comparison.LessThanEqual
import com.gilt.thehand.rules.SingleValueRuleParser

/**
 * A typed implementation of the LessThanEqual trait, specific to String.
 */
case class StringLte(value: String) extends LessThanEqual with ConvertsToString {
  // This override is necessary because the native type String is not Ordered (RichString mixes this in).
  override def matchInnerType(v: String) = v <= value
}

/**
 * Use this to differentiate LessThanEqual[String] from other versions of LessThanEqual.
 */
object StringLteParser extends SingleValueRuleParser[StringLte] {
  def ruleConstructor(value: String) = StringLte(value)
  def toValue(value: String) = value
}
