package com.gilt.thehand.rules

import com.gilt.thehand.RuleParser
import com.gilt.thehand.rules.conversions.ConvertsToString

/**
 * A typed implementation of the In trait, specific to String.
 */
case class StringIn(values: String*) extends In with ConvertsToString

/**
 * Use this to differentiate In[String] from other versions of In.
 */
object StringInParser extends SeqRuleParser[StringIn] {
  def toValue(value: String)(implicit parser: RuleParser) = value
  def ruleConstructor = StringIn.apply
}
