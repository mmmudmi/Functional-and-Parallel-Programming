//import Thesaurus.{bfs, current, found, result, starter}

import java.io.File
import scala.io.Source
object Thesaurus extends App {

  val defaultEncoding = "ISO8859-1"
// create map(string(parenr),setofstring(nbr))
  def load(filename: String) = {
  val file = Source.fromFile(filename)
  try{
    val f = file.getLines() //Iterator[String]
    f.next() //String //cut ISO8859-1 out first

    def setOfString(line: Iterator[String], countLines: Int): Set[String] = countLines match {
      case 0 => Set.empty
      case _ =>
        val currentLine: String = line.next()
      /*
        val oneTail :String = ")|swelling|puffiness|lump"                               val twoTail :String = ")|swelling|puffiness|lump"
        println((oneTail.tail.split('|').toSet)) => Set(, swelling, puffiness, lump)    println((twoTail.tail.tail.split('|').toSet)) => Set(swelling, puffiness, lump)
      */
        currentLine.substring(currentLine.lastIndexOf(")|")).tail.tail.split('|').toSet ++ setOfString(line, countLines - 1)
    }

    def loop(currentFile: Iterator[String], currentMap: Map[String, Set[String]]): Map[String, Set[String]] = currentFile match {
      case currentFile if currentFile.isEmpty => currentMap
      case _ =>
        val line = currentFile.next()
        val key: String = line.substring(0, line.lastIndexOf("|"))
        val countLines: Int = line.substring(line.lastIndexOf("|")).tail.toInt
        val updatedMap: Map[String, Set[String]] = Map(key -> setOfString(currentFile, countLines))
        loop(currentFile, currentMap ++ updatedMap)
    }

    loop(f, Map.empty)
    }
    finally{ file.close() }
    }
    //println(load("/Users/mudmi/Desktop/FunPar/src/main/scala/a2-starter/thesaurus_db.txt"))


    def linkage(thesaurusFile: String): String => String => Option[List[String]] = {
      val map = load(thesaurusFile)
      val firstWord = (wordA:String) => map.getOrElse(wordA,Nil).toSet
      val secondWord = (wordA:String) => (wordB:String) => //findPath(wordA, Option(wordB), GraphBFS.bfs(firstWord,starter)._1,false,wordB::Nil)
        val starter:String = wordA
        val finisher:String = wordB
        val current:Option[String] = Option(finisher)
        val nbrs = firstWord //(key: String) => map.getOrElse(key, Nil).toSet
        val bfs:Map[String,String] = GraphBFS.bfs(nbrs,starter)._1
        val found:Boolean = false
        val result:List[String] = finisher::Nil
        findPath(starter,current,bfs, found, result)
      secondWord
    }

//let's do find from the finisher node and do tail recursive as the reversing process
    def findPath(starter:String,current:Option[String],bfs:Map[String,String],found:Boolean,result:List[String]):Option[List[String]] = current match{
      case None => if(found) Option(result) else None //no next node
      case _ =>
        val next:Option[String] = bfs.get(current.get)
        if (next.isEmpty) findPath(starter,None,bfs,found,Nil)
        else if(starter.equals(next.get)) findPath(starter,None,bfs,true,next.get::result)
        else findPath(starter,next,bfs,found,next.get::result)
    }

//  val dataBase = load("/Users/mudmi/Desktop/FunPar/src/main/scala/a2-starter/thesaurus_db.txt")
//  val nbrs = (key: String) => dataBase.getOrElse(key, Nil).toSet
//  val starter:String = "clear"
//  val finisher:String = "vague"
//  val current:Option[String] = Option(finisher)
//  val bfs:Map[String,String] = GraphBFS.bfs(nbrs,starter)._1
//  val found:Boolean = false
//  val result:List[String] = finisher::Nil
//  val path = "/Users/mudmi/Desktop/FunPar/src/main/scala/a2-starter/thesaurus_db.txt"
//  //println(findPath(starter,current,bfs, found, result))
//  println(linkage(path)("clear")("vague"))

  //
//  println(GraphBFS.bfs(nbrs, "clear")._1) //._1 bc we dont want distance
//
//  println(GraphBFS.bfs(nbrs, "clear")._1.getOrElse("vague", Nil)) //faint
//
//  println(GraphBFS.bfs(nbrs, "clear")._1.getOrElse("faint", Nil)) //light
//
//  println(GraphBFS.bfs(nbrs, "clear")._1.getOrElse("light", Nil)) //done since output == "clear"
//

}
