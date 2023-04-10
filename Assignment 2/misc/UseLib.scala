object UseLib extends App {
  def onlyBeginsWithLower(xs: Vector[String]): Vector[String] = xs.filter(x => x.charAt(0).isLower && !x.charAt(1).isLower)

  def longestString(xs: Vector[String]): Option[String] = if (xs.isEmpty) None else Some(xs.maxBy(_.length))

  def longestLowercase(xs: Vector[String]): Option[String] = if (onlyBeginsWithLower(xs).isEmpty) None else Some(onlyBeginsWithLower(xs).maxBy(_.length))

//  val t : Vector[String] = Vector("hhEllo","hHE","jIewojdf","di")
//  println(longestLowercase(t))
}
