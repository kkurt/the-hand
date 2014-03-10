package com.gilt.thehand.rules

import com.gilt.thehand.{AbstractContext, AbstractRuleParser, Rule}

/**
 * Used to statically evaluate a Rule as true, and parse values to a true
 */
case object True extends Rule with AbstractRuleParser {
  /**
   * Extractor for rule matching. Since this is a True, always return a Some.
   */
  def unapply(context: AbstractContext): Option[AbstractContext] = Some(context)

  /**
   * Extractor for string parsing.
   */
  def unapply(deserializeFrom: String): Option[Rule] = deserializeFrom.trim match {
    case "True" | "true" | "T" | "t" | "1" => Some(True)
    case _ => None
  }
}
