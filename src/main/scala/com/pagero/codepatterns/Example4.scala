package com.pagero.codepatterns

import scala.util.{Failure, Success}

class Workbook(var sheet: Sheet)

class Sheet(var cell: Cell)

class Cell(var content: Content)

class Content(var data: Data)

class Data(var value: String)

class Document(var workbook: Workbook)

object Example4 extends App {
  val doc1 = new Document(new Workbook(new Sheet(new Cell(new Content(new Data("Hello"))))))
  val doc2 = new Document(new Workbook(new Sheet(null)))

  withIf(doc1)
  withIf(doc2)

  withPatternMatching(doc1)
  withPatternMatching(doc2)

  withComprehension(doc1)
  withComprehension(doc2)

//  val t1 = System.currentTimeMillis()
//  for(i <- 1 to 10000000){
//    withIf(doc1)
//    withIf(doc2)
//
//    withComprehension(doc1)
//    withComprehension(doc2)
//  }
//  val t2 = System.currentTimeMillis()
//  val dur = t2-t1
//  println(dur / 1000.0)


  //  withComprehensionAndErrorMsg(doc1)
  //  withComprehensionAndErrorMsg(doc2)

  def withIf(doc: Document): Unit = {
    if (doc.workbook != null) {
      if (doc.workbook.sheet != null) {
        if (doc.workbook.sheet.cell != null) {
          if (doc.workbook.sheet.cell.content != null) {
            if (doc.workbook.sheet.cell.content.data != null) {
              if (doc.workbook.sheet.cell.content.data.value != null) {
                //println(doc1.workbook.sheet.cell.content.data.value)
              } else {
//                println("value is null")
              }
            } else {
//              println("data is null")
            }
          } else {
//            println("content is null")
          }
        } else {
//          println("cell is null")
        }
      } else {
//        println("sheet is null")
      }
    } else {
//      println("workbook is null")
    }
  }

  def withComprehension(doc: Document): Unit = {

    val result = for {
      workbook <- Option(doc.workbook)
      sheet <- Option(workbook.sheet)
      cell <- Option(sheet.cell)
      content <- Option(cell.content)
      data <- Option(content.data)
      value <- Option(data.value)
    } yield value

    //println(result)
  }

  def withComprehensionAndErrorMsg(doc: Document): Unit = {
    val result = for {
      workbook <- Option(doc.workbook).asTry("workbook is null")
      sheet <- Option(workbook.sheet).asTry("sheet is null")
      cell <- Option(sheet.cell).asTry("cell is null")
      content <- Option(cell.content).asTry("content is null")
      data <- Option(content.data).asTry("data is null")
      value <- Option(data.value).asTry("value is null")
    } yield value

    //println(result)
  }

  implicit class OptUtils[T](a: Option[T]) {
    implicit def asTry[T](errMsg: String) = {
      a match {
        case Some(b) => Success(b)
        case _ => Failure(new Exception(errMsg))
      }
    }
  }

  def withPatternMatching(doc: Document): Unit = {
    Option(doc.workbook) match {
      case Some(workbook) =>
        Option(workbook.sheet) match {
          case Some(sheet) =>
            Option(sheet.cell) match {
              case Some(cell) =>
                Option(cell.content) match {
                  case Some(content) =>
                    Option(content.data) match {
                      case Some(data) =>
                        Option(data.value) match {
                          case Some(value) => println(value)
                          case _ => println("value is null")
                        }
                      case _ => println("data is null")
                    }
                  case _ => println("content is null")
                }
              case _ => println("cell is null")
            }
          case _ => println("sheet is null")
        }
      case _ => println("workbook is null")
    }
  }
}
