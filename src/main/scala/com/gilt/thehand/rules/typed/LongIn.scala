package com.gilt.thehand.rules.typed

import com.gilt.thehand.rules.conversions.ConvertsToLong
import com.gilt.thehand.rules.comparison.In
import com.gilt.thehand.rules.SeqRuleParser

/**
 * A typed implementation of the In trait, specific to Long. This can be used for any non-decimal datatype.
 */
case class LongIn(values: Long*) extends In with ConvertsToLong

/**
 * Use this to differentiate In[Long] from other versions of In.
 */
object LongInParser extends SeqRuleParser[LongIn] {
  def toValue(value: String) = value.toLong
}

