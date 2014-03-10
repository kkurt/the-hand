package com.gilt.thehand.rules

import com.gilt.thehand.RuleParser

/**
 * A typed implementation of the In trait, specific to Long. This can be used for any non-decimal datatype.
 */
case class LongIn(values: Long*) extends In[Long] {
  /**
   * Accept other 'Int'-like types when applicable.
   */
  override def toRuleType = {
    case bd: BigDecimal if bd.isValidLong => bd.toLong
    case b: Boolean => if (b) 1L else 0L
    case b: Byte => b.toLong
    case c: Char if c.isDigit => c.toString.toLong // toString is necessary, otherwise it will be the ascii char num.
    case d: Double if d.isValidInt => d.toLong // No isValidLong on Double; this may cause some confusion, but keep it for now.
    case i: Int => i.toLong
    case s: Short => s.toLong
    case s: String if s.matches("^\\d+$") => s.toLong
  }

}

/**
 * Use this to differentiate In[Long] from other versions of In.
 */
object LongInParser extends SeqRuleParser[LongIn] {
  def toValue(value: String)(implicit parser: RuleParser) = value.toInt
  def ruleConstructor = LongIn.apply
}

