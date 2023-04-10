use rayon::prelude::*;

#[allow(dead_code)]
pub fn longest_run(xs: &[i32]) -> Option<i32> {
    let max:usize = all_runs(xs).into_par_iter().map(|each| each.len())
        .reduce(|| 0, |left, right| if left<right {right} else { left });
    match max { 0 => None, _ => Some(max as i32),}
}

#[allow(dead_code)]
pub fn num_runs(xs: &[i32]) -> usize {
    if xs.is_empty() { return 0; }
    all_runs(xs).len()
}

pub fn all_runs(xs:&[i32]) -> Vec<Vec<i32>> {
    xs.into_par_iter()
        .map(|&each| vec![vec![each]])
        .reduce(||vec![vec![]], | left, right| {
            if left == vec![vec![]] && right != vec![vec![]] { return right.clone(); }
            else if right == vec![vec![]] && left != vec![vec![]] { return left.clone(); }
            let last_left: Vec<i32> = left[left.len()-1 as usize].clone();
            let first_right: Vec<i32> = right[0].clone();
            return if last_left[last_left.len() - 1] < first_right[0] {
                // [[1], [1,2,3]]  [[4,5], [3]] --> [[1],[1,2,3,4,5],[3]]
                let link: Vec<i32> = vec![last_left, first_right].into_par_iter().flatten().collect();
                let mut no_tail = left.clone();
                no_tail.pop();
                let mut no_head = right.clone();
                no_head.remove(0);
                vec![no_tail, vec!(link), no_head].into_par_iter().flatten().collect()
            } else {
                vec![left, right].into_par_iter().flatten().collect()
            }
        })
}
/*
[3, 1, 4, 5, 2, 4]
[3, 1, 4][ 5, 2, 4]
[3 ] [1, 4] [ 5 ] 2, 4]
[3] [1] [4] [5] [2] [4]
vec<vec[i32]>
[[3],[1]]  [[4,5]]  [[2,4]]
left: check in last vector (last index)
right: check in first vector (first index)
if left < right {merge the vector} else {push at the end}
[[3],[1,4,5]] [[2,4]]
[[3],[1,4,5], [2,4]]
*/
#[cfg(test)]
mod tests {
    use super::*;
    
    #[test]
    fn test_longest_run_simple() {
        assert_eq!(None, longest_run(&vec![]));
        assert_eq!(Some(1), longest_run(&[7]));
        assert_eq!(Some(2), longest_run(&[7, 8, 3]));
        assert_eq!(Some(1), longest_run(&[4, 3, 2, 1]));
        assert_eq!(Some(3), longest_run(&[7, 8, 3, 4, 5, 1]));
        assert_eq!(Some(3), longest_run(&[3, 1, 4, 5, 2, 4]));
        assert_eq!(Some(3), longest_run(&[7, 11, 11, 15, 19]));
    }

    #[test]
    fn test_num_runs_simple() {
        assert_eq!(0, num_runs(&vec![]));
        assert_eq!(1, num_runs(&[7]));
        assert_eq!(2, num_runs(&[7, 8, 3]));
        assert_eq!(3, num_runs(&[7, 8, 3, 4, 5, 1]));
        assert_eq!(3, num_runs(&[3, 1, 4, 5, 2, 4]));
        assert_eq!(2, num_runs(&[7, 11, 11, 15, 19]));
        assert_eq!(4, num_runs(&[4, 3, 2, 1]));
    }
}

