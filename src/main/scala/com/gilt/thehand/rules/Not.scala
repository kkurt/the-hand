package com.gilt.thehand.rules

import com.gilt.thehand.{AbstractContext, Context, Rule}

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

//  object NotParser extends AbstractRuleParser {
//    def toValue(value: String)(implicit parser: RuleParser): Rule = parser.fromString(value)
//    def unapply(fromStr: String): Option[Rule] = {
//      val matchRegEx = "%s\\((.+)\\)".trim.format(manifest.runtimeClass.getSimpleName).r
//      fromStr match {
//        case matchRegEx(valuesStr) => Some(Not(valuesStr))
//        case _ => None
//      }
//    }
//  }
