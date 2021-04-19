package com.pagero.codepatterns

import cats.data.{EitherT, OptionT}


import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import cats.implicits._
import io.github.hamsters.FutureOption
import io.github.hamsters.MonadTransformers._

object Example3 extends App {
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def dbOp1(inp: Int): Future[Option[String]] = {
    Future.successful(Some(s"Output1 $inp"))
    //    Future.successful(None)
  }

  def dbOp2(inp: String): Future[Option[String]] = {
    Future.successful(Some(s"Output2 $inp"))
    //    Future.successful(None)
  }


  def dbOpNone(): Future[Option[String]] = {
    Future.successful(None)
  }

  //  nestedCompose()
  //  composeWithLift()
  composeWithOptionT()
  composeWithEitherT()

  def nestedCompose() = {
    val composed = dbOp1(1).flatMap {
      case Some(val1) =>
        dbOp2(val1).flatMap {
          case Some(val2) =>
            Future.successful(s"$val2")
          case None => Future.failed(new Exception("db opt2 failed"))
        }
      case None => Future.failed(new Exception("db opt1 failed"))
    }

    val output = Await.result(composed, Duration.Inf)
    println(output)
  }

  def composeWithLift(): Unit = {
    val composed = for {
      val1 <- dbOp1(1)
      out1 <- lift(val1, "db opt1 failed")
      val2 <- dbOp2(out1)
      out2 <- lift(val2, "db opt2 failed")
    } yield out2

    val output = Await.result(composed, Duration.Inf)
    println(output)
  }

  def composeWithOptionT(): Unit = {
    val composed = for {
      out1 <- OptionT(dbOp1(1))
      out2 <- OptionT(dbOp2(out1))
    } yield out2

    val out = composed.value
    val output = Await.result(out, Duration.Inf)
    println(output)
  }

  def composeWithEitherT() = {
    val composed = for {
      out1 <- EitherT.fromOptionF(dbOp1(1), "db opt1 failed")
      out2 <- EitherT.fromOptionF(dbOp2(out1), "db opt2 failed")
    } yield out2

    val out = composed.value
    val output = Await.result(out, Duration.Inf)
    println(output)
  }

  def lift[T](value: Option[T], err: String): Future[T] = {
    if (value.isDefined) Future.successful(value.get)
    else Future.failed(new Exception(err))
  }

  //  def classicFutureOptionNest() = {
  //    //How doyou fix this ... optOpt1 is a Option[T]
  //        for {
  //          optOp1 <- dbOp1(1)
  //          //      optOp2 <- dbOp2(Some(optOp1)) //Wont even compile
  //        } yield {}
  //
  //
  //        //Because of this people write really bad code.
  //        dbOp1(1).flatMap {
  //          case Some(a) =>
  //            //with A
  //            //      write some code which wash your windows
  //            //      tie your shoes with a
  //            //      Comb your hair
  //            dbOp2(a).flatMap {
  //              case Some(b) =>
  //                // with b,
  //                //        Change the gravitation constant of the universe
  //                //        Disrupt spacetime continuum
  //                //
  //                dbOp3(b).flatMap {
  //                  case Some(c) =>
  //                    //with C
  //                    //        Charge your distruptors
  //                    //        Arm your phasers
  //                    //        Load your torpedoes
  //                    //        All hands battle stations
  //                    dbOp4(c).flatMap {
  //                      case Some(d) =>
  //                        // With D
  //                        //        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc tempus dolor vitae lectus consequat, vitae congue nulla fringilla.
  //                        //        Curabitur facilisis porttitor sem. Curabitur tempor ligula neque, vel molestie mauris interdum semper.
  //                        //        Vestibulum ut quam cursus, tempus risus vel, pretium magna. In mollis tristique risus, tempor
  //                        //        lobortis justo interdum eget. Quisque ut eros id elit sagittis commodo a ut ex. Cras lacinia
  //                        //        sagittis sapien vel imperdiet. Aliquam ac commodo diam, quis condimentum eros. Mauris ut
  //                        //        euismod lectus. Pellentesque ut consequat orci. Vestibulum et odio eros. Nullam vel justo vitae ante congue
  //                        //        posuere. Maecenas lectus lacus, iaculis vitae posuere et, semper et dui.
  //                        Future {}
  //                      case None =>
  //                        // Handle your errors
  //                        // Make some apologies
  //                        // Prepare for late night dev-ops
  //                        Future {}
  //                    }
  //                }
  //              case None =>
  //                // Handle your errors
  //                // Make some apologies
  //                // Prepare for late night dev-ops
  //                Future {}
  //            }
  //          case None => {
  //            // Handle your errors
  //            // Make some apologies
  //            // Prepare for late night dev-ops
  //            Future {
  //            }
  //          }
  //        }
  //
  //    val xxx = for {
  //      optOp1 <- dbOp1(1)
  //      x <- lift(optOp1, "Invalid option optOpt1")
  //      _ <- {
  //        //with A
  //        //      write some code which wash your windows
  //        //      tie your shoes with a
  //        //      Comb your hair
  //        Future {}
  //      }
  //      y <- dbOp2(x)
  //      z <- lift(y, "Invalid option y")
  //      // you see where the pattern....
  //      // Now we eliminate the netsting of case statements.
  //    } yield {}
  //
  //
  //    //this works, however no error handling if the option is None
  //    val yyyy = for {
  //      //      a <- OptionT(dbOp1(1))
  //      a <- OptionT(dbOp1(1))
  //      _ <- OptionT(dbOpNone(1))
  //      b <- {
  //        //with A
  //        //      write some code which wash your windows
  //        //      tie your shoes with a
  //        //      Comb your hair
  //        OptionT(dbOp2(a))
  //      }
  //    } yield {}
  //
  //    Await.result(yyyy.value, Duration.Inf)
  //
  //    //may be we lift the Option to another Future
  //  }
}
