object GraphBFS extends App {
  //return  Map[V, V] a map of a child -> its parent  and Map[V, Int]) a map of distance from src to V node
  def bfs[V](nbrs: V => Set[V], src: V) = {
    //nbrs is the func that give a set of input's nbrs; both child and parent

//frontier: nabors parent : (child->parent)
    def expand(frontier: Set[V], parent: Map[V, V]): (Set[V],Map[V, V]) = {
      def loop(newFrontier:Set[V], updatedParent:Map[V, V], waited:Set[V]):(Set[V],Map[V, V]) = waited.size match{
        case 0 => (newFrontier,updatedParent)
        case _ =>
          if !updatedParent.contains(waited.head) then loop(newFrontier,updatedParent, waited.tail)
          else
            val tempFrontier:Set[V] = nbrs(waited.head).filterNot(parent.contains) //set of new frontier
            //                                           append a map of (child -> parent) where all child/children have waited.head as a parent
            loop(newFrontier ++ tempFrontier,updatedParent ++ tempFrontier.foldLeft(Map.empty[V,V])((remain,V)=> remain + (V -> waited.head)), waited.tail)
      }
      loop(Set.empty,parent,frontier)
    }

    //while loop
    def iterate(frontier: Set[V], parent: Map[V, V], distance: Map[V, Int], d: Int): (Map[V, V], Map[V, Int]) =
      if frontier.isEmpty then (parent, distance)
      else {
        val (frontier_, parent_) = expand(frontier, parent)
//                             append a map of (child -> d) where all child/children have frontier as a parent and d is counted base on each level of tree
        val distance_ = distance ++ frontier.foldLeft(Map.empty[V,Int])((remain,V)=> remain + (V -> d))
        iterate(frontier_, parent_, distance_, d + 1)
      }
    iterate(Set(src), Map(src -> src), Map(), 0)
  }

  def nbrs(node: String): Set[String] = {
    if (node.equals("A")) Set("B","C")
    else if (node.equals("B"))  Set("A","D","E","F")
    else if (node.equals("C"))  Set("A","G")
    else if (node.equals("D"))  Set("B")
    else if (node.equals("E"))  Set("B")
    else if (node.equals("F"))  Set("B")
    else if (node.equals("G")) Set("C")
    else Set()
  }
  println(bfs(nbrs,"A"))
}
