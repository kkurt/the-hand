package com.gilt.thehand

/**
 * A common base type to link all contexts.
 */
trait AbstractContext

/**
 * Use this to define a typed context under which a rule can operate. Rules can react to multiple contexts internally.
 */
case class Context[+T](instance: T) extends AbstractContext
