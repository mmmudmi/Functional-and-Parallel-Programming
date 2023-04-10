object TurnIt extends App {
  def transpose(A: List[List[Int]]): List[List[Int]] = {
    /* get a new row from the given column
    *  [1,2,3]
    *  [4,5,6]  --->  wantedCol=2  --> [3,6]
    * */
    def getNewRow(old:List[List[Int]],oldRow:Int,wantedCol:Int,countCol:Int):List[Int] = oldRow match{
      case 0 => if(countCol==wantedCol) old.head.head::Nil
                else getNewRow(old.head.tail::old.tail,oldRow,wantedCol, countCol+1)
      case _ => if(countCol==wantedCol) old.head.head::getNewRow(old.tail,oldRow-1,wantedCol,0) //add and reset
                else getNewRow(old.head.tail::old.tail, oldRow, wantedCol, countCol+1) //move on
    }
    /* compliment "getNewRow" to the "fillAll" function
    *  loop for "newRow" times and add "fillAll" function each time with wantedCol=newRol or oldCol
    *  [1,2,3]                   [1,4]  1st oldCol
    *  [4,5,6]  -->  3 times --> [2,5]  2nd oldCol
    *                            [3,6]  3rd oldCol
    * */
    def fillAll(old:List[List[Int]],ans:List[List[Int]],oldRow:Int,newRow:Int):List[List[Int]] = newRow match{
      case 0 => getNewRow(old,oldRow,newRow,0)::ans
      case _ => fillAll(old, getNewRow(old,oldRow,newRow,0)::ans, oldRow, newRow-1)
    }
    fillAll(A,Nil,A.length-1,A.head.length-1)

  }
//  val a: List[Int] = List(1,2,3)
//  val b: List[Int] = List(4,5,6)
//  val c: List[Int] = List(7,8,9)
//  val d: List[List[Int]] = List(a,b,c)
//  println(transpose(d))
}
