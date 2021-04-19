package com.pagero.codepatterns

import cats.data.Validated.{Invalid, Valid, _}
import cats.data.ValidatedNec
import cats.implicits.{catsSyntaxTuple4Semigroupal, _}

object Example1MultiValidation extends App {

  type ValidationResult[A] = ValidatedNec[Error, A]

  case class Error(s: String)

  def validateOption[T](v: Option[T], err: String): ValidationResult[T] = {
    if (v.isDefined) v.get.validNec
    else Error(s"Invalid $err").invalidNec
  }

  nestedOptionsWithCats()

  def nestedOptionsWithCats() = {
    val valU = validateOption(getUser(None), "Error user")
    val valPass = validateOption(getUser(Some("11")), "Error Pass")
    val valSal = validateOption(getSalary(Some(11)), "Error Sal")
    val valAge = validateOption(getAge(None), "Error Age")

    (valU, valPass, valSal, valAge).mapN((i, j, k, l) => {}) match {
      case Valid(_) => println("All valid")
      case Invalid(s) => {
        s.iterator.foreach(println)
        println(s">>>Validation failure\n$s")
      }
    }
  }

  def getUser(user: Option[String]) = user

  def getSalary(sal: Option[Int]) = sal

  def getAge(age: Option[Int]) = age

}
