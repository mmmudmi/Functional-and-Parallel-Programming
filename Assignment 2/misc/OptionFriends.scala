object OptionFriends extends App {
  def lookup(xs: List[(String, String)], key: String): Option[String] = {
    val ans: List[(String, String)] = xs.filter((k,v) => k.equals(key))
    if(ans.isEmpty)None else Some(ans.head._2)
  }

//  val s: List[(String, String)] = List(("a", "xy"), ("c", "pq"), ("r", "je"))
//  println(lookup(s,"a"))

  def resolve(userIdFromLoginName: String => Option[String],
              majorFromUserId: String => Option[String],
              divisionFromMajor: String => Option[String],
              averageScoreFromDivision: String => Option[Double],
              loginName: String): Double = {
    userIdFromLoginName(loginName).flatMap(majorFromUserId).flatMap(divisionFromMajor).flatMap(averageScoreFromDivision).getOrElse(0.0)
  //   (((apply loginName in userIdFromLoginName) apply in majorFromUserId) apply in divisionFromMajor) apply in averageScoreFromDivision
  // if None returns 0.0
  }
}
