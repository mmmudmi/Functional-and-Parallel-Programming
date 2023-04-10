object Aggregate extends App {
  def myMin(p: Double, q: Double, r: Double): Double = {
    def myMinHelper(a: Double, b:Double, c:Double, min:Double, count:Double): Double = count match{
      case 0 => min
      case _ => if (a<=min) myMinHelper(b,c,a,a,count-1) else myMinHelper(b,c,a,min,count-1)
    }
    myMinHelper(p,q,r,p,3)
  }
  //println(myMin(5,2,3))
  def myMean(p: Double, q: Double, r: Double): Double = (p+q+r)/3
  //println(myMean(3,7,4))

  def myMed(p: Double, q: Double, r: Double): Double = {
    def max(a: Double, b:Double, c:Double, m:Double, count:Double): Double = count match{
      case 0 => m
      case _ => if (a>=m) max(b,c,a,a,count-1) else max(b,c,a,m,count-1)
    }
    val minimum: Double = myMin(p, q, r)
    val maximum: Double = max(p,q,r,p,3)
    def repeated(a:Double,b:Double, c:Double, count:Int, check:Double ,round:Double): Boolean = round match{
      case 0 => if(count>1)true else false
      case _ => if (a==check) repeated(b,c,a,count+1,check,round-1) else repeated(b,c,a,count,check,round-1)
    }
    val repeatedMin:Boolean = repeated(p,q,r,0,minimum,3)
    val repeatedMax:Boolean = repeated(p,q,r,0,maximum,3)
    def myMedHelper(a: Double, b:Double, c:Double, med:Double, count:Double): Double = count match {
      case 0 => med
      case _ => if(repeatedMin) myMedHelper(b,c,a,minimum,0)
                else if (repeatedMax) myMedHelper(b,c,a,maximum,0)
                else if (a!=minimum && a!=maximum) myMedHelper(b,c,a,a,0)
                else myMedHelper(b,c,a,med,count-1)
    }
    myMedHelper(p,q,r,p,3)
  }
//  println(myMed(1,1,1))
//  println(myMed(1,1,23))

}
