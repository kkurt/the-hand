package com.gilt.thehand

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class UtilSpec extends FlatSpec with ShouldMatchers {

  "Util.nestedSplit" should "work for a simple split" in {
    Util.nestedSplit("1,2,3,4") should be (Seq("1", "2", "3", "4"))
  }

  it should "do a simple split with a non-standard delimiter" in {
    Util.nestedSplit("1|2,3|4", '|') should be (Seq("1", "2,3", "4"))
  }

  it should "do a nested split" in {
    Util.nestedSplit("1,(2,3),(4,(5,(6,7)))") should be (Seq("1", "(2,3)", "(4,(5,(6,7)))"))
  }

  it should "do a nested split with a non-standard delimiter" in {
    Util.nestedSplit("1,(2|3)|(4|(5|(6|7)))", '|') should be (Seq("1,(2|3)", "(4|(5|(6|7)))"))
  }

  it should "do a nested split with a non-standard opening delimiter" in {
    Util.nestedSplit("1,{2,3),{(4,{5,{6,7)),)", openDelimiter = '{') should be (Seq("1", "{2,3)", "{(4,{5,{6,7)),)"))
  }

  it should "do a nested split with a non-standard closing delimiter" in {
    Util.nestedSplit("1,(2,3},(4,(5,(6,7)},}}", closeDelimiter = '}') should be (Seq("1", "(2,3}", "(4,(5,(6,7)},}}"))
  }

  it should "do a nested split with all non-standard delimiters" in {
    Util.nestedSplit("1,{(2|3|}|{4|{5|{6|7}}}", '|', '{', '}') should be (Seq("1,{(2|3|}", "{4|{5|{6|7}}}"))
  }

}
