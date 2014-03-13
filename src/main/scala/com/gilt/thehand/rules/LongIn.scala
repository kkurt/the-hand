package com.gilt.thehand.rules

import com.gilt.thehand.RuleParser
import com.gilt.thehand.rules.conversions.ConvertsToLong

/**
 * A typed implementation of the In trait, specific to Long. This can be used for any non-decimal datatype.
 */
case class LongIn(values: Long*) extends In with ConvertsToLong

/**
 * Use this to differentiate In[Long] from other versions of In.
 */
object LongInParser extends SeqRuleParser[LongIn] {
  def toValue(value: String)(implicit parser: RuleParser) = value.toLong
  def ruleConstructor = LongIn.apply
}
