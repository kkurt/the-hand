package com.gilt.thehand

import org.scalatest.FlatSpec
import com.gilt.thehand.test.RuleTester

/**
 * This is a trait that can be used to test custom rules in a comprehensive manner. All classes that inherit from this
 * trait should call the 'runTests' method with a Map of Rules to a tuple containing two lists: the first a list of
 * contexts that should match the rule, the second a list of contexts that should not match the rule. See @LongInSpec
 * for an example.
 */
class AbstractRuleSpec extends FlatSpec with RuleTester {
  val parser: AbstractRuleParser = DefaultParser

  def runTestsInFramework(tests: Map[String, Map[String, () => Unit]]) {
    tests foreach { case (groupName, tests) =>
      val (firstTestDescription, firstTest) = tests.head
      groupName should firstTestDescription in {
        firstTest()
      }

      tests.tail.foreach { case ((description, test)) =>
        it should description in {
          test()
        }
      }
    }
  }
}
