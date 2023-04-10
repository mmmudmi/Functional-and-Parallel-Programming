// use std::cmp;
// use std::cmp::*;
// use std::collections::HashSet;
//
// #[allow(dead_code)]
// pub fn solve(board: &str) -> Vec<String> {
//     turn_to_string(helper_solve(board))
// }
// pub fn turn_to_string(board: HashSet<Vec<Vec<u32>>>) -> Vec<String>{
//     let mut outer = Vec::new();
//     for set in board {
//         for row in set {
//             let mut inner = String::new();
//             for col in row {
//                 inner += &col.to_string();
//             }
//             outer.push(inner);
//         }
//     }
//     outer
// }
//
// pub fn helper_solve (board: &str) -> HashSet<Vec<Vec<u32>>> {
//     let mut to_return = HashSet::new();
//     let mut lines = vec![];
//     let mut current_board = board.clone();
//     while !current_board.is_empty() {
//         let (chunk, rest) = cur.split_at(cmp::min(9, current_board.len()));
//         lines.push(chunk);
//         current_board = rest;
//     }
//     to_return
// }
//
// fn not_found(mut grid: &Vec<Vec<u32>>, check: u32, row: usize, col: usize) -> bool {
//     let size = grid[0].len();
//     if grid[row].contains(&check) { return false; }
//     for j in 0..size {
//         if grid[j][col] == check {  return false; }
//     }
//     //not finished
//
//
// }
//
// fn solver<'a>(grid: &'a mut Vec<Vec<u32>>, mut row: usize, mut col: usize, mut result_set: &'a mut HashSet<Vec<Vec<u32>>>, ) -> &'a HashSet<Vec<Vec<u32>>> {
//     let size: usize = grid.len();
//     if  col == size.clone() - 1 && row == size - 1 {
//         if grid[row][col] > 0 {
//             result_set.insert(grid.clone());
//             return result_set;
//         } else {
//             (1..=9).into_iter().for_each(|check|{
//                 if not_found(grid, check, row.clone(), col.clone()) {
//                     grid[row.clone()][col.clone()] = num;
//                     result_set.insert(grid.to_owned());
//                     grid[row.clone()][col.clone()] = 0;
//                 }
//             });
//         }
//         return result_set;
//     }
//     //not done
// }



