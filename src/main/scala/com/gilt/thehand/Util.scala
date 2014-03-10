package com.gilt.thehand

/**
 * A set of utility functions.
 */
object Util {
  /**
   * This takes a given string and splits it by a given delimiter, keeping track of delimiters that indicate opening and
   * closing of nested objects. The primary use case is to split by comma while ignoring commas that appear inside of
   * parenthesis. Example:
   *
   * And(True, False), Or(Not(False), False), True
   *
   * should split to these values:
   * [1] And(True, False)
   *
   * [2] Or(Not(False), False)
   *
   * [3] True
   *
   * @param str The string to split.
   * @param listDelimiter The delimiter used to split the members of the list; defaults to ','.
   * @param openDelimiter The delimiter used to indicate the start of a nested list; defaults to '('.
   * @param closeDelimiter The delimiter used to indicate the end of a nested list; defaults to ')'.
   * @return The list of individual members.
   */
  def nestedSplit(str: String, listDelimiter: Char = ',', openDelimiter: Char = '(', closeDelimiter: Char = ')'): Seq[String] = {
    val (_, result, remaining) = str.foldLeft((0, Seq.empty[String], "")) { case ((numParens, agg, current), c) =>
      assert(openDelimiter != closeDelimiter, "Cannot call nestedSplit with the same character for the open and close delimiters")
      assert(openDelimiter != listDelimiter, "Cannot call nestedSplit with the same character for the open and list delimiters")
      assert(closeDelimiter != listDelimiter, "Cannot call nestedSplit with the same character for the close and list delimiters")
      c match {
        case `openDelimiter` => (numParens + 1, agg, current + c)
        case `closeDelimiter` => (numParens - 1, agg, current + c)
        case `listDelimiter` => if (numParens <= 0) (0, agg ++ Seq(current), "")
        else (numParens, agg, current + c)
        case _ => (numParens, agg, current + c)
      }
    }
    result ++ Seq(remaining)
  }
}
