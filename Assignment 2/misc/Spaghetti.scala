object Spaghetti extends App {
  def spaghetti: Stream[String] = {
    def readAloud(str: String,prev:Char,count:Int): String = str match {
      case "" => count.toString.head+:prev+:""
      case _ =>
        if(prev.equals(str.head)) readAloud(str.tail,prev,count+1)
        else count.toString.head+:prev+:readAloud(str.tail,str.head,1)
    }
    lazy val result: Stream[String] = "1"#::result.map(x => readAloud(x.tail,x.head,1))
    result
//  def looper(str: String):Stream[String] = str #:: looper(readAloud(str.tail, str.head, 1))
//  looper("1") //start with "1"
   }
  //println(spaghetti.take(9).toList)

  def ham: Stream[String] = {
    def helper(n:Int):Stream[String] = n match {
      case 1 => "0"#::"1"#::Stream.empty
      case _ => helper(n-1).map(x => "0"+x)#:::helper(n-1).reverse.map(y => "1"+y)
    }
    lazy val result:Stream[String] = Stream.from(1).flatMap(x => helper(x))
    result
  }
//  println(ham.take(20).toList)
  
}

