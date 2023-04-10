use rayon::prelude::*;


#[allow(dead_code)]
pub fn puff(one_d: &[i32], n: usize) -> Vec<Vec<i32>> {
    if one_d.len() == 0 {return vec![] ;}
    if one_d.len() <= n {return vec![one_d.to_vec()];}
    else {
        one_d.par_chunks(n)
            .into_par_iter()
            .map(|chunk| chunk.to_vec())
            .collect()
    }
}

#[allow(dead_code)]
pub fn reflect(one_d: &[i32], n: usize) -> Vec<i32> {
    if one_d.len() <= n { return one_d.to_vec(); }
    let mut to_return = one_d.to_vec();
    to_return.par_iter_mut().enumerate()
        .for_each( |(index,ele)|{
            let (row,col) = index_to_row_col(index,n);
            let trans_target = row_col_to_index(col, row, n);
            *ele = one_d[trans_target] as i32;
        });
    to_return
}
//helper functions
pub fn index_to_row_col (index: usize, n:usize) -> (usize,usize) { (index/n,index%n) }
pub fn row_col_to_index (row: usize, col: usize, n:usize) -> usize { (row*n)+col }
/*
        n = 4

        vec![0, 1, 2, 3], -> (0..4) -> n*row..(n*row)+n 0
        vec![4, 5, 6, 7], -> (4..8)  1
        vec![8, 9, 10, 11],  -> (8..12)  2
        vec![12, 13, 14, 15], -> (12..15)   3

*/

#[cfg(test)]
mod tests {
    use super::*;
    
    #[test]
    fn test_puff_simple() {
        assert_eq!(vec![] as Vec<Vec<i32>>, puff(&[], 0));
        assert_eq!(vec![vec![9]], puff(&[9], 1));
        assert_eq!(vec![
            vec![1, 3, 2, 5],
            vec![4, 1, 7, 9],
            vec![8, 6, 4, 3],
            vec![7, 9, 2, 0],
        ], puff(&[1, 3, 2, 5, 4, 1, 7, 9, 8, 6, 4, 3, 7, 9, 2, 0], 4));
    }

    #[test]
    fn test_reflect_simple() {
        assert_eq!(vec![] as Vec<i32>, reflect(&[], 0));
        assert_eq!(vec![9], reflect(&[9], 1));
        assert_eq!(vec![3, 1, 2, 7], reflect(&[3, 2, 1, 7], 2));
        assert_eq!(vec![1, 4, 8, 7, 3, 1, 6, 9, 2, 7, 4, 2, 5, 9, 3, 0],
            reflect(&[1, 3, 2, 5, 4, 1, 7, 9, 8, 6, 4, 3, 7, 9, 2, 0], 4));
        assert_eq!(vec![1, 3, 2, 5,
                        4, 1, 7, 9,
                        8, 6, 4, 3,
                        7, 9, 2, 0],
            reflect(&[1, 4, 8, 7,
                            3, 1, 6, 9,
                            2, 7, 4, 2,
                            5, 9, 3, 0], 4));
    }
}
