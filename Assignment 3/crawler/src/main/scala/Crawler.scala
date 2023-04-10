import org.jsoup.*
import collection.JavaConverters.*
import java.net.URL
import scala.concurrent.Future
import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object Crawler {

  sealed case class WebStats(
    // the total number of (unique) files found
    numFiles: Int,
    // the total number of (unique) file extensions (.jpg is different from .jpeg)
    numExts: Int,
    // a map storing the total number of files for each extension.
    extCounts: Map[String, Int],
    // the total number of words in all html files combined, excluding
    // all html tags, attributes and html comments.
    totalWordCount: Long
  )

  //prep for WebStats. wait to get updated
  var extCounts: Map[String, Int] = Map[String, Int]()
  var totalWordCount:Long = 0
  var visited:Set[String] = Set()

  def nbrs(url: String): Future[Set[String]] =  {
    val promise = Promise[Set[String]]
    try{
      visited += url  //update url to the set of visited link
      //println(url)
      val doc = Jsoup.connect(url.toString).ignoreContentType(true).ignoreHttpErrors(true).get()

      val links = doc.select("a[href]")
      val absLinks = links.asScala.map(link => link.attr("abs:href")).toSet
      val media = doc.select("[src]")
      val absMedia = media.asScala.map(link => link.attr("abs:src")).toSet
      val imports = doc.select("link[href]")
      val absImports = imports.asScala.map(link => link.attr("abs:href")).toSet
      val scripts = doc.select("script[src]")
      val absScripts = scripts.asScala.map(link => link.attr("abs:src")).toSet
      val iframe = doc.select("iframe[src]")
      val absIframe = iframe.asScala.map(link => link.attr("abs:src")).toSet

      val unadjustedLinks:Set[String] = absLinks++absMedia++absImports++absScripts++absIframe //all the links
      var adjustedLinks:Set[String] = Set.empty //filter # \ / ? out of the link
        unadjustedLinks.foreach(link =>
        val check:String = link.split('/').takeRight(1).mkString
        if (!check.contains('.')) adjustedLinks
        else if( check.contains('#')) adjustedLinks += link.substring(0, link.lastIndexOf("#"))
        else if( check.contains('\\')) adjustedLinks += link.substring(0, link.lastIndexOf("\\"))
        else if( check.contains('?')) adjustedLinks += link.substring(0, link.lastIndexOf("?"))
        else if( check.last.equals('/')) adjustedLinks += (check+"index.html")
        else adjustedLinks += link
      )

      totalWordCount = totalWordCount + links.text().trim.split("\\s+").map(_.toLowerCase).toVector.count(_.nonEmpty)
                       + media.text().trim.split("\\s+").map(_.toLowerCase).toVector.count(_.nonEmpty)
                       + imports.text().trim.split("\\s+").map(_.toLowerCase).toVector.count(_.nonEmpty)
                       + scripts.text().trim.split("\\s+").map(_.toLowerCase).toVector.count(_.nonEmpty)
                       + iframe.text().trim.split("\\s+").map(_.toLowerCase).toVector.count(_.nonEmpty)

      promise.success(adjustedLinks)
    }catch  { case e => e }//nothing
    promise.future
  }

  //bfs function returns nothing but update each time it calls nbrs function
  def bfs(url: String): Unit = {
    var frontierLinks = Await.result(nbrs(url),Duration.Inf)
    while(frontierLinks.nonEmpty){
      val newFrontierLinks = expand(frontierLinks)
      frontierLinks = newFrontierLinks
    }

    def expand(frontierLinks: Set[String]): Set[String] = {
      frontierLinks.flatMap(link => {
        if(!visited.contains(link) && link.contains(URL(url).getHost)){
          Await.result(nbrs(link), Duration.Inf) // bc nbrs is Future --> return Set[URL]
        } else Set.empty
      })
    }
  }

  def crawlForStats(basePath: String):WebStats = {
    bfs(basePath)
    //check extensions from visited links
    visited.foreach( url =>
      val ext = url.substring(url.lastIndexOf("."))
      if (!url.equals(basePath)){
        if(!extCounts.contains(ext)) {extCounts += (ext,1)} else extCounts += (ext,extCounts(ext)+1)
      }
    )
    WebStats(visited.size,extCounts.size,extCounts,totalWordCount)
  }

  def main(args: Array[String]) = {
    val sampleBasePath = "https://cs.muic.mahidol.ac.th/courses/ooc/api/"
    println(crawlForStats(sampleBasePath))
  }
}

