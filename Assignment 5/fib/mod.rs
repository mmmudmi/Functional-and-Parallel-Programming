use rayon::iter::*;
use num_bigint::{BigUint, ToBigUint};

//"par_fib_seq" uses "scan" function which contains the following
// W = W[n/2]+O(n) = O(n)
// S = S[n/2] + O(logn) = O(log2n)


#[allow(dead_code)]
pub fn par_fib_seq(n: u32) -> Vec<num_bigint::BigUint> {
    return match n {
        0 => Vec::new(),
        1 => vec![1.to_biguint().unwrap()],
        _ => {
            let mut temp = vec![(1,1,1,0); (n - 1) as usize];
            let (mut matrix, addition) = scan(&temp);
            matrix.push((1,1,1,0)); //last in matrix
            let mut to_return:Vec<num_bigint::BigUint> = matrix.into_par_iter()
                .map(|each| each.0.to_biguint().unwrap())
                .collect::<Vec<num_bigint::BigUint>>();
            to_return.sort(); // make it in order
            to_return
        },
    }
}

pub fn multiply_helper(first:(i32,i32,i32,i32), second: (i32, i32, i32, i32)) -> (i32, i32, i32, i32){ //O(1)
    /*
    [a b] * [e f]  = [(a*e)+(b*g)  (a*f)+(b*h)]
    [c d]   [g h]    [(c*e)+(h*g)  (c*f)+(d*h)]
    */
    return match (first, second) {
        ((a, b, c, d), (e, f, g, h)) => {
            let t_left = (a.clone() * e.clone()) + (b.clone() * g.clone());
            let t_right = (a.clone() * f.clone()) + (b.clone() * h.clone());
            let l_left = (c.clone() * e.clone()) + (h.clone() * g.clone());
            let l_right = (c.clone() * f.clone()) + (d.clone() * h.clone());
            (t_left, t_right, l_left, l_right)
        }
    }
}

//from lecture
//W = W[n/2]+O(n) = O(n)
//S = S[n/2] + O(logn) = O(log2n)
pub fn scan(xs:&Vec<(i32,i32,i32,i32)>) -> (Vec<(i32,i32,i32,i32)>,(i32,i32,i32,i32)) {
    if xs.is_empty() {return (vec![],(1,1,1,0));}  // O(1)
    let half = xs.len()/2;  // O(1)
    let contracted = &(0..half).into_par_iter()
        .map(|i| multiply_helper(xs[2*i],xs[2*i+1] )) // O(1)
        .collect::<Vec<(i32,i32,i32,i32)>>();
    let (c_prefix, mut c_sum) = scan(contracted); // w(n/2) + O(1) =O(logn)

    let mut pfs: Vec<(i32,i32,i32,i32)> = (0..half).into_par_iter()
        .flat_map(|i| vec![c_prefix[i], multiply_helper(c_prefix[i], xs[2*i])]).collect(); // O(n)

    if xs.len()%2 == 1 {
        pfs.push(c_sum);  // O(1)
        c_sum = multiply_helper(c_sum.clone(), xs[xs.len()-1]); // O(1)
    } //last one only /odd
    (pfs,c_sum)
}


/*
//from lecture
pub fn plus_scan(xs:&[i32]) -> (Vec<i32>,i32) {
    use rayon::iter::*;
    if xs.is_empty() {return (vec![],0);}
    let half = xs.len()/2;
    let contracted = &(0..half).into_par_iter()
        .map(|i| xs[2*i] + xs[2*i+1])
        .collect::<Vec<i32>>();
    let (c_prefix, mut c_sum) = plus_scan(contracted);
    let mut pfs: Vec<i32> = (0..half).into_par_iter()
        .flat_map(|i| vec![c_prefix[i],c_prefix[i]+xs[2*i]])
        .collect();
    if xs.len()%2==1 {
        pfs.push(c_sum);
        c_sum += xs[xs.len()-1];
    } //last one only /odd
    (pfs,c_sum)
}

*/