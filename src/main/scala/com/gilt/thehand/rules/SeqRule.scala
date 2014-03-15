package com.gilt.thehand.rules

import com.gilt.thehand.{AbstractRuleParser, Rule, Util}

/**
 * Implement this trait for any rule that takes a list of inputs of the same type. Examples: And, Or, In.
 */
trait SeqRule extends Rule {
  type InnerType // Implementers will need to set this type.

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
   * @return The parsed value of the sub-type.
   */
  def toValue(value: String): T#InnerType

  /**
   * A constructor for the SeqRule that takes a list of typed values. Override this if your class constructor does not
   * simply take a Seq[InnerType].
   */
  def ruleConstructor(values: Seq[T#InnerType]): T = manifest.runtimeClass.getConstructor(classOf[Seq[T#InnerType]]).newInstance(values).asInstanceOf[T]

  def unapply(deserializeFrom: String): Option[T] = {
    val matchRegEx = s"${manifest.runtimeClass.getSimpleName}\\((.+)\\)".r
    deserializeFrom match {
      case matchRegEx(valuesStr) => Some(ruleConstructor(Util.nestedSplit(valuesStr, ',').map(s => toValue(s.trim) )))
      case _ => None
    }
  }
}
