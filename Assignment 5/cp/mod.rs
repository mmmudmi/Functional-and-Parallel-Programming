use rayon::iter::*;
use rayon::prelude::*;

#[allow(dead_code)]
pub fn par_closest_distance(points: &[(i32, i32)]) -> i64 {
    let mut sorted_vec = points.par_iter().map(|ele| *ele).collect::<Vec<(i32,i32)>>();
    sorted_vec.par_sort_unstable_by_key(|(x,y)| *x);
    (get_distance(&sorted_vec).1 as f64 ).sqrt() as i64
}
pub fn distance_calculation (a: (i32, i32), b: (i32, i32)) -> i64 {
    return match (a, b) {
        ((ax, ay), (bx, by)) => {
            ((ax - bx).pow(2)) * 2
        }
    } as i64
}
pub fn get_distance (points: &[(i32, i32)]) -> (Vec<(i32,i32)>, i64) {
    if points.len() < 4 {
        let mut minimum = std::i64::MAX;
        (0..points.len()).into_iter()
            .for_each(|i| {
                (i + 1..points.len()).into_iter()
                    .for_each(|j| {
                        let calculated = distance_calculation(points[i], points[j]);
                        minimum = if minimum < calculated {minimum} else {calculated};
                    })
            });
        let mut all_points = points.to_vec();
        all_points.sort_by_key(|ele| ele.1);
        return (all_points,minimum);
    } else {
        let (first_half,right_half) = points.split_at(points.len()/2);
        let ((mut LEFT,left), ( mut RIGHT, right)) = rayon::join(|| get_distance(first_half), || get_distance(right_half));
        let mut to_return = Vec::with_capacity(points.len());
        unsafe { to_return.set_len(points.len());}
        to_return = [LEFT,RIGHT].concat();
        let first_distance = if left<right { left } else {right};
        let filtered = to_return.par_iter()
            .filter(|(x, y)| ((points[points.len()/2].0 - x).abs() as i64) < first_distance)
            .map(|t| *t).collect::<Vec<(i32, i32)>>();
        let sec_distance = (0..filtered.len()).into_par_iter()
            .map( |index| {
                let range = if index + 7 > filtered.len() { filtered.len() } else { index + 7 };
                ((index + 1)..range).into_par_iter().map(|inner_index| distance_calculation (filtered[inner_index], filtered[index]))
                    .min().unwrap_or(i64::max_value())
            }).reduce(|| first_distance, |x, y| i64::min(x, y));

        (filtered, if first_distance<sec_distance {first_distance} else { sec_distance })
    }

}
