package com.gilt.thehand.rules

import com.gilt.thehand.{AbstractRuleParser, Rule, RuleParser, Util}

/**
 * Implement this trait for any rule that takes a list of inputs of the same type. Examples: And, Or, In.
 */
trait SeqRule extends Rule {
  type InnerType // Implementers will need to set this type, see In[T] for an example.

  /**
   * The list of 'InnerType' values that apply to this rule.
   */
  def values: Seq[InnerType]

  override def toString = this.getClass.getSimpleName + "(" + values.map(_.toString).mkString(",") + ")"
}

/**
 * Implement the abstract methods below for easy parsing of SeqRules.
 *
 * @tparam T The type of SeqRule that is being parsed.
 */
abstract class SeqRuleParser[T <: SeqRule : Manifest] extends AbstractRuleParser {
  /**
   * Implement this method to convert a string (the input from the parser) to the typed value in the SeqRule. You will
   * need to implicitly define a parser in scope; if using default rules, simply import @DefaultParser
   *
   * @param value The string representation of the sub-type of SeqRule.
   * @param parser An implicitly-defined parser that can be used recursively to parse strings into values.
   * @return The parsed value of the sub-type.
   */
  def toValue(value: String)(implicit parser: RuleParser): T#InnerType

  /**
   * A constructor for the SeqRule that takes a list of typed values. Often (if using a case class), this will be the
   * 'apply' method, for example 'Or.apply'.
   */
  def ruleConstructor: Seq[T#InnerType] => Rule

  def unapply(fromStr: String): Option[Rule] = {
    val matchRegEx = s"${manifest.runtimeClass.getSimpleName}\\((.+)\\)".r
    fromStr match {
      case matchRegEx(valuesStr) => Some(ruleConstructor(Util.nestedSplit(valuesStr, ',').map(s => toValue(s.trim) )))
      case _ => None
    }
  }
}
