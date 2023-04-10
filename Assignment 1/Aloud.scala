object Aloud extends App {
  def readAloud(xs: List[Int]): List[Int] = {
    def helper(oldList:List[Int],prev:Int,count:Int): List[Int] = oldList match {
      case Nil => count::prev::Nil
      case h::t => if (prev==h) helper(t,h,count+1)
                     else count::prev::helper(t,h,1)
    }
    if (xs.isEmpty) xs else helper(xs.tail,xs.head,1)
  }
//  println(readAloud(Nil))
//  println(readAloud(List(1,1,2)))
//  println(readAloud(List(-1,2,7)))
//  println(readAloud(List(3,3,8,-10,-10,-10)))
//  println(readAloud(List(3,3,1,1,3,1,1))) 2,3,2,3
}

/*
* but it is in reverse bc using tail recursive
    def helper(oldList:List[Int], prev: Int, count:Int, newList:List[Int]): List[Int] = oldList match {
      case Nil => prev::count::newList
      case h::t => if (prev==h) helper(t,h, count+1, newList)
      else helper(t,h,1,prev::count::newList)
    }
    def reverse(l: List[Int] , n: List[Int]): List[Int] = l match {
      case Nil => n
      case (h::t) => reverse(t,h::n)
    }
if (xs.isEmpty) xs else reverse(helper(xs.tail,xs.head,1,Nil),Nil)
* */
