package com.pagero.codepatterns

import cats.data.ValidatedNec
import cats.data.Validated.{Invalid, Valid, _}
import cats.data.ValidatedNec
import cats.implicits.{catsSyntaxTuple4Semigroupal, _}

import scala.util.{Failure, Success, Try}

case class Request(name: Option[String], age: Option[Int])

case class Metadata(name: String, age: Int)

object Example1 extends App {
  //  val request = Request(Some("Kanishka"), None)
  val request = Request(None, None)

  //  val res = requestToMetadataVerbose(request)
    val res = requestToMetadataSol1(request)
  //    val res = requestToMetadataSol2(request)
  //  val res = requestToMetadataSol3(request)
//  val res = requestToMetadataSolValidated(request)
  println(res)

  def requestToMetadataSol1(req: Request): Try[Metadata] = {
    for {
      name <- req.name.asTry[String]("Name required")
      age <- req.age.asTry[Int]("Age required")
    } yield Metadata(name, age)
  }

  implicit class OptUtils[T](maybeT: Option[T]) {
    implicit def asTry[T](errMsg: String) = {
      maybeT match {
        case Some(valT) => Success(valT)
        case _ => Failure(new Exception(errMsg))
      }
    }
  }

  def requestToMetadataSol2(req: Request): Try[Metadata] = {
    for {
      name <- req.name.map(Success(_)).getOrElse(Failure(new Exception("Name required")))
      age <- req.age.map(Success(_)).getOrElse(Failure(new Exception("Age required")))
    } yield Metadata(name, age)
  }

  //  def requestToMetadataSol3(req: Request): Try[Metadata] = {
  //    val output = req match {
  //      case Request(Some(name), Some(age)) => Success(Metadata(name, age))
  //      case _ => errorHandler(req)
  //    }
  //
  //    output
  //  }

  //  def errorHandler(request: Request): Try[Metadata] = {
  //    for {
  //      name <- request.name.asTry[String]("Name required")
  //      age <- request.age.asTry[Int]("Age required")
  //    } yield Metadata(name, age)
  //  }

  def requestToMetadataVerbose(req: Request): Try[Metadata] = {
    val nameOpt = req.name
    val ageOpt = req.age

    val nameTry = nameOpt.fold[Try[String]](Failure(new Exception("Name required")))(Success(_))
    val ageTry = ageOpt.fold[Try[Int]](Failure(new Exception("Age required")))(Success(_))

    for {
      name <- nameTry
      age <- ageTry
    } yield Metadata(name, age)
  }

  type ValidationResult[A] = ValidatedNec[Error, A]

  case class Error(s: String)

  def validateOption[T](v: Option[T], err: String): ValidationResult[T] = {
    if (v.isDefined) v.get.validNec
    else Error(err).invalidNec
  }

  def requestToMetadataSolValidated(req: Request): Try[Metadata] = {
    val nameVal = validateOption(req.name, "Name required")
    val ageVal = validateOption(req.age, "Age required")

    (nameVal, ageVal).mapN((name, age) => Metadata(name, age)) match {
      case Valid(metadata) => Success(metadata)
      case Invalid(s) => {
        val errorText = new StringBuilder()
        s.iterator.foreach(errorMsg => errorText ++= s"${errorMsg.s},")
        Failure(new Exception(errorText.toString()))
      }
    }
  }

}

