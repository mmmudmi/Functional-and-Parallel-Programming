import java.util.Date

object DateUtil extends App {
  type Date = (Int, Int, Int)//(day, month, year)

  def isOlder(x: Date, y: Date): Boolean =
    (x._3<y._3) || //if yearX < yearY
      (x._3 == y._3 && x._2 < y._2) || //if yearX == yearY && monthX < monthY
      ((x._3 == y._3 && x._2 == y._2) && x._1<y._1) //if yearX == yearY && monthX == monthY && dayX < dayY

  def numberInMonth(xs: List[Date], month: Int): Int = xs.count((d,m,y) => m == month) //for x in xs count if x._2 == month

  def numberInMonths(xs: List[Date], months: List[Int]): Int = xs.count((d,m,y) => months.contains(m)) // xs.count(x => months.contains(x._2)

  def datesInMonth(xs: List[Date], month: Int): List[Date] = xs.filter(x=> x._2 == month) //add x to the list when x._2 == month

  def datesInMonths(xs: List[Date], months: List[Int]): List[Date] = xs.filter(x => months.contains(x._2)) //add to the list if months.contains(x._2)

  def dateToString(d: Date): String = {
    val months:List[String] = List("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    //count start at 1
    def helper(m:List[String],count:Int): String = count match{
      case d._2 => m.head+"-"+d._1+"-"+d._3
      case _ => helper(m.tail,count+1)
    }
    helper(months,1)
  }

  //if yr%4 == 0 then fab has 29 days
  def whatMonth(n: Int, yr: Int): Int = {
    def whatMonthHelper(m:List[Int], count:Int, days:Int): Int = days match{
      case days if days-m.head <0 => count
      case _ => whatMonthHelper(m.tail,count+1,days-m.head)
    }
    val m1: List[Int] = List(31,28,31,30,31,30,31,31,30,31,30,31)
    val m2: List[Int] = List(31,29,31,30,31,30,31,31,30,31,30,31)
    if ((yr%4==0 || yr%400 == 0)&& yr%100 !=0) whatMonthHelper(m2,1,n) else whatMonthHelper(m1,1,n)
  }

  def oldest(dates: List[Date]): Option[Date] = dates match{
    case Nil => None
    case h::Nil => Some(h)
    case h::t::Nil => if(isOlder(h,t)) Some(h) else Some(t)
    case _ =>
      val (l,r) = dates.splitAt(dates.length/2)
      if(isOlder(oldest(l).get,oldest(r).get)) oldest(l) else oldest(r)
  }

  def isReasonableDate(d: Date): Boolean = {
    val m1: List[Int] = List(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    val m2: List[Int] = List(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    def helper(m: Int, ms: List[Int]): Int = m match {
      case 1 => ms.head
      case _ => helper(m - 1, ms.tail)
    }
    if (d._2 > 0 && d._2 <= 12 && d._1 > 0 && d._3 > 0) {
      if ((d._3 % 4 == 0 || d._3 % 400 == 0) && d._3 % 100 != 0) d._1 <= helper(d._2, m2) else d._1 <= helper(d._2, m1) //check dates
    }
    else false
  }
//  println(isOlder((2,5,2001), (20,5,2002)))
//  println(numberInMonth(List((1,2,2011), (2,12,2011), (4,2,2011)),2))
//  println(numberInMonths(List((1,2,2011), (2,12,2011), (4,2,2011)),List(2,12)))
//  println(datesInMonth( List((1,2,2011), (2,12,2011), (4,2,2011), (2,3,2011), (10,2,2011)), 2))
//  println(datesInMonths( List((1,2,2011), (2,12,2011), (4,2,2011), (2,3,2011), (10,2,2011)), List(2,3)))
//  println(dateToString(12,5,2014))
//  println(whatMonth(61,2013))
//  println(oldest( List((1,4,2002), (5,4,2002), (12,4,2002)) ))
//  println(isReasonableDate( (29,2,2017) ))

//  val d1:Date = (1,2,2023)
//  val d2:Date = (2,2,2023)
//  val d3:Date = (2,2,2020)
//  val d4:Date = (1,6,1998)
//  val d5:Date = (6,1,1998)
//  println(isOlder(d1,d2))
//  println(isOlder(d1,d3))
//  println(isOlder(d5,d4))
//  println(isOlder(d5,d5))
//
//  val numberInMonth_tester:List[Date] = List((1,5,2022),(4,5,2009),(14,2,2005),(8,5,2001),(4,3,2004),(1,3,0),(32,2,-1))
//  val monthList:List[Int] = List(3,5)
//  val datesInMonth_tester:List[Date] = List((1,5,2022),(4,5,2009),(14,2,2005),(8,5,2001),(4,3,2004),(1,3,0),(32,5,-1))
//  println(numberInMonth(numberInMonth_tester,2))
//  println(numberInMonths(numberInMonth_tester,monthList))
//  println(datesInMonth(datesInMonth_tester,3))
//  println(datesInMonths(datesInMonth_tester,monthList))
//
//  println(dateToString(1,2,2000))
//  println(isOlder((21,5,2022), (20,5,2002)))
//  println(numberInMonth(List((1,2,2011), (2,12,2011), (4,2,2011)),2))
//  println(datesInMonth( List((1,2,2011), (2,12,2011), (4,2,2011), (2,3,2011), (10,2,2011)), 2))
//  println(dateToString(12,5,2014))
//  println(dateToString(12,5,0))
//  println(whatMonth(61,2013))
//  println(whatMonth(1,2013))
//  println(whatMonth(31,2013))
//  println(whatMonth(31,-1))
//  println(oldest(List((1,6,2011), (5,10,2015), (12,4,2002),(2,1,2000),(1,1,0))))
//  println(isReasonableDate( (29,2,1999) ))
//  println(isReasonableDate( (29,2,2000) ))
//  val d9:Date = (29,2,1999)
//  val d10:Date = (1,3,0)
//  println(isOlder(d9,d10))

}

