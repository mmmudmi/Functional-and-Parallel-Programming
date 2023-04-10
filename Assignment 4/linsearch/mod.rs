//use std::sync::TryLockError::Poisoned;
use rayon::iter::*;

#[allow(dead_code)]
pub fn par_lin_search<T: Eq + Sync>(xs: &[T], k: &T) -> Option<usize> {
    if xs.is_empty() {return None;}  //O(1)
    let getFirst: Option<(usize,&T)> = Some((0,xs.get(0).unwrap()));  //O(1)
    let ans:Option<(usize,&T)> = xs.par_iter().enumerate().map(|tup| Some(tup)).reduce(|| getFirst, |a,b| find_K( a, b, &k));
    if ans.is_none() {None} else { Some(ans.unwrap().0) } //O(1)
}
// reduce helper
fn find_K<T: Eq + Sync > (a:  Option<(usize, T)>, b: Option<(usize,T)>, k: &T) -> Option<(usize,T)> { //find_K takes O(1)
    match (a,b) {

        (Some(a),Some(b)) =>
            if a.1 == *k { Some(a) }       //O(1)
            else if b.1 == *k { Some(b) }  //O(1)
            else { None },                 //O(1)
        (Some(a),None) =>
            if a.1 == *k { Some(a) }       //O(1)
            else { None },                 //O(1)
        (None,Some(b)) =>
            if b.1 == *k { Some(b) }      //O(1)
            else { None },                //O(1)
        _ => None,                        //O(1)
    }
}


/*
! Note: "find_K" function takes only O(1)

   Analyzing the work and span:

      w(n) = O(1) + 2w(n/2)  --> in .reduce() we divide n into half w(n/2) and put both into recursive function calls 2w(n/2)
           = O(n)
      s(n) = O(1) + s(n/2) --> since .reduce() will be done in parallel, the longest time will be max( s(n/2) , s(n/2) ) which is s(n/2)
           = O(log n)

      p(n) = w/s = O(n/log n) --> this is the amount of processor is in used

*/

#[cfg(test)]
mod tests {
    use crate::linsearch::par_lin_search;

    #[test]
    fn basic_lin_search() {
        let xs = vec![3, 1, 4, 2, 7, 3, 1, 9];
        assert_eq!(par_lin_search(&xs, &3), Some(0));
        assert_eq!(par_lin_search(&xs, &5), None);
        assert_eq!(par_lin_search(&xs, &1), Some(1));
        assert_eq!(par_lin_search(&xs, &2), Some(3));

        let ys = vec![2];
        assert_eq!(par_lin_search(&ys, &1), None);
        assert_eq!(par_lin_search(&ys, &2), Some(0));

        let zs = vec![];
        assert_eq!(par_lin_search(&zs, &0), None);

    }
}
