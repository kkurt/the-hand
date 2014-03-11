package com.gilt.thehand.rules

import com.gilt.thehand.{AbstractContext, AbstractRuleParser, Rule, RuleParser}
import scala.Some

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
object NotParser extends AbstractRuleParser {
  def toValue(value: String)(implicit parser: RuleParser): Rule = parser.fromString(value)
  def unapply(fromStr: String): Option[Rule] = {
    val matchRegEx = "Not\\((.+)\\)".r
    fromStr match {
      case matchRegEx(valuesStr) => Some(Not(toValue(valuesStr)))
      case _ => None
    }
  }
}
