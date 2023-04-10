object AllPerm extends App {
  def allPerm(n: Int): List[List[Int]] = {
    //  insertIn(index=1  , item=4,   count=0  ,List(1,2,3) ,Nil ) --> [1,4,2,3]
    def insertIn(index:Int, item:Int, count:Int, l:List[Int], hold:List[Int]): List[Int] = {
      if (count==index) hold:::item::l else insertIn(index, item, count+1, l.tail, l.head::Nil:::hold)
    }
    //  count= item = 3, l = (1, 2)  --> (3, 1, 2), (1, 3, 2), (1, 2, 3)
    def innerAdd(count:Int,item:Int, l:List[Int], ans:List[List[Int]]) : List[List[Int]] = count match{
      case 0 => ans
      case _ => insertIn(count-1,item,0,l,Nil)::innerAdd(count-1,item,l,ans)
    }
    //  ref= [(1,2),(2,1)] size= 2, add=3 --> [(3,1,2),(1,3,2),(1,2,3),(3,2,1),(2,3,1)(2,1,3)]
    def outerAdd(ref:List[List[Int]],size:Int,add:Int): List[List[Int]] = size match{
      case 0 => Nil
      case _ => innerAdd(add,add,ref.head,Nil):::outerAdd(ref.tail,size-1,add)
    }

    def addAllUp(nth: Int): List[List[Int]] = nth match{
      case 1 => List(List(1))
      case _ => val prev: List[List[Int]] = addAllUp(nth-1) //[(1,2),(2,1)] len = 2
                outerAdd(prev,prev.length,nth)
    }
    addAllUp(n)
  }
  println(allPerm(3))
}
