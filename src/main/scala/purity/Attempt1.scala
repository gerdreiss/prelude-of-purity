package purity

import cats.data.*
import cats.implicits.*

/**
 * This solution works, but handles errors poorly.
 */

object Attempt1:
  type Stack = List[Int]

  type Eff[A] = State[Stack, A]

  def push(x: Int): Eff[Unit] =
    State.modify(x :: _)

  val pop: Eff[Int] =
    for
      stack <- State.get
      x     <- State.set(stack.tail).as(stack.head)
    yield x

  def getExprElements(expr: String): List[String] =
    expr.split(" ").toList

  def evalRPNExpression(elements: List[String]): Int =
    def processElement(element: String): Eff[Unit] =
      element match
        case "+" => processTopElements(_ + _)
        case "-" => processTopElements(_ - _)
        case "*" => processTopElements(_ * _)
        case x   => push(x.toInt)

    def processTopElements(f: (Int, Int) => Int): Eff[Unit] =
      for
        x <- pop
        y <- pop
        _ <- push(f(x, y))
      yield ()

    (elements.traverse(processElement) *> pop).runA(List.empty).value

  def run = evalRPNExpression(getExprElements("1 2 + 3 *"))
