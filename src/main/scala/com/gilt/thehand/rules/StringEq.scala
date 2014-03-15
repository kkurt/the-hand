package com.gilt.thehand.rules

import com.gilt.thehand.rules.conversions.ConvertsToString
import com.gilt.thehand.RuleParser

/**
 * A typed implementation of the Eq trait, specific to String.
 */
case class StringEq(value: String) extends Eq with ConvertsToString

/**
 * Use this to differentiate Eq[String] from other versions of Eq.
 */
object StringEqParser extends SingleValueRuleParser[StringEq] {
  def ruleConstructor(value: String) = StringEq(value)
  def toValue(value: String)(implicit parser: RuleParser) = value
}