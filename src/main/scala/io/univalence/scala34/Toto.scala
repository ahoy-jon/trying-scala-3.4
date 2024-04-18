package io.univalence.scala34

import language.experimental.captureChecking
import language.experimental.saferExceptions
import scala.util.Try
import java.io.FileOutputStream
import scala.annotation.capability

class DividedByZero extends Exception

object Toto:
  def multiplyBy(x:Int, y:Int):Int = x * y

  def divide(x: Int, y: Int): Int throws DividedByZero =
    if y != 0 then x / y else throw DividedByZero()

  def divideTwice(x:Int, y:Int): Int throws DividedByZero = 
    val first = divide(x,y)
    divide(first, y)

  def squareAndDivide(x:Int, y:Int): Int throws DividedByZero = 
    divide(multiplyBy(x,x), y)


  def divideThenSquare(x:Int, y:Int): Int throws DividedByZero = 
    val res = divide(x,y)
    multiplyBy(res, res)


  type WithArithmeticsException[T] = CanThrow[DividedByZero] ?=> T


  val divide2:WithArithmeticsException[Int => Int] = divideThenSquare(1, _)



  extension [A,E] (a: A throws E) 
    def toTry:Try[A] = Try {
      import unsafeExceptions.canThrowAny
      try a 
      catch 
        case e:E => throw e
    }




  
  //-------  en mode Scala 2 ------
  def oldDivide(x: Int, y: Int): Try[Int] = Try (x /y)

  def oldDivideTwice(x:Int, y:Int): Try[Int] = 
    val first = oldDivide(x,y)
    first.flatMap(res => oldDivide(res, y))

  def oldSquareAndDivide(x:Int, y:Int): Try[Int] = 
    oldDivide(multiplyBy(x,x), y)

  def oldDivideThenSuare(x:Int, y:Int):Try[Int] = 
    oldDivide(x,y).map(res => multiplyBy(res, res))


  def getRandom(using rand:RandomGen):Int = summon[RandomGen].getInt()

  def getRandomUntilN(n:Int)(using rand:RandomGen) = getRandom % n





@capability trait RandomGen {
  def getInt:() => Int
}

@capability trait Runtime

type Task[A] = Runtime ?=> () => A


object SomePrg:
  import Toto._

  def unsafeRun[A](ops: Task[A] throws Exception):A = {
    given runtime:Runtime = new Runtime{}
    try
      ops()
    catch 
      case e:Exception => ???

  }

  def realStuff(x: Int)(using Runtime): Unit throws DividedByZero = 
    val cal = divide(10, x)
    println(s"tchou tchou $cal")

  
  val task:Task[Unit] throws DividedByZero = () => realStuff(5)

  extension [A](t:Task[A])
    def repeatN(n:Int):Task[Seq[A]] = 
      () => (0 until n).map(_ => t())



@main def testRun():Unit = 
  import SomePrg._


  val divide_ : Int throws Exception =  Toto.divide(1, 0)
  //val res = divide_.toTry
  //println(res)

  def truc[A](ops:Task[A] throws Exception)(using Runtime):A throws Exception = {
    ???
  }

  val future = (truc {

    task()

  }).fork

  


  val prg: Task[Unit] throws Exception = () =>truc (() => {
    task()
    task
    val t3:Task[Unit] = () => task.repeatN(3)
    println("yo")
    t3()
    println("lo")
    t3()
  })