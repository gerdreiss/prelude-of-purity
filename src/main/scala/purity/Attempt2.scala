package purity

import cats.data.*
import cats.implicits.*

object Attempt2:

  type Stack = List[Int]

  type Eff[A] = State[Stack, Either[String, A]]

  val pop: Eff[Int] =
    for
      stack <- State.get
      x     <- stack match
                 case head :: tail => State.set(tail).as(Right(head))
                 case _            => State.pure(Left("No operands left"))
    yield x

  def getExprElements(expr: String): List[String] =
    expr.split(" ").toList

  def evalRPNExpression(elements: List[String]): Either[String, Int] =
    def processElement(element: String): Eff[Unit] = ??? // unreadable code

    def processTopElements(f: (Int, Int) => Int): Eff[Unit] = ??? // unreadable code

    ??? // unreadable code

  def run = evalRPNExpression(getExprElements("1 2 + 3 *"))
