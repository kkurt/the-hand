package com.gilt.thehand.rules

import com.gilt.thehand.{AbstractContext, Context}
/**
 * A general interface that can be used to implement an In rule for any datatype. In practice, this is used mostly
 * for parsing, to differentiate, say, In[String] from In[Long].
 */
trait In[U] extends SeqRule {
  type InnerType = U

  /**
   * Implement/override this if you want to accept 'similar' types that do not match InnerType. For example, Long and
   * Int are often interchangeable, so this method can be overridden to translate Int to Long (see @LongIn for an
   * example).
   */
  def toRuleType: PartialFunction[Any, InnerType] = PartialFunction.empty

  /**
   * Attempts to match the context, first looking to strictly match InnerType. It then attempts to match 'similar' types
   * as defined by 'toRuleType'.
   */
  def unapply(context: AbstractContext): Option[AbstractContext] = context match {
    case c: Context[InnerType] if values.contains(c.instance) => Some(context)
    case c: Context[_] if toRuleType.isDefinedAt(c.instance) && values.contains(toRuleType(c.instance)) => Some(context)
    case _ => None
  }
}
