package com.pagero.codepatterns

object Example2 extends App {

  def nested(inputSeq: Seq[Int],
             funcA: Option[(Int) => (String)],
             funcB: Option[(Seq[Int]) => (String)]
            ): Unit = {
    if (inputSeq.nonEmpty) {
      if (funcA.isDefined) {
        if (inputSeq.size == 1) {
          funcA.get(inputSeq.head)
        } else {
          throw new IllegalStateException("function A defined, bad input though")
        }
      } else if (funcB.isDefined) {
        funcB.get(inputSeq)
      } else {
        throw new IllegalStateException("function B is not defined")
      }
    } else {
      throw new IllegalStateException("empty collection")
    }
  }
}

object Example2Refactored extends App {

  val inputSeq = List(10, 20, 30)
//  val inputSeq = List()

  inputSeq match {
    case List(singleElem) => processSingleElem(singleElem, (inp) => inp.toString)
    case head :: tail => processCollection(inputSeq, (collec) => collec.size.toString)
  }

  def processSingleElem(input: Int, func: (Int) => (String)): Unit = {
    func(input)
  }

  def processCollection(inputSeq: Seq[Int], func: (Seq[Int]) => (String)): Unit = {
    func(inputSeq)
  }

}
