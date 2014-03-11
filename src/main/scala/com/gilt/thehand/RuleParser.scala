package com.gilt.thehand

import com.gilt.thehand.exceptions.CannotDeserializeException
import com.gilt.thehand.rules._

/**
 * Implement this trait when defining a new RuleParser. The parser should take the serialized string version of the
 * rule and deserialize it into the rule itself.
 */
trait AbstractRuleParser {
  /**
   * Implement this to provide an extractor that pulls the Rule out of the String.
   * @param deserializeFrom The String that should be parsed into a Rule.
   * @return None if the string cannot be parsed; Some(rule) if the string can be parsed.
   */
  def unapply(deserializeFrom: String): Option[Rule]

  /**
   * A convenience method for times when using the extractor is too heavy.
   * @param deserializeFrom The String that should be parsed into a Rule.
   * @return The Rule that had been serialized; this method will throw a CannotDeserializeException if it is unable
   *         to deserialize the string into a rule based on the current parser.
   */
  def fromString(deserializeFrom: String): Rule = unapply(deserializeFrom).getOrElse(throw CannotDeserializeException("Unable to deserialize [%s]".format(deserializeFrom)))
}

/**
 * Use this to create a parsing object that incorporates the specific parsers that your application uses. It also
 * includes parsers for the "default" list of parsers (True, False, And, Or, etc); simply instantiating an object
 * without any arguments will give you a parser that can parse only those defaults.
 *
 * @param ruleParsers: A list of parsing objects that will be used to parse strings.
 */
case class RuleParser(ruleParsers: AbstractRuleParser*) extends AbstractRuleParser {
  val defaultParsers = Seq(True, False, NotParser, AndParser, OrParser, StringInParser, LongInParser)

  def unapply(deserializeFrom: String): Option[Rule] = (ruleParsers ++ defaultParsers).foldLeft(Option.empty[Rule])((result, currentParser) =>
    result orElse currentParser.unapply(deserializeFrom)
  )
}
