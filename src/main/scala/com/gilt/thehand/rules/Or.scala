package com.gilt.thehand.rules

import com.gilt.thehand.{AbstractContext, Rule, RuleParser}

/**
 * Pass any number of rules into this Or class to boolean-or their results together. This will short-circuit
 * processing once the first rule passes.
 */
case class Or(values: Rule*) extends SeqRule {
  type InnerType = Rule

  def unapply(context: AbstractContext): Option[AbstractContext] = values.foldRight(Option.empty[AbstractContext]) ((currentRule, aggregateResult) =>
    aggregateResult orElse currentRule.unapply(context)
  )
}

/**
 * Parses a String to an Or rule, recursively pasring for each member of the rule.
 */
object OrParser extends SeqRuleParser[Or] {
  def toValue(value: String)(implicit parser: RuleParser): Rule = parser.fromString(value)

  def ruleConstructor = Or.apply
}

