package com.gilt.thehand

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class UtilSpec extends FlatSpec with Matchers {

  "Util.nestedSplit" should "work for a simple split" in {
    Util.nestedSplit("1,2,3,4") shouldBe Seq("1", "2", "3", "4")
  }

  it should "do a simple split with a non-standard delimiter" in {
    Util.nestedSplit("1|2,3|4", '|') shouldBe Seq("1", "2,3", "4")
  }

  it should "do a nested split" in {
    Util.nestedSplit("1,(2,3),(4,(5,(6,7)))") shouldBe Seq("1", "(2,3)", "(4,(5,(6,7)))")
  }

  it should "do a nested split with a non-standard delimiter" in {
    Util.nestedSplit("1,(2|3)|(4|(5|(6|7)))", '|') shouldBe Seq("1,(2|3)", "(4|(5|(6|7)))")
  }

  it should "do a nested split with a non-standard opening delimiter" in {
    Util.nestedSplit("1,{2,3),{(4,{5,{6,7)),)", openDelimiter = '{') shouldBe Seq("1", "{2,3)", "{(4,{5,{6,7)),)")
  }

  it should "do a nested split with a non-standard closing delimiter" in {
    Util.nestedSplit("1,(2,3},(4,(5,(6,7)},}}", closeDelimiter = '}') shouldBe Seq("1", "(2,3}", "(4,(5,(6,7)},}}")
  }

  it should "do a nested split with all non-standard delimiters" in {
    Util.nestedSplit("1,{(2|3|}|{4|{5|{6|7}}}", '|', '{', '}') shouldBe Seq("1,{(2|3|}", "{4|{5|{6|7}}}")
  }

}
