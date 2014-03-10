package com.gilt.thehand

/**
 * The base trait to implement when defining new types of rules.
 */
trait Rule {
  /**
   * Implement this to determine whether a context matches a rule. Under the hood, this will likely match on various
   * context types, since matching can differ based on context, and a given rule might successfully match in more than
   * one context. The Option return type is leveraged by the 'extractor' pattern and as such implementers should return
   * None when rules do not match the context.
   *
   * @param context The context on which we are attempting to match the rule; can be any type of context.
   * @return None for no match; Some(context) if the rule matches the context.
   */
  def unapply(context: AbstractContext): Option[AbstractContext]

  /**
   * A convenience method to be used to see if a rule matches a context.
   * @param context The context in which the rule attempts to match or not match.
   * @return Boolean: true if the rule matches the context; false if not.
   */
  def matches(context: AbstractContext): Boolean = unapply(context).isDefined
}
