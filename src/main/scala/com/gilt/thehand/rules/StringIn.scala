package com.gilt.thehand.rules

import com.gilt.thehand.RuleParser

/**
 * A typed implementation of the In trait, specific to String.
 */
case class StringIn(values: String*) extends In[String] {
  /**
   * Accepts all types of contexts.
   */
  override def toRuleType = {
    case o => o.toString
  }
}

/**
 * Use this to differentiate In[String] from other versions of In.
 */
object StringInParser extends SeqRuleParser[StringIn] {
  def toValue(value: String)(implicit parser: RuleParser) = value
  def ruleConstructor = StringIn.apply
}
