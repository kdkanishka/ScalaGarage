package com.pagero.codepatterns

import cats.data.{EitherT, OptionT}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import cats.implicits._
import io.github.hamsters.FutureOption
import io.github.hamsters.MonadTransformers._

import scala.util.Try

object EitherSol extends App {
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def dbOp1(input: Int): Future[Either[String, Int]] = {
    Future.successful(Right(input + 1))
  }

  def dbOp2(input: Int): Future[Either[String, Int]] = {
    Future.successful(Right(input * 10))
    Future.successful(Left("Unable to process db op2"))
  }

  def dbOp3(input: Int): Future[Either[String, Int]] = {
    Future.successful(Right(input * 5))
  }

  def dbOp11(inp: Int): Future[Option[String]] = {
    Future.successful(Some(s"Output1 $inp"))
    //    Future.successful(None)
  }

  compose()

  def compose(): Unit = {
    val composed = for {
      out1 <- EitherT(dbOp1(5))
      out2 <- EitherT(dbOp2(out1))
      out3 <- EitherT(dbOp3(out2))
    } yield out3

    val result = composed.value

    val output = Await.result(result, Duration.Inf)
    println(output)
  }

}
