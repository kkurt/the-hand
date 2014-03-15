package com.gilt.thehand.rules

import com.gilt.thehand.rules.conversions.ConvertsTo
import com.gilt.thehand.{Rule, RuleParser, AbstractRuleParser}

/**
 * A type of rule that takes a single value and tests against it.
 */
trait SingleValueRule { self: ConvertsTo =>
  type InnerType

  /**
   * The value on which to test equality.
   * @return
   */
  def value: InnerType

  /**
   * Implement this to determine the test that determines the outcome of this rule. The context's value will be passed
   * into this method and you can compare it against the value above.
   * @param v
   * @return
   */
  def matchInnerType(v: InnerType): Boolean
}

/**
 * Implement the abstract methods below for easy parsing of SingleValueRules.
 *
 * @tparam T The type of SingleValueRule that is being parsed.
 */
abstract class SingleValueRuleParser[T <: SingleValueRule : Manifest] extends AbstractRuleParser {
  /**
   * Implement this method to convert a string (the input from the parser) to the typed value in the Eq. You will
   * need to implicitly define a parser in scope; if using default rules, simply import @DefaultParser
   *
   * @param value The string representation of the sub-type of Eq.
   * @param parser An implicitly-defined parser that can be used recursively to parse strings into values.
   * @return The parsed value of the sub-type.
   */
  def toValue(value: String)(implicit parser: RuleParser): T#InnerType

  /**
   * A constructor for the SeqRule that takes a typed value. Override this if your class constructor does not simply
   * take an InnerType.
   *
   * I can't get the default to work, even after some effort - so giving up for now. I think some sort of type erasure
   * allows this to work for Seq[T#InnerType] (in SeqRuleParser), whereas the non-erased type here does not work.
   */
  def ruleConstructor(value: T#InnerType): Rule //= manifest.runtimeClass.getConstructor(classOf[Any]).newInstance(value).asInstanceOf[Rule]

  def unapply(deserializeFrom: String): Option[Rule] = {
    val matchRegEx = s"${manifest.runtimeClass.getSimpleName}\\((.+)\\)".r
    deserializeFrom match {
      case matchRegEx(valueStr) => Some(ruleConstructor(toValue(valueStr.trim)))
      case _ => None
    }
  }
}