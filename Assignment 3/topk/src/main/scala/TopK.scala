import scala.concurrent.{Await, Promise}
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.io.Source
import java.io.File
import scala.concurrent.duration.*
import concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global

object TopK {

  def wordsInLine (line:String) : mutable.Map[String,Int] = {
    val splitToArray: Array[String] = line.split("[ ,!.]+").filterNot(_.isEmpty)
    val map = mutable.Map[String,Int]().empty.withDefaultValue(0)
    splitToArray.foreach(ele => map(ele.toLowerCase)+=1)
    map
  }

  def mergeMaps (map1:mutable.Map[String,Int],map2:mutable.Map[String,Int]) : mutable.Map[String,Int] = {
    val ans:mutable.Map[String,Int] = map1
    map2.map((k,v) =>
      if(ans.contains(k)) ans(k) += v
      else ans.addOne(k,v)
    )
    ans
  }

  def topKWords(k:Int)(fileSpec: String): Vector[(String, Int)] = {
    val file:File = new File(fileSpec)
    val allLines: Vector[String] = Source.fromFile(fileSpec, "ISO-8859-1").getLines().toVector //make it to vector of line [line,line,line, ...]
    //assign to Future
    val  assignToFuture = allLines.map( line => Future {
      wordsInLine(line)
    })
    val realAns = Future.sequence(assignToFuture)
    val allMaps = Await.result(realAns,Duration.Inf)
    val merged = allMaps.foldLeft(mutable.Map.empty [String,Int]){
      (acc,ele) => mergeMaps(acc,ele)
    }.toVector.sortBy((k,v) => v)
    val len = merged.length
    val (first,last) = merged.splitAt(len-k)
    last
  }

  def main(args: Array[String]) = {
    // println(topKWords(2)("/Users/mudmi/Desktop/untitled/test2.txt"))

  }
}


