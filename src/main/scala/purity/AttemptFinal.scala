package purity

import zio.prelude.*

object AttemptFinal:

  type Stack = List[Int]

  type Eff[+A] = EState[Stack, String, A] // ZPure[Nothing, Stack, Stack, Any, String, A]

  val pop: Eff[Int] =
    for
      stack <- EState.get[Stack]
      x     <- stack match
                 case head :: tail => EState.set(tail).as(head)
                 case _            => EState.fail("No operands left")
    yield x

  def push(x: Int): Eff[Unit] =
    EState.update(_ :+ x)

  def getExprElements(expr: String): List[String] =
    expr.split(" ").toList

  def evalRPNExpression(elements: List[String]): Either[String, Int] =
    def processElement(element: String): Eff[Unit] =
      element match
        case "+" => processTopElements(_ + _)
        case "-" => processTopElements(_ - _)
        case "*" => processTopElements(_ * _)
        case x   =>
          EState
            .fromOption(x.toIntOption)
            .flatMap(push)
            .orElseFail(s"Invalid operand: $x")

    def processTopElements(operator: (Int, Int) => Int): Eff[Unit] =
      for
        x <- pop
        y <- pop
        _ <- push(operator(x, y))
      yield ()

    // Here we use 'forEach' from the ForEach Typeclass
    elements
      .forEach(processElement)
      .zipRight(pop)
      .provideState(List.empty[Int])
      .runEither

  def run: Either[String, Int] =
    evalRPNExpression(getExprElements("1 2 + 3 *"))
