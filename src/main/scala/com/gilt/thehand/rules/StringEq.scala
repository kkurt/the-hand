package com.gilt.thehand.rules

import com.gilt.thehand.rules.conversions.ConvertsToString
import com.gilt.thehand.RuleParser

/**
 * A typed implementation of the Eq trait, specific to String.
 */
case class StringEq(value: String) extends Eq with ConvertsToString

/**
 * Use this to differentiate In[Long] from other versions of In.
 */
object StringEqParser extends EqParser[StringEq] {
  def toValue(value: String)(implicit parser: RuleParser) = value
  def ruleConstructor = StringEq.apply
}