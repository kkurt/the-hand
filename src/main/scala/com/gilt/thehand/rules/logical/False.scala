package com.gilt.thehand.rules.logical

import com.gilt.thehand.{AbstractContext, AbstractRuleParser, Rule}

/**
 * Used to statically evaluate a Rule as false, and parse values to a False
 */
case object False extends Rule with AbstractRuleParser {
  /**
   * Extractor for rule matching. Since this is a False, always return a None.
   */
  def unapply(context: AbstractContext): Option[AbstractContext] = None

  /**
   * Extractor for string parsing.
   */
  def unapply(deserializeFrom: String): Option[Rule] = deserializeFrom.trim match {
    case "False" | "false" | "F" | "f" | "0" => Some(False)
    case _ => None
  }
}
