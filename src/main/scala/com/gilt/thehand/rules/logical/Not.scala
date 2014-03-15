package com.gilt.thehand.rules.logical

import com.gilt.thehand.{AbstractContext, AbstractRuleParser, Rule, RuleParser}

/**
 * Flips the rule evaluation of the nested rule from false to true or vice versa.
 */
case class Not(rule: Rule) extends Rule {
  /**
   * Attempt to match the context using the nexted rule and flip Some/None based on the result.
   */
  def unapply(context: AbstractContext): Option[AbstractContext] = context match {
    case `rule`(context) => None
    case _ => Some(context)
  }
}

/**
 * Parses a String to a Not rule, recursively parsing the inner member into a rule.
 */
case class NotParser(parser: RuleParser) extends AbstractRuleParser {
  def toValue(value: String): Rule = parser.fromString(value)
  def unapply(deserializeFrom: String): Option[Rule] = {
    val matchRegEx = "Not\\((.+)\\)".r
    deserializeFrom match {
      case matchRegEx(valuesStr) => Some(Not(toValue(valuesStr)))
      case _ => None
    }
  }
}
