package scalaz

////
/**
 *
 */
////
trait ApplicativePlus[F[_]] extends Applicative[F] with PlusEmpty[F] { self =>
  ////

  /**The composition of ApplicativePlus `F` and `G`, `[x]F[G[x]]`, is a ApplicativePlus */
  def compose[G[_]](implicit G0: ApplicativePlus[G]): ApplicativePlus[({type λ[α] = F[G[α]]})#λ] = new CompositionApplicativePlus[F, G] {
    implicit def F = self

    implicit def G = G0
  }

  /**The product of ApplicativePlus `F` and `G`, `[x](F[x], G[x]])`, is a ApplicativePlus */
  def product[G[_]](implicit G0: ApplicativePlus[G]): ApplicativePlus[({type λ[α] = (F[α], G[α])})#λ] = new ProductApplicativePlus[F, G] {
    implicit def F = self

    implicit def G = G0
  }

  def some[A](a: F[A]): F[List[A]] = {
    lazy val y: Free.Trampoline[F[List[A]]] = z map (plus(_, point(Nil)))
    lazy val z: Free.Trampoline[F[List[A]]] = y map (apply(a, _)(_ :: _))
    z.run
  }

  def many[A](a: F[A]): F[List[A]] = {
    lazy val y: Free.Trampoline[F[List[A]]] = z map (plus(_, point(Nil)))
    lazy val z: Free.Trampoline[F[List[A]]] = y map (apply(a, _)(_ :: _))
    y.run
  }

  ////
  val applicativePlusSyntax = new scalaz.syntax.ApplicativePlusSyntax[F] { def F = ApplicativePlus.this }
}

object ApplicativePlus {
  @inline def apply[F[_]](implicit F: ApplicativePlus[F]): ApplicativePlus[F] = F

  ////

  ////
}

