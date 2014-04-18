The Hand
========

A serializable rules engine.

When you use this library, you - the developer - are king (or queen). The king defines the rules; the Hand of the King
writes down the king's rules and enforces them.

## Goals Of This Library

This library sets out to achieve two main goals:

1. Be able to define rules in a human-readable manner.
2. Be able to serialize those rules into a human-readable string (for storage) and back into strongly-typed objects.

Of course, this library only provides the foundation of a rules engine; as such a secondary goal is to be easily
extensible so that clients can define their own custom rules with minimal overhead.


## Why Is This Useful?

This library excels when you have a use for customizable rules that fall outside of a simple if-then-else structure,
specifically if those rules need to be stored as strings (i.e. in a database or a config file). Furthermore, for
developing highly-specific and greatly-varying logical cases, this library is much easier to expand than a collection
of if-then/match/etc statements. But, really, the best explanation of its usefulness is to look at the example in
"Getting Started" below. Also check out a full example in the [ExampleSpec](../../blob/master/src/test/scala/com/gilt/thehand/ExampleSpec.scala).


## Getting Started

Let's look at a rule system built around these two example classes:

```
    // An enum for a limited list of currencies
    object Currency extends Enumeration {
      type Currency = Value
      val CAD, CNY, EUR, MXN, USD = Value
    }
    import Currency._ // Allow access to all Currency enums

    case class Money(amount: BigDecimal, currency: Currency)
```

In order to build a rule system around them, you would follow these steps:

### Step 1: Define a Rule

To define a new Rule, implement the [Rule trait](../../blob/master/src/main/scala/com/gilt/thehand/Rule.scala) - which
is as simple as defining the [`unapply` method](../../blob/master/src/main/scala/com/gilt/thehand/Rule.scala#L16) for the
Rule; you may optionally need to override `toString` if the default `toString` behavior does not match your parser.

```
    case class CurrencyIn(values: Currency*) extends Rule {
      def unapply(context: AbstractContext): Option[AbstractContext] = context match {
        case Context(c: Currency) if values.contains(c) => Some(context)
        case Context(m: Money) if values.contains(m.currency) => Some(context)
        case Context(s: String) if Currency.values.exists(_.toString == s.toUpperCase) && values.contains(Currency.withName(s.toUpperCase)) => Some(context)
        case _ => None
      }

      override def toString = "CurrencyIn(%s)".format(values.map(_.toString).mkString(", "))
    }
```

A few notes about the sample rule above:

1. `unapply` is used here to support the concept of [Scala Extractors](http://docs.scala-lang.org/tutorials/tour/extractor-objects.html);
examples of extractor usage follow below. The Rule trait automatically adds in a convenience method if you don't want
to use the extractor: [`def matches`](../../blob/master/src/main/scala/com/gilt/thehand/Rule.scala#L23).
2. The input to `unapply` is the [Context](../../blob/master/src/main/scala/com/gilt/thehand/Context.scala) under which
the rule operates. It accepts any type of AbstractContext, but in practice it's easiest to pattern-match using the
type-parameterized Context class to match on the inner type of that Context. _This allows the Rule above to match not
only on Currency, but also Money and String._ The ability for a Rule to match multiple types is one of the main features
of this library, and is what raises it above a simple if-then-else.
3. When defining `unapply` you return an Option: Some if the rule matches the context, None if it does not (the
extractor pattern wraps this output and knows what to do with the Option). Inside the Some, you return the matching
context, which allows extractors to be chained together.
4. The `values: Currency*` allows the CurrencyIn rule to be instantiated using any number of currencies: CurrencyIn(USD)
or CurrencyIn(USD, CAD, CNY). Under the hood, this will be an array that can be tested against.
5. It is best to override `toString` here for parsing purposes. The default `toString` would output
`CurrencyIn(WrappedArray(CAD, MXN, USD))`; the version above will produce `CurrencyIn(CAD, MXN, USD)`, which makes more
sense for both readability and parsing.


### Step 2: Define a RuleParser

Once you have defined a new rule, now you need to be able parse a String into that rule. Given the rule above (and its
`toString` method, let's take a look at the string to be parsed:

```
    scala> val isNorthAmericanCurrency = CurrencyIn(CAD, MXN, USD)
    isNorthAmericanCurrency: CurrencyIn = CurrencyIn(CAD, MXN, USD)

    scala> isNorthAmericanCurrency.toString
    res1: String = CurrencyIn(CAD, MXN, USD)
```

To parse this string, implement the [`AbstractRuleParser` trait](../../blob/master/src/main/scala/com/gilt/thehand/RuleParser.scala#L11),
which is as simple as implementing the [`unapply` method](../../blob/master/src/main/scala/com/gilt/thehand/RuleParser.scala#L17):

```
    object CurrencyInParser extends AbstractRuleParser {
      def unapply(deserializeFrom: String): Option[Rule] = {
        val matchRegEx = "CurrencyIn\\((.+)\\)".r
        deserializeFrom match {
          case matchRegEx(valuesStr) => Some(CurrencyIn(valuesStr.split(',').map(s => Currency.withName(s.trim)): _*))
          case _ => None
        }
      }
    }
```

A few notes about the sample parser above:

1. Again, `unapply` is used to expose the Scala Extractor pattern; in this case, the result is the Rule that is
extracted from the String (or a None if the Rule can't be parsed from the String). The AbstractRuleParser also adds in
a convenience method ([`fromString`](../../blob/master/src/main/scala/com/gilt/thehand/RuleParser.scala#L25)) if you don't
want to use the extractor. The input to `unapply` is the string to be parsed.
2. This is an `object` because it is a static method and doesn't depend on any internal variables.
3. The parsing basically boils down to "pull out the comma-delimited list between 'CurrencyIn(' and ')' and map that
to the Currency enum.


### Step 3: Instantiate a custom parser

The example parser above works great for the CurrencyIn Rule - but knows nothing about other types of rules. You will
need to build your other custom Rules and RuleParsers, then combine them together using the [`RuleParser` case class](../../blob/master/src/main/scala/com/gilt/thehand/RuleParser.scala#L35):

```
    val myParser = RuleParser(CurrencyInParser)
```

This will also mix in default parsers for things like And, Or, Not, etc. With this, you'll be able to define more
sophisticated, combined Rules:

```
    scala> val combinedRule = myParser.fromString("And(CurrencyIn(USD, CAD, CNY), Not(CurrencyIn(USD, MXN, EUR)))")
    combinedRule: com.gilt.thehand.Rule = And(CurrencyIn(USD, CAD, CNY), Not(CurrencyIn(USD, MXN, EUR)))
```

### Step 4: Use the RuleParser and Rule

As referenced above, this library supports two modes of processing: Scala Extractors and convenience methods; you can
use either one, depending on which is more readable in your code. To parse strings, here is how the `myParser` above
would be used:

```
    // Scala Extractor
    "And(CurrencyIn(USD, CAD, CNY), Not(CurrencyIn(USD, MXN, EUR)))" match {
      case myParser(rule) => // Do something with the rule
      case _ => // Do something different when the string cannot be parsed
    }
```

or

```
    // Convenience method, this will throw a CannotDeserializeException in the failure case
    val myRule = myParser.fromString("And(CurrencyIn(USD, CAD, CNY), Not(CurrencyIn(USD, MXN, EUR)))")
```

In turn, once you have a Rule, you can match a context in two different ways:

```
    // Scala Extractor
    Context(USD) match {
      case myRule(context) => // Do something when Rule matches
      case _ => // Do something else when Rule does not match
    }
```

or

```
    // Convenience method
    if myRule.matches(Context(USD)) // Do something when Rule matches
    else // Do something else when Rule does not match
```

Note that because of the multiple Context-matching in the Rule definition, the Rule matching is not limited to only
the Currency enum; it also won't throw an exception for Contexts is doesn't know about:

```
    scala> myRule.matches(Context(USD)) // false because of the Rule logic
    res1: Boolean = false

    scala> myRule.matches(Context(CAD)) // Matches the Currency
    res2: Boolean = true

    scala> myRule.matches(Context("CAD")) // Matches the String
    res3: Boolean = true

    scala> myRule.matches(Context(Money(10.00, CAD))) // Matches the Money
    res4: Boolean = true

    scala> myRule.matches(Context(10.00)) // Doesn't know about Double
    res5: Boolean = false
```


### Step 5: Bringing it all together

At first glance, you wouldn't be blamed for asking, "Why all this Rule/Parsing overhead when I can accomplish this with
a bunch of if-thens?" In response to that, let's build out the example slightly more.

```
    // A rule based on a given amount
    case class AmountAtLeast(value: BigDecimal) extends Rule {
      def unapply(context: AbstractContext): Option[AbstractContext] = context match {
        case Context(m: Money) if m.amount >= value => Some(context)
        case Context(d: BigDecimal) if d >= value => Some(context)
        case Context(d: Double) if BigDecimal(d) >= value => Some(context)
        case _ => None
      }
    }

    // A parser for the rule above
    object AmountAtLeastParser extends AbstractRuleParser {
      def unapply(deserializeFrom: String): Option[Rule] = {
        val matchRegEx = "AmountAtLeast\\((\\d+(\\.\\d+)?)\\)".r
        deserializeFrom match {
          case matchRegEx(valueStr, _) => Some(AmountAtLeast(BigDecimal(valueStr)))
          case _ => None
        }
      }
    }

    // A definition of a Bank, which will determine when that bank will accept a deposit
    case class Bank(name: String, acceptDepositRule: Rule) {
        def acceptDeposit(m: Money) = acceptDepositRule.matches(Context(m))
    }

    // Include all of the custom rules in our parser
    val bankParser = RuleParser(CurrencyInParser, AmountAtLeastParser)
```

Given the bank class, we can now define a number of different types of banks:

```
    val creditUnionUS = Bank("US Credit Union", And(AmountAtLeast(0), CurrencyIn(USD, CAD))) // Small credit union in the US accepts USD or CAD
    val creditUnionCA = Bank("CA Credit Union", Or(And(AmountAtLeast(10), CurrencyIn(CAD)), And(AmountAtLeast(100), CurrencyIn(USD)))) // Small Canadian CU accepts CAD or USD when deposit is $100 or more
    val largeInternational = Bank("International Bank", AmountAtLeast(0)) // Large international bank accepts any deposit
```

As you can see, the definition of these very different banks - and the rule that determines when they will accept a
deposit - can vary greatly. But by using the Rule system, the code to make the "valid deposit" decision is simple:

```
    scala> creditUnionUS.acceptDeposit(Money(10, USD))
    res1: Boolean = true

    scala> creditUnionCA.acceptDeposit(Money(10, USD))
    res2: Boolean = false

    scala> largeInternational.acceptDeposit(Money(10, USD))
    res3: Boolean = true

    scala> creditUnionUS.acceptDeposit(Money(10, CAD))
    res4: Boolean = true

    scala> creditUnionCA.acceptDeposit(Money(10, CAD))
    res5: Boolean = true

    scala> largeInternational.acceptDeposit(Money(10, CAD))
    res6: Boolean = true

    scala> creditUnionUS.acceptDeposit(Money(10, CNY))
    res7: Boolean = false

    scala> creditUnionCA.acceptDeposit(Money(10, CNY))
    res8: Boolean = false

    scala> largeInternational.acceptDeposit(Money(10, CNY))
    res9: Boolean = true

    scala> creditUnionUS.acceptDeposit(Money(100, USD))
    res10: Boolean = true

    scala> creditUnionCA.acceptDeposit(Money(100, USD))
    res11: Boolean = true

    scala> largeInternational.acceptDeposit(Money(100, USD))
    res12: Boolean = true

    scala> creditUnionUS.acceptDeposit(Money(100, CNY))
    res13: Boolean = false

    scala> creditUnionCA.acceptDeposit(Money(100, CNY))
    res14: Boolean = false

    scala> largeInternational.acceptDeposit(Money(100, CNY))
    res15: Boolean = true

    scala> creditUnionUS.acceptDeposit(Money(-1, USD))
    res16: Boolean = false

    scala> creditUnionCA.acceptDeposit(Money(-1, USD))
    res17: Boolean = false

    scala> largeInternational.acceptDeposit(Money(-1, USD))
    res18: Boolean = false
```

Expanding upon this, the banks above could be stored in a database (or config file) and loaded from a column/String.
A very rough example:

```
    val sql = "select name, rule from banks"
    val rs = conn.select(sql)
    val banks = rs.map { row =>
        Bank(row["name"], bankParser.fromString(rs["rule"]))
    }
    val myDeposit = Money(10, USD)
    val banksThatWillAcceptMyDeposit = banks.filter(_.acceptDeposit(myDeposit))
```

Since Rules are serializable, the bank can be loaded from storage with "constant" storage complexity even
as the complexity of the rule increases (e.g. the rule for creditUnionCA is stored no differently than the rule for
largeInternational - they're both just strings - even though the former is a much more complex rule).

Furthermore, when you need to add even more complexity - a new type of Rule, or a new Context - there is no need to
touch the existing Bank rules (or even the Bank object): just add your new Rule and start using it. Contrasting the Bank
example above against an alternate example that doesn't use Rules, it might look something like this:

```
    case class Bank2(name: String, acceptedDepositAmountsByCurrency: Map[Currency, BigDecimal], defaultAcceptableAmount: Option[BigDecimal]) {
      def acceptDeposit(m: Money) = {
        val minAmount: Option[BigDecimal] = acceptedDepositAmountsByCurrency.find { case (currency: Currency, amount: BigDecimal) =>
          currency == m.currency
        } map { case (currency: Currency, amount: BigDecimal) =>
          amount
        } orElse defaultAcceptableAmount
        minAmount.map(_ < m.amount).getOrElse(false)
    }

    val creditUnionUS = Bank2("US Credit Union", Map(USD -> 0, CAD -> 0), None)
    val creditUnionCA = Bank2("CA Credit Union", Map(USD -> 100, CAD -> 0), None)
    val largeInternational = Bank2("International Bank", Map.empty, Some(0))
```

Though the `val` definitions above seem relatively simple, the `acceptDeposit` method is fairly complex - and highly
specific to this use case. Plus, that Map could get extensive when adding many different currencies; think about how
this might be stored in a database (a secondary mapping table?). Then, if some completely new logic needs to be added
in, the model might completely change to support it - adding properties and columns that in the majority of cases (i.e.
existing banks) will not be needed. For example, maybe you want to group currencies by region - now Bank2 needs an
Option[Region] property, that gets added to the `acceptDeposit` method, a new column is added, existing data is updated;
if this was using a Rule system, you'd add a RegionIn Rule and start using it in the Banks that want to use it; nothing
else needs to change.


### Step 6: Testing

This library includes a trait that you should use to run a standard suite of tests against the Rules you develop:
[RuleSpec](../../blob/master/src/test/scala/com/gilt/thehand/RuleSpec.scala). Extend this trait with your own test classes
by defining a Map of Rules to a tuple of Sets of Contexts that either should match (first member of the tuple) or should
not match (second member of the tuple) the rule. The standard testing will make sure those examples are tested, in
addition to ensuring that your parser works correctly. Here is an example:

```
    class CurrencyInSpec extends RuleSpec {
      override val parser = RuleParser(CurrencyInParser) // You must override the default parser to include your customizations

      val testCases = Map(
        CurrencyIn(USD, CAD) -> (
            Set(Context(USD), Context("CAD")), // Should match successfully
            Set(Context(CNY), Context("MXN")) // Should not match
          ),
        CurrencyIn(CNY) -> (
            Set(Context(CNY)), // Should match successfully
            Set(Context(USD), Context(CAD), Context("MXN")) // Should not match
          )
      )

      runTests(testCases)
    }
```

Augment this standard testing to include additional tests that you care about. Examples of this can be seen throughout
the testing suite in this library.

Note that when using RuleSpec in your tests, you will need to include an additional line in your build file:

```
    "com.gilt" %% "the-hand" % "0.0.2" % "test->test" classifier "tests"
```


### Step 7: Helper classes (optional)

In Step 3 above, the example creates "raw" rules by implementing the very base traits. In practice, rules often end up
following one of a handful of formats: accepts a particular type, accepts _n_ values of a particular type, etc. With that
in mind, this library also includes some helper traits that allow you to skip implementation of `unapply` and simply
define how to parse into and out of your particular type:

1. [SingleValueRule](../../blob/master/src/main/scala/com/gilt/thehand/rules/SingleValueRule.scala)
2. [SeqRule](../../blob/master/src/main/scala/com/gilt/thehand/rules/SeqRule.scala)
3. [Eq, In, etc](../../tree/master/src/main/scala/com/gilt/thehand/rules/comparison)

For examples of how to use these, refer to:

1. [The Example](../../blob/master/src/test/scala/com/gilt/thehand/ExampleSpec.scala)
2. [The typed package](../../tree/master/src/main/scala/com/gilt/thehand/rules/typed)
3. [And, Or, etc](../../tree/master/src/main/scala/com/gilt/thehand/rules/logical)

In addition, your context is not limited to Context - you're able to add additional contexts by inheriting from the
AbstractContext trait.


## Design Decisions, Via a Q&A

### 1. Why roll your own parsing engine and not use, say, JSON?

Very good question. First, I wanted this library to have minimal dependencies and not have to rely on Jackson (or
something similar) for JSON parsing. Second, I wanted to make the serialized strings as readable as possible. For me,

```
    And(StringIn(foo, bar), Not(LongIn(1, 2, 3)))
```

is a lot more readable than

```
    {
        "type": "and",
        "values": [
            {
                "type": "string_in",
                "values": ["foo", "bar"]
            },
            {
                "type": "not",
                "value": {
                    "type": "long_in",
                    "values": [1, 2, 3]
                }
            }
        ]
    }
```

The JSON above is just one way how these rule object might be serialized, but you get the idea: a lot more verbose,
a much larger "document", somewhat difficult for a human to parse. The kicker is the difficulty of representing the
Scala class as part of the JSON document ("type" above, with accompanying attributes), which is awkward at best.


### 2. Why doesn't the serialized string include quotation marks around the strings?

Everything is coming from a string anyway (during deserialization), so it felt like unnecessary cruft - both in the
size of the serialized string and in the parsing logic. Adding non-quoted numerics only adds to the parsing complexity
(lots of escape characters, logic around matching quotes, quotes vs apostrophes, etc), while also adding the risk of
finding a non-quoted non-numeric and needing to throw an exception. Though there is something compelling about being
able to simply copy-paste the serialized string into the Scala REPL and have it be 100% functioning code, it didn't
quite offset the drawbacks.


### 3. Why cross-compile into Scala 2.9.x?

Not entirely sure, but it wasn't too hard to support Scala 2.9.x - so I did. Here's what we lose, that we might have
used in the code:

1. String interpolation (i.e. `s"This is my value: $value"` instead of `"This is my value %s".format(value)`).
2. Removing deprecated `manifest.erasure` and replacing it with `manifest.runtimeClass`.
3. Removing deprecated `org.scalatest.matchers.ShouldMatchers` and replacing it with `org.scalatest.Matchers`.

Those don't seem like a big deal, so until there is a more compelling reason to drop Scala 2.9 support, we'll keep it.


## Publishing

This project is published to Maven Central, using semver versioning. It is written in Scala 2.10.3 but is also
cross-compiled to 2.9.1, 2.9.2. If you're using SBT, you can include this library as a dependency like this:

```
libraryDependencies ++= Seq(
  "com.gilt" %% "thehand" % "0.0.2"
)
```

To publish, set up your environment based on the "Contributors" section below, then:

1. Run `sbt +test` to ensure that all cross-compiled versions pass the tests.
2. Assuming all tests pass, and replacing {x.x.x} below with the current version:
    1. Edit version.sbt to remove '-SNAPSHOT'
    2. `git add version.sbt`
    3. `git commit -m "Moving to version {x.x.x}"`
    4. `git tag {x.x.x}`
    5. `git push origin master`
    6. `git push --tag`
3. Run `sbt +publishSigned`
4. Move the version to the next snapshot:
    1. Edit version.sbt to add back in '-SNAPSHOT' and bump the version
    2. `git add version.sbt`
    3. `git commit -m "Moving to version {x.x.x}-SNAPSHOT"`
    5. `git push origin master`
5. Promote the release by following these steps: https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide#SonatypeOSSMavenRepositoryUsageGuide-8a.ReleaseIt

Note: The versioning above may eliminated at some point if we add this in: https://github.com/sbt/sbt-release/issues/49


## Contributors

If you would like to contribute to this project and would like to be able to publish new versions, you will need the
following (more in-depth instructions at http://www.scala-sbt.org/release/docs/Community/Using-Sonatype.html ):

1. Generate a GPG key pair
    1. Download GPG tools from http://gpgtools.org/
    2. Run `gpg --gen-key`
2. Create a Sonatype JIRA account
    1. https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide#SonatypeOSSMavenRepositoryUsageGuide-2.Signup
    2. Contact us to associate your account with this repository
3. Add your Sonatype JIRA credentials to ~/.sbt/0.13/sonatype.sbt

        credentials += Credentials("Sonatype Nexus Repository Manager",
                                   "oss.sonatype.org",
                                   "your-sonatype-username",
                                   "your-sonatype-password")

4. Make sure your public key is pushed to the remote keyserver:
    1. `gpg --list-public-keys`
    2. Find the hash for the one you just created.
    3. `gpg --keyserver hkp://keyserver.ubuntu.com --send-keys {your-hash-here}`
