package purity

// didn't even get this sh*t to compile...

// import cats.data.*
// import cats.implicits.*

// object Attempt3:

//   type Stack = List[Int]

//   type Eff[A] = EitherT[State[Stack, ?], String, A]

//   val pop: Eff[Int] =
//     for
//       stack <- EitherT.liftF(State.get[Stack])
//       x     <- stack match
//                  case head :: tail => EitherT.liftF[State[Stack, ?], String, Int](State.set(tail).as(head))
//                  case _            => EitherT.left[State[Stack, ?], Int]("No operands left")
//     yield x
