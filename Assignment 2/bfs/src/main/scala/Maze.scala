object Maze extends App{
  class Item(Row:Int,Col:Int,Data:String){
    val row:Int = Row
    val col:Int = Col
    val data: String = Data
    def isNearBy(that:Item):Boolean = {
      if(that.row==this.row+1 && that.col==this.col) true  //down
      else if(that.row==this.row-1 && that.col==this.col) true  //up
      else if(that.row==this.row && that.col==this.col-1) true  //left
      else if(that.row==this.row && that.col==this.col+1) true  //right
      else false
    }
    def direction(that:Item):String = {
      if(that.row==this.row+1 && that.col==this.col) "d"  //down
      else if(that.row==this.row-1 && that.col==this.col) "u"  //up
      else if(that.row==this.row && that.col==this.col-1) "l"  //left
      else if(that.row==this.row && that.col==this.col+1) "r"  //right
      else ""
    }
    def isSrc(): Boolean = data.equals("s")
    def isExit(): Boolean = data.equals("e")
    def isFree(): Boolean = data.equals(" ")
    def isNotFree(): Boolean = data.equals("x")
    override def toString: String = data//"Row" + row.toString + " Col" + col.toString + " " + data
  }
  def solveMaze(maze: Vector[String]): Option[String] = {
    //val items:List[Item] = addToList(maze,0) //List(Row2 Col1 s, Row2 Col2  , Row2 Col3  , Row2 Col4  , Row1 Col3  , Row1 Col4 e)
    val items:List[Item] = addToList(maze,0) .filterNot(x => x.isNotFree())
    val map:Map[Item,Set[Item]] = items.foldLeft(Map.empty[Item,Set[Item]]){ (remainMap,element) =>
        val frontiers:Set[Item] = items.foldLeft(Set.empty[Item])((remainSet,ele) => remainSet ++ items.filter(element.isNearBy))
        remainMap + (element -> frontiers)
    }
    //println(nbrs) //HashMap(b -> Set(d, a), e -> Set(s, d), d -> Set(e, c, b), a -> Set(c, b), s -> Set(e), c -> Set(d, a))
    val nbrs = (key: Item) => map.getOrElse(key, Nil).toSet
    val src:Option[Item] = if !items.exists(_.isSrc()) then None else Option(items.filter(_.isSrc()).head)
    val exit:Option[Item] = if !items.exists(_.isExit()) then None else Option(items.filter(_.isExit()).head)
    if(src.isEmpty||exit.isEmpty) return None
    val bfs:Map[Item,Item] = GraphBFS.bfs(nbrs,src.get)._1
    val found:Boolean = false
    val path:Option[List[Item]] = findPath(src.get,Option(exit.get),bfs,found,exit.get::Nil)
    if(path.isEmpty) return None
    //all [a,b,c,d,e]   this [a,b,c,d]    that [b,c,d,e]
    val pathString:String = pathToString(path.filterNot(x => x==exit).get,path.get.tail)
    //println(path)
    println(pathString.length)
    Option(pathString)
  }

  def pathToString(thisPath:List[Item],thatPath:List[Item]):String = {
    if(thisPath.isEmpty||thatPath.isEmpty) ""
    else thisPath.head.direction(thatPath.head) + pathToString(thisPath.tail, thatPath.tail)
  }

  def findPath(starter:Item,current:Option[Item],bfs:Map[Item,Item],found:Boolean,result:List[Item]):Option[List[Item]] = current match{
    case None => if(found) Option(result) else None //no next node
    case _ =>
      val next:Option[Item] = bfs.get(current.get)
      if (next.isEmpty) findPath(starter,None,bfs,found,Nil)
      else if(starter.equals(next.get)) findPath(starter,None,bfs,true,next.get::result)
      else findPath(starter,next,bfs,found,next.get::result)
  }

  def addToItem(row:String,rowCount:Int,colCount:Int):List[Item] = row match{
    case "" => Nil
    case _ => (new Item(rowCount,colCount,row.substring(0,1))) :: addToItem(row.tail,rowCount, colCount+1)
  }
  def addToList(rows: Vector[String],countRow:Int):List[Item] = rows.size match{
    case 0 => Nil
    case _ =>
      addToList(rows.tail,countRow+1):::addToItem(rows.head,countRow, 0)
  }

  // find


  val maze: Vector[String] = Vector(
    "xxxxxxxxxxxxxxxxxx",
    "x   x       x   ex",
    "x   x    x  x xxxx",
    "x        x  x    x",
    "xs  x    x       x",
    "xxxxxxxxxxxxxxxxxx")

  val bigMaze: Vector[String] = Vector(
    "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
    "x                                                                                                                                                                                                                                                       ex",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                  v                                      x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                           x                                                                                                                                                                            x",
    "x                                                                           x                                                                                                                                                                            x",
    "x                                                                           x                                                                                                                                                                            x",
    "x                                                                           x                                                                                                                                                                            x",
    "x                                                                           x                                                                                                                                                                            x",
    "x                                                                           x                                                                                                                                                                            x",
    "x                                                                           x                                                                                                                                                                            x",
    "x                                                                           x                                                                                                                                                                            x",
    "x                                                                           x                                                                                                                                                                            x",
    "x                                                                           x                                                                                                                                                                            x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                    x                                                                                                                                                                                                                   x",
    "x                                    x                                                                                                                                                                                                                   x",
    "x                                    x                                                                                                                                                                                                                   x",
    "x                                    x                                                                                                                                                                                                                   x",
    "x                                    x                                                                                                                                                                                                                   x",
    "x                                    x                                                                                                                                                                                                                   x",
    "x                                    x                                                                                                                                                                                                                   x",
    "x                                    x                                                                                                                                                                                                                   x",
    "x                                    x                                                                                                                                                                                                                   x",
    "x                                    x                                                                                                                                                                                                                   x",
    "x                                    x                                                                                                                                                                                                                   x",
    "x                                    x                                                                                                                                                                                                                   x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "x                                                                                                                                                                                                                                                        x",
    "xs                                                                                                                                                                                                                                                       x",
    "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
  )
  println(solveMaze(maze))
  println(Some("rrurrrrruurrrdddrruuurrr"))



  /*
  "xxxxxx",
  "xxx ex",
  "xs   x",
  "xxxxxx"
  *
  * */
}
