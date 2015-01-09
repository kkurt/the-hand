package com.gilt.thehand.test

import com.gilt.thehand.{AbstractRuleParser, AbstractContext, Rule}
import com.gilt.thehand.exceptions.CannotDeserializeException

/**
 * Implement this trait in order to provide standardized testing for your custom rules. Your implementation will need to
 * mix in your preferred testing framework in order to run the tests; see @AbstractRuleSpec in the testing section of
 * this project for an example.
 */
trait RuleTester {
  /**
   * Define a parser that is specific to the custom rule being tested.
   */
  val parser: AbstractRuleParser

  /**
   * Implement this within your chosen test framework; see @AbstractRuleSpec for an example.
   *
   * The 'tests' map should be thought of like this:
   *
   * testGroupName -> (
   *  testDescription1 -> test1,
   *  testDescription2 -> test2,
   *  ...
   * )
   */
  def runTestsInFramework(tests: Map[String, Map[String, () => Unit]])

  /**
   * Call this method in your inheriting class to run the tests.
   *
   * @param testCases A list of rules and the contexts that it should and shouldn't match against.
   * @tparam T A general Rule type.
   * @tparam U The general type of context that the rule successfully matches against.
   * @tparam V The general type of context that the rule unsuccessfully matches against.
   */
  def runTests[T <: Rule, U <: AbstractContext, V <: AbstractContext](testCases: Map[T, (Set[U], Set[V])]) {
    val tests: Map[String, Map[String, () => Unit]] = testCases map { case (rule, (matchingContexts, nonMatchingContexts)) =>
      rule.toString -> (Map(
          "serialize and deserialize using fromString" -> (() => {
          val serializedRule = rule.toString
          val deserializedRule = parser.fromString(serializedRule)
          assert(deserializedRule == rule)
          assert(serializedRule match {
            case parser(rule) => true
            case _ => false
          })
        })) ++

        Map(
          "serialize and deserialize using the extractor" -> (() => {
          val serializedRule = rule.toString
          serializedRule match {
            case parser(deserializedRule) => assert(deserializedRule == rule)
            case _ => throw new CannotDeserializeException(s"Could not deserialize [$serializedRule]")
          }
        })) ++

        matchingContexts.map { context =>
          s"match $context" -> (() => {
            // Match using 'matches'
            assert(rule.matches(context))
            // Match using the extractor
            assert(context match {
              case rule(c) => true
              case _ => false
            })
          })
        } ++

        nonMatchingContexts.map { context =>
          s"not match $context" -> (() => {
            // Attempt match using 'matches'
            assert(!rule.matches(context))
            // Attempt match using the extractor
            assert(context match {
              case rule(c) => false
              case _ => true
            })
          })
        }
      )
    }
    runTestsInFramework(tests)
  }
}
