package com.gilt.thehand.rules

import com.gilt.thehand._
import com.gilt.thehand.rules.conversions.ConvertsTo
import com.gilt.thehand.RuleParser
import scala.Some

/**
 * A rule that tests equality.
 */
trait Eq { self: ConvertsTo =>
  type InnerType

  /**
   * The value on which to test equality.
   * @return
   */
  def value: InnerType

  /**
   * Simple equality test for this rule.
   * @param v
   * @return
   */
  def matchInnerType(v: InnerType) = value == v
}

/**
 * Implement the abstract methods below for easy parsing of SeqRules.
 *
 * @tparam T The type of SeqRule that is being parsed.
 */
abstract class EqParser[T <: Eq : Manifest] extends AbstractRuleParser {
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
   * A constructor for the Eq that takes a typed value. Often (if using a case class), this will be the
   * 'apply' method, for example 'Or.apply'.
   */
  def ruleConstructor: T#InnerType => Rule

  def unapply(deserializeFrom: String): Option[Rule] = {
    val matchRegEx = s"${manifest.runtimeClass.getSimpleName}\\((.+)\\)".r
    deserializeFrom match {
      case matchRegEx(valueStr) => Some(ruleConstructor(toValue(valueStr.trim)))
      case _ => None
    }
  }
}