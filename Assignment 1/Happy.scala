object Happy extends App {
  def sumOfDigitsSquared(n: Int): Int = {
    def sumHelper(str:String,count:Int): Int = count match {
      case 0 => 0
      case _ => str.substring(0,1).toInt * str.substring(0,1).toInt + sumHelper(str.substring(1),count-1)
    }
    sumHelper(n.toString,n.toString.length)
  }

  def isHappy(n: Int): Boolean = n match{
    case 1 => true
    case 4 => false
    case _ => isHappy(sumOfDigitsSquared(n))
  }

  def kThHappy(k: Int): Int = {
    def kThHappyHelper(n: Int, ans: Int, track: Int): Int = n match {
      case 0 => ans
      case _ => if (isHappy(track)) kThHappyHelper(n-1,track,track+1)
                else kThHappyHelper(n, ans, track+1)
    }
    kThHappyHelper(k,1,1)
  }

  //println(sumOfDigitsSquared(199))
  //  println(isHappy(100))
  //  println(isHappy(111))
  //  println(isHappy(1234))
  //  println(isHappy(989))
  //  println(kThHappy(1))
  //  println(kThHappy(3))
  //  println(kThHappy(11))
  //  println(kThHappy(19))
 }
