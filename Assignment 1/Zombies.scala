object Zombies extends App {
  def countBad(hs: List[Int]): Int = {
    //count what's not in order given "check" as the input that we wanna check with
    def mergeCount(list:List[Int], check:Int ):Int = list match {
      case Nil => 0
      case h::Nil => if(h > check) 1 else 0
      case _ => mergeCount(list.slice(0,list.length/2),check) + mergeCount(list.slice(list.length/2,list.length),check)
    }
    //loop to check each element in hs list
    def loop(list:List[Int],len:Int):Int = len match{
      case 0 => 0
      case _ => mergeCount(list.tail,list.head) + loop(list.tail, len-1)
    }
    loop(hs,hs.length)
  }
//  println(countBad(List(35, 22, 10)) == 0)
//  println(countBad(List(3,1,4,2)) == 3)
//  println(countBad(List(5,4,11,7)) == 4)
//  println(countBad(List(1, 7, 22, 13, 25, 4, 10, 34, 16, 28, 19, 31)) == 49)
}
