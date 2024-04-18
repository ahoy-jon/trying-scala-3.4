import io.univalence.scala34.Toto

import scala.unsafeExceptions.canThrowAny
import scala.annotation.capability
import language.experimental.captureChecking


@capability trait Run



@main def hello(): Unit =
    println("abc")
    import Toto.divide

    divide(1, divide(2, 2))
    println("oups")


def msg = "I was compiled by Scala 3. :)"




def race[T](t1: => T,t2: => T)(using run: Run): T = ???


def test = {

    given run:Run = ???

    val res:Int = race(1,2)

}