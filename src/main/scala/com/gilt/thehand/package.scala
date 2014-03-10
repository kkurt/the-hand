package com.gilt

package object thehand {
  /**
   * This can be used as a default parser when you aren't defining any custom rules.
   */
  implicit val DefaultParser = RuleParser()
}
