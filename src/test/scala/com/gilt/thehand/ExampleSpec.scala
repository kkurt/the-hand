package com.gilt.thehand

import com.gilt.thehand.rules.logical.{Or, And, Not}
import com.gilt.thehand.rules.{SeqRuleParser, SingleValueRule}
import com.gilt.thehand.rules.conversions.ConvertsTo
import com.gilt.thehand.rules.comparison.{In, LessThanEqual}
import com.gilt.thehand.exceptions.CannotDeserializeException
import org.scalatest.matchers.ShouldMatchers

/**
 * This is an example of how you might implement custom rules, taking you through the setps sequentially.
 * It also show how to use the rules.
 */
class ExampleSpec extends AbstractRuleSpec with ShouldMatchers {

  /**
   * 1. Define some custom objects.
   */
  // Various types of weather.
  object WeatherType extends Enumeration {
    type WeatherType = Value
    val Clouds, Rain, Snow, Sun, Wind = Value
  }
  import WeatherType._

  // A class for temperature.
  object DegreeType extends Enumeration {
    type DegreeType = Value
    val Celsius, Fahrenheit = Value
  }
  import DegreeType._

  case class Temperature(degrees: BigDecimal, degreeType: DegreeType = Fahrenheit) extends Ordered[Temperature] {
    lazy val inF = if (degreeType == Fahrenheit) degrees else ((BigDecimal(9) / 5) * degrees) + 32
    lazy val inC = if (degreeType == Celsius) degrees else (BigDecimal(5) / 9) * (degrees - 32)

    def compare(that: Temperature) = degreeType match {
      case Fahrenheit => degrees compare that.inF
      case Celsius => degrees compare that.inC
    }

    override def toString = "Temperature(%s, %s)".format(degrees, degreeType)
  }

  // A class to represent the current weather.
  case class Weather(temperature: Temperature, weatherType: WeatherType)

  // Some classes to represent a person's choice on which type of jacket to wear.
  object JacketType extends Enumeration {
    type JacketType = Value
    val Winter, Light, Rain = Value
  }
  import JacketType._

  case class Person(wearWinterJacket: Rule, wearLightJacket: Rule, wearRainJacket: Rule, wearNoJacket: Rule) {
    def whichJacket(context: Context[Weather]): Option[JacketType] = context match {
      case `wearWinterJacket`(context) => Some(Winter)
      case `wearRainJacket`(context) => Some(JacketType.Rain)
      case `wearLightJacket`(context) => Some(Light)
      case `wearNoJacket`(context) => None
      case _ => throw new RuntimeException("I don't have a rule for this weather! %s".format(context.instance))
    }
  }

  /**
   * 2. Define rules related to the custom objects. The main points to remember are:
   */
  case class TemperatureLte(value: Temperature) extends SingleValueRule with ConvertsTo with LessThanEqual {
    type InnerType = Temperature

    /**
     * 2a. Your rule can apply to many different context types by implementing the toRuleType method to translate
     *     from those other types to the type that the rule cares about.
     */
    def toRuleType = {
      case t: Temperature => t
      case w: Weather => w.temperature
      case bd: BigDecimal => Temperature(bd)
      case i: Int => Temperature(BigDecimal(i))
      case l: Long => Temperature(BigDecimal(l))
    }

    /**
     * 2b. Your rule should be specific how it is represented as a string. This will be used later when parsing the
     *     string back into the Rule.
     */
    override def toString = "TemperatureLte(%s)".format(value)
  }

  /**
   * 2c. Define a companion object to parse the rule from a string into the Rule itself.
   */
  object TemperatureLteParser extends AbstractRuleParser {
    def unapply(deserializeFrom: String): Option[Rule] = {
      val matchRegEx = "TemperatureLte\\(Temperature\\((.+), (Fahrenheit|Celsius)\\)\\)".r
      deserializeFrom match {
        case matchRegEx(degreesStr, degreeTypeStr) => Some(TemperatureLte(Temperature(BigDecimal(degreesStr), DegreeType.withName(degreeTypeStr))))
        case _ => None
      }
    }
  }

  /**
   * Note: This is a completely different rule, but has the same characteristics as 2a - 2c above.
   */
  case class WeatherTypeIn(values: WeatherType*) extends In with ConvertsTo {
    type InnerType = WeatherType

    def toRuleType = {
      case w: WeatherType => w
      case w: Weather => w.weatherType
      case s: String if WeatherType.values.map(_.toString).contains(s) => WeatherType.withName(s)
    }
  }
  object WeatherTypeInParser extends SeqRuleParser[WeatherTypeIn] {
    override def ruleConstructor(values: Seq[WeatherType]) = WeatherTypeIn(values: _*)
    def toValue(value: String) = WeatherType.withName(value)
  }

  /**
   * 3. Use the rules.
   */
  val alice = Person(
    wearWinterJacket = Or(TemperatureLte(Temperature(30)), WeatherTypeIn(Snow)),
    wearLightJacket = Or(TemperatureLte(Temperature(60)), And(TemperatureLte(Temperature(70)), WeatherTypeIn(Clouds, Wind))),
    wearRainJacket = WeatherTypeIn(WeatherType.Rain),
    wearNoJacket = Not(TemperatureLte(Temperature(60)))
  )
  val bob = Person(
    wearWinterJacket = Or(TemperatureLte(Temperature(0, Celsius))),
    wearLightJacket = Or(TemperatureLte(Temperature(19, Celsius)), And(TemperatureLte(Temperature(23, Celsius)), WeatherTypeIn(Clouds))),
    wearRainJacket = And(WeatherTypeIn(WeatherType.Rain), TemperatureLte(Temperature(25, Celsius))),
    wearNoJacket = Not(TemperatureLte(Temperature(19, Celsius)))
  )

  /**
   * 4. Define a parser that contains your custom parsers. If you have no custom parsers, you can import
   *    com.gilt.thehand.DefaultParser. In normal usage, this won't be an override and can be named anything; within
   *    RuleSpec, you will need to override the `parser` val.
   */
  override val parser = RuleParser(WeatherTypeInParser, TemperatureLteParser)

  /**
   * 5. Set up standard testing by using RuleSpec.
   */
  val temperatureTestCases = Map(
    TemperatureLte(Temperature(30)) -> (
        Set(Context(Weather(Temperature(30), Snow)), Context(Temperature(30)), Context(29), Context(28L), Context(BigDecimal(29.9)), Context(Temperature(-1.2, Celsius))),
        Set(Context(Weather(Temperature(31), Snow)), Context(Temperature(30.1)), Context(31), Context(32L), Context(BigDecimal(30.1)), Context(Temperature(-1.1, Celsius)))
      ),
    TemperatureLte(Temperature(30, Celsius)) -> (
        Set(Context(Weather(Temperature(86), Sun)), Context(Temperature(86)), Context(85), Context(84L), Context(BigDecimal(85.9)), Context(Temperature(30, Celsius))),
        Set(Context(Weather(Temperature(87), Wind)), Context(Temperature(86.1)), Context(87), Context(88L), Context(BigDecimal(86.1)), Context(Temperature(30.1, Celsius)))
      )
  )
  runTests(temperatureTestCases)

  val weatherTypeTestCases = Map(
    WeatherTypeIn(Sun, Wind) -> (
        Set(Context(Sun), Context(Wind)),
        Set(Context(Snow), Context(Clouds))
      ),
    WeatherTypeIn(Wind) -> (
        Set(Context(Wind)),
        Set(Context(Sun), Context(Snow), Context(Clouds))
      )
  )
  runTests(weatherTypeTestCases)

  /**
   * 6. Set up custom tests.
   */
  // Some weather conditions
  val coldButSunny = Context(Weather(Temperature(31), Sun))
  val snowingAbove30 = Context(Weather(Temperature(31), Snow))
  val windyAnd70 = Context(Weather(Temperature(21.1, Celsius), Wind))
  val cloudyAnd70 = Context(Weather(Temperature(21.1, Celsius), Clouds))
  val rainingButWarm = Context(Weather(Temperature(27, Celsius), WeatherType.Rain))

  "A Person can use its own rules to decide which jacket based on the weather" should "work when loaded in code" in {
    // Alice and Bob use different rules to determine which jacket to wear. They may choose different jackets in
    // different weather.
    alice.whichJacket(coldButSunny) should be (Some(Light))
    bob.whichJacket(coldButSunny) should be (Some(Winter))

    alice.whichJacket(snowingAbove30) should be (Some(Winter))
    bob.whichJacket(snowingAbove30) should be (Some(Winter))

    alice.whichJacket(windyAnd70) should be (Some(Light))
    bob.whichJacket(windyAnd70) should be (None)

    alice.whichJacket(cloudyAnd70) should be (Some(Light))
    bob.whichJacket(cloudyAnd70) should be (Some(Light))

    alice.whichJacket(rainingButWarm) should be (Some(JacketType.Rain))
    bob.whichJacket(rainingButWarm) should be (None)
  }

  it should "work when loaded from a string" in {
    // Create Charlie to match Alice and David to match Bob, but from strings.
    val charlie = Person(
      wearWinterJacket = parser.fromString("Or(TemperatureLte(Temperature(30, Fahrenheit)), WeatherTypeIn(Snow))"),
      wearLightJacket = parser.fromString("Or(TemperatureLte(Temperature(60, Fahrenheit)), And(TemperatureLte(Temperature(70, Fahrenheit)), WeatherTypeIn(Clouds, Wind)))"),
      wearRainJacket = parser.fromString("WeatherTypeIn(Rain)"),
      wearNoJacket = parser.fromString("Not(TemperatureLte(Temperature(60, Fahrenheit)))")
    )
    val david = Person(
      wearWinterJacket = parser.fromString(bob.wearWinterJacket.toString),
      wearLightJacket = parser.fromString(bob.wearLightJacket.toString),
      wearRainJacket = parser.fromString(bob.wearRainJacket.toString),
      wearNoJacket = parser.fromString(bob.wearNoJacket.toString)
    )

    charlie.whichJacket(coldButSunny) should be (Some(Light))
    david.whichJacket(coldButSunny) should be (Some(Winter))

    charlie.whichJacket(snowingAbove30) should be (Some(Winter))
    david.whichJacket(snowingAbove30) should be (Some(Winter))

    charlie.whichJacket(windyAnd70) should be (Some(Light))
    david.whichJacket(windyAnd70) should be (None)

    charlie.whichJacket(cloudyAnd70) should be (Some(Light))
    david.whichJacket(cloudyAnd70) should be (Some(Light))

    charlie.whichJacket(rainingButWarm) should be (Some(JacketType.Rain))
    david.whichJacket(rainingButWarm) should be (None)
  }

  /**
   * Example usage of how to parse and match rules in code.
   */

  "Matching Rules" should "work with the extractor" in {
    val aliceWinterJacket = alice.wearWinterJacket
    val bobWinterJacket = bob.wearWinterJacket
    coldButSunny match {
      case `aliceWinterJacket`(context) => assert(false)
      case `bobWinterJacket`(context) => assert(true)
      case _ => assert(false)
    }
  }

  it should "work with the matches method" in {
    alice.wearWinterJacket.matches(coldButSunny) should be (false)
    bob.wearWinterJacket.matches(coldButSunny) should be (true)
  }

  "Parsing Rules" should "work round-trip with the extractor" in {
    val rule = alice.wearWinterJacket.toString match {
      case parser(rule) => rule
      case _ => throw new CannotDeserializeException("Failed deserializing [%s]".format(alice.wearWinterJacket.toString))
    }
    rule should be (alice.wearWinterJacket)
  }

  it should "work round-trip with fromString" in {
    parser.fromString(alice.wearWinterJacket.toString) should be (alice.wearWinterJacket)
  }

  it should "work for a raw string with extractor" in {
    val rule = "And(TemperatureLte(Temperature(35, Celsius)), Not(WeatherTypeIn(Sun, Clouds)))" match {
      case parser(rule) => rule
      case _ => throw new CannotDeserializeException("Failed deserializing [%s]".format(alice.wearWinterJacket.toString))
    }
    rule should be (And(TemperatureLte(Temperature(35, Celsius)), Not(WeatherTypeIn(Sun, Clouds))))
  }

  it should "work for a raw string with fromString" in {
    parser.fromString("And(TemperatureLte(Temperature(35, Celsius)), Not(WeatherTypeIn(Sun, Clouds)))") should be (And(TemperatureLte(Temperature(35, Celsius)), Not(WeatherTypeIn(Sun, Clouds))))
  }

}
