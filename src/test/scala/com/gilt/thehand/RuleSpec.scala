package com.gilt.thehand

import org.scalatest.{FlatSpec, Matchers}
import com.gilt.thehand.exceptions.CannotDeserializeException

/**
 * This is a trait that can be used to test custom rules in a comprehensive manner. All classes that inherit from this
 * trait should call the 'runTests' method with a Map of Rules to a tuple containing two lists: the first a list of
 * contexts that should match the rule, the second a list of contexts that should not match the rule. See @LongInSpec
 * for an example.
 */
trait RuleSpec extends FlatSpec with Matchers {
  lazy val parser: AbstractRuleParser = DefaultParser

  /**
   * Call this method in your inheriting class to run the tests.
   *
   * @param testCases A list of rules and the contexts that it should and shouldn't match against.
   * @tparam T A general Rule type.
   * @tparam U The general type of context that the rule successfully matches against.
   * @tparam V The general type of context that the rule unsuccessfully matches against.
   */
  def runTests[T <: Rule, U <: AbstractContext, V <: AbstractContext](testCases: Map[T, (Set[U], Set[V])]) {
    testCases foreach { case (rule, (matchingContexts, nonMatchingContexts)) =>
      rule.toString should "serialize and deserialize using fromString" in {
        val serializedRule = rule.toString
        val deserializedRule = parser.fromString(serializedRule)
        deserializedRule should be (rule)
        assert(serializedRule match {
          case parser(rule) => true
          case _ => false
        })
      }

      it should "serialize and deserialize using the extractor" in {
        val serializedRule = rule.toString
        serializedRule match {
          case parser(deserializedRule) => deserializedRule should be (rule)
          case _ => throw new CannotDeserializeException(s"Could not deserialize [$serializedRule]")
        }
      }

      matchingContexts foreach { context =>
        it should s"match $context" in {
          // Match using 'matches'
          assert(rule.matches(context))
          // Match using the extractor
          assert(context match {
            case rule(c) => true
            case _ => false
          })
        }
      }

      nonMatchingContexts foreach { context =>
        it should s"not match $context" in {
          // Attempt match using 'matches'
          assert(!rule.matches(context))
          // Attempt match using the extractor
          assert(context match {
            case rule(c) => false
            case _ => true
          })
        }
      }
    }
  }
}
