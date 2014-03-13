package com.gilt.thehand.rules

import com.gilt.thehand.rules.conversions.ConvertsTo

/**
 * A general interface that can be used to implement an In rule for any datatype. In practice, this is used mostly
 * for parsing, to differentiate, say, In[String] from In[Long].
 */
trait In extends SeqRule { self: ConvertsTo =>
  /**
   * The rule matches if the value is in the list of values.
   * @param value The value to match against.
   * @return
   */
  def matchInnerType(value: InnerType) = values.contains(value)
}
