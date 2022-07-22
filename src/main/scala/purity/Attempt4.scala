package purity

import zio.*
import java.io.IOException

object Attempt4:

  type Stack = Ref[List[Int]]

  type Eff[+A] = ZIO[Stack, String, A]

  val pop: Eff[Int] =
    for
      stackRef <- ZIO.service[Stack]
      stack    <- stackRef.get
      x        <- stack match
                    case head :: tail => stackRef.set(tail).as(head)
                    case _            => ZIO.fail("No operands left")
    yield x

  def push(x: Int): Eff[Unit] =
    ZIO.serviceWith[Stack](_.update(_ :+ x))

  def getExprElements(expr: String): List[String] =
    expr.split(" ").toList

  def evalRPNExpression(elements: List[String]): ZIO[Any, IOException, Either[String, Int]] =
    def processElement(element: String): Eff[Unit] =
      element match
        case "+" => processTopElements(_ + _)
        case "-" => processTopElements(_ - _)
        case "*" => processTopElements(_ * _)
        case x   => ZIO.from(x.toIntOption).orElseFail(s"Invalid operand: $x").flatMap(push)

    def processTopElements(operator: (Int, Int) => Int): Eff[Unit] =
      for
        x      <- pop
        y      <- pop
        result <- push(operator(x, y))
      yield result

    (ZIO.foreachDiscard(elements)(processElement) *> pop).either
      .provide(ZLayer(Ref.make(List.empty[Int])))

  def run = Unsafe.unsafe {
    Runtime.default.unsafe
      .run(evalRPNExpression(getExprElements("1 2 + 3 *")))
      .getOrThrowFiberFailure()
  }
