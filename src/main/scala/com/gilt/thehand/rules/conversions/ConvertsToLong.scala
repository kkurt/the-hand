package com.gilt.thehand.rules.conversions

/**
 * Inherit from this to implement a rule that converts from other types to a Long.
 */
trait ConvertsToLong extends ConvertsTo {
  type InnerType = Long

  /**
   * Accept other 'Long'-like types when applicable.
   */
  override def toRuleType = {
    case l: Long => l
    case bd: BigDecimal if bd.toLong == bd => bd.toLong
    case b: Boolean => if (b) 1L else 0L
    case b: Byte => b.toLong
    case c: Char if c.isDigit => c.toString.toLong // toString is necessary, otherwise it will be the ascii char num.
    case d: Double if d.toLong == d => d.toLong
    case i: Int => i.toLong
    case s: Short => s.toLong
    case s: String if s.matches("^\\d+$") => s.toLong
    case s: String if s.matches("^\\d+\\.0+$") => s.split("\\.").head.toLong
  }

}
