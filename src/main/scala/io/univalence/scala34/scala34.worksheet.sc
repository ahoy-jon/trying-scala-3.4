trait Ref[A] {
  def get: A
  def set(a: A): Unit
  def update(f: A => A): Unit
}

type RefOperation[A] = Ref[Int] ?=> A

def increment: RefOperation[Unit] = state ?=> state.update(_ + 1)
// Increment the state

def get: RefOperation[Int] = state ?=> state.get

// Implémentation concrète de Ref[A] pour A étant Int
class IntRef(var value: Int) extends Ref[Int] {
  def get: Int = value
  def set(a: Int): Unit = value = a
  def update(f: Int => Int): Unit = value = f(value)
}

def runOps[A](init: Int)(ops: RefOperation[A]): A =
  given r: Ref[Int] = new IntRef(init) // Utilisation de IntRef avec la valeur initiale
  ops(using r)

// Example usage
val finalState = runOps(0) {
  increment
  increment
  get
}
// finalState devrait être 2

def purify[A](ops:RefOperation[A]): Int => (Int, A) = i => runOps(i){
  val a = ops
  (get, a)
}



purify(increment)(2)