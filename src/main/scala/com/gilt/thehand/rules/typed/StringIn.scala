package com.gilt.thehand.rules.typed

import com.gilt.thehand.rules.conversions.ConvertsToString
import com.gilt.thehand.rules.comparison.In
import com.gilt.thehand.rules.SeqRuleParser

/**
 * A typed implementation of the In trait, specific to String.
 */
case class StringIn(values: String*) extends In with ConvertsToString

/**
 * Use this to differentiate In[String] from other versions of In.
 */
object StringInParser extends SeqRuleParser[StringIn] {
  def toValue(value: String) = value
}
