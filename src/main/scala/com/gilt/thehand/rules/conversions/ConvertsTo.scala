package com.gilt.thehand.rules.conversions

import com.gilt.thehand.{AbstractContext, Context, Rule}

/**
 * A general rule trait that incorporates logic to convert from other types to the expected one.
 *
 * The InnerType is the type to convert to.
 */
trait ConvertsTo extends Rule {
  type InnerType

  /**
   * Implement/override this if you want to accept 'similar' types that do not match InnerType. For example, Long and
   * Int are often interchangeable, so this method can be overridden to translate Int to Long (see @LongIn for an
   * example). Due to type erasure, you will need to include the real InnerType in the listing.
   */
  def toRuleType: PartialFunction[Any, InnerType]

  /**
   * Implement this in order to determine whether a given value matches the current rule.
   *
   * @param value The rule is attempting to match against this value.
   * @return
   */
  def matchInnerType(value: InnerType): Boolean

  /**
   * Attempts to match the context, first looking to strictly match InnerType. It then attempts to match 'similar' types
   * as defined by 'toRuleType'.
   */
  def unapply(context: AbstractContext): Option[AbstractContext] = context match {
    case Context(c: Char) => unapply(Context(c.toString)) // Necessary because Char implicitly converts to many other types, causing confusion.
    case Context(value) if toRuleType.isDefinedAt(value) && matchInnerType(toRuleType(value)) => Some(context)
    case _ => None
  }
}
