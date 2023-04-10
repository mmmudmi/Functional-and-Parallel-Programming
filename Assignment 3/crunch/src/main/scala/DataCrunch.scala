import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable
import scala.concurrent.duration.Duration


object DataCrunch {

  trait DataProvider {
    def get(onSuccess: Seq[String] => Unit,
            onFailure: () => Unit): Unit
  }


  object LoremIpsum extends DataProvider {
    override def get(onSuccess: Seq[String] => Unit,
            onFailure: () => Unit): Unit = {
      val lorem =
        """Lorem ipsum dolor sit amet, consectetur adipiscing elit.
        Cras nec sagittis justo. Nullam dignissim ultricies velit a tempus.
        Aenean pharetra semper elit eu luctus. Fusce maximus lacus eget magna
        ultricies, ac suscipit justo lobortis. Nullam pellentesque lectus
        at tellus gravida rhoncus. Nam augue tortor, rutrum et eleifend id,
        luctus ut turpis. Vivamus nec sodales augue.

        Suspendisse non erat diam. Mauris hendrerit neque at sem laoreet
          vehicula. Sed aliquam urna a efficitur tincidunt. In non purus
        tincidunt, molestie velit vulputate, mollis nisl. Pellentesque
        rhoncus molestie bibendum. Etiam sit amet felis a orci fermentum
        tempor. Duis ante lacus, luctus ac scelerisque eget, viverra ut felis."""
      onSuccess(lorem.split("\n"))
    }
  }

  object FailedSample extends DataProvider {
    override def get(onSuccess: Seq[String] => Unit,
                     onFailure: () => Unit): Unit = {
      onFailure()
    }
  }

  // This returns a DataProvider that feeds the "consumer" all the lines from a
  // file indicated by filename.
  def FileSource(filename: String): DataProvider = new DataProvider {
    override def get(onSuccess: Seq[String] => Unit, onFailure: () => Unit): Unit = {
      try {
        val lines = io.Source.fromFile(filename)
          .getLines
          .toVector
        onSuccess(lines)
      }
      catch {
        case _: Throwable => onFailure()
      }
    }
  }
  def funcOnFailure() = throw new Exception("failed")

  def dataProviderFuture(dp: DataProvider): Future[Seq[String]] = {
    //on success gives lines
    val p = Promise[Seq[String]]
    try {
      dp.get(EachLine => p.success(EachLine), funcOnFailure)
    } catch {
      case ex => p.failure(ex)
    }
    p.future

  }

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

  def highestFreq(linesFut: Future[Seq[String]]): Future[(String, Double)] = {
    val lines = linesFut.value.get.get
    val maps = lines.flatMap(wordsInLine)
    val mergedMap = maps.groupBy(map => map).map( each => (each._1._1,each._2.length))
    val numWords:Double = mergedMap.values.sum
    val mostRepeated: (String,Int) = mergedMap.maxBy(tuple => tuple._2)
    val frq:Double = mostRepeated._2.toDouble/numWords
    Future((mostRepeated._1,frq))
  }

  def main(args: Array[String]) = {
    // Example of how DataProvider is typically used. Comment out the block of code
    // below so it doesn't print some random garbage.
    def funcOnSuccess(lines: Seq[String]) = lines.foreach(println(_))
    def funcOnFailure() = println("Failed")
    val sampleProvider = LoremIpsum
    sampleProvider.get(funcOnSuccess, funcOnFailure)

    val test1 = dataProviderFuture(sampleProvider)
    val test2 = highestFreq(test1)
    Await.result(test2,Duration.Inf)
    println(test2)
  }

}
