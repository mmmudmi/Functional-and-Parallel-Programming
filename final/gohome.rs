use rayon::prelude::*;

#[allow(dead_code)]
pub fn to_board<'a>(st: &'a str) -> Vec<&'a [u8]> {
    st.lines()
      .map(|line| line.as_bytes())
      .collect()
}
/*
board =
[[120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120],
 [120, 32,  104, 120, 32,  32,  32,  120, 32,  109, 120],
 [120, 32,  32,  32,  32, 120,  32,  32,  32,  32,  120],
[120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120]]
*/
#[allow(dead_code)]
pub fn locate(board: &[&[u8]], target_ch: u8) -> Option<(usize, usize)> {
    let adjusted_board: Vec<(u8,usize,usize)> = (0..board.len()).into_par_iter()
        .flat_map(|row| {
            (0..board[row].len()).into_par_iter()
                .map(move |col| (board[row][col],row,col ))
                .collect::<Vec<(u8,usize,usize)>>()
        })
        .collect::<Vec<(u8,usize,usize)>>();
    //adjusted_board contains Vec<(ch, #row, #column)>
    adjusted_board.into_par_iter()
        .find_map_any(|(ch,row,col)| if ch == target_ch {Some((row,col))} else {None})
}

#[allow(dead_code)]
pub fn dist_home(board: &[&[u8]]) -> Option<usize> {
    use std::collections::HashSet;
    let home = locate(board, 'h' as u8)?;
    let me = locate(board, 'm' as u8)?;
    let mut seen: HashSet<(usize, usize)> = HashSet::new();
    let mut frontier: HashSet<(usize, usize)>  = [me].into_iter().collect();
    let mut distance = 0usize;

    fn nbrs(board: &[&[u8]], row: usize, col: usize) -> Vec<(usize,usize)> {
        //find boundary
        if board.is_empty() || board[0].is_empty() { return vec![];}
        let (down,up,left,right) = (row.clone()-1,row.clone()+1,col.clone()-1,col.clone()+1 );
        (down..=up).into_par_iter().flat_map(|r| {
                (left..=right).into_par_iter()
                    .flat_map(|c|
                        { if ((r == up && c == col.clone())
                            || (r == down && c == col.clone())
                            || (r == row.clone() && (c == right || c == left)))
                            && board[r][c] != 'x' as u8
                            { vec![(r,c)] } else { vec![] }
                        }
                    ).collect::<Vec<(usize,usize)>>()
        }).collect::<Vec<(usize,usize)>>()
    }

    while !frontier.is_empty() {
        let not_visited_nbrs: Vec<(usize, usize)> = frontier
            .par_iter()
            .flat_map(|&(current_row, current_col)| {
                let its_nbrs = nbrs(&board, current_row, current_col);
                its_nbrs.into_par_iter()
                    .filter(|node| !seen.contains(node))
                    .collect::<Vec<(usize,usize)>>()
            }).collect();
        seen.extend(&not_visited_nbrs);
        frontier.extend(&not_visited_nbrs);
        distance += 1;
        if frontier.contains(&home) { break;} //already get home yay!
    }
    match distance { 0 => None, _ => Some(distance),}
}

#[cfg(test)]
mod tests {
    use super::*;

    const SMALL_MAZE: &str = 
        "xxxxxxxxxxx\n\
         x hx   x mx\n\
         x    x    x\n\
         xxxxxxxxxxx";
    const SMALL_MAZE2: &str =
        "xxxxxxxxxxx\n\
         x  x   x  x\n\
         x         x\n\
         x         x\n\
         xm        x\n\
         xxxxxxxxxxx";
    
    #[test]
    fn test_locate_simple() {
        let board = to_board(SMALL_MAZE);
        assert_eq!(Some((1usize, 2usize)), locate(&board, 'h' as u8));
        assert_eq!(None, locate(&board, 'p' as u8));
    }
    
    #[test]
    fn test_dist_home_simple() {
        let board = to_board(SMALL_MAZE);
        assert_eq!(Some(11), dist_home(&board));
        let board1 = to_board(SMALL_MAZE2);
        assert_eq!(None, dist_home(&board1));
    }
}
