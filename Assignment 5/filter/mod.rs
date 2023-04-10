mod cwslice;
use rayon::iter::*;

#[allow(dead_code)]
pub fn par_filter<F>(xs: &[i32], p: F) -> Vec<i32>
    where F: Fn(i32) -> bool + Send + Sync
{
    let flags = xs.par_iter().map(|&ele| { p(ele) as i32}).collect::<Vec<i32>>();
    let plus_scan_output = plus_scan(&flags);
    let mut output_len =  plus_scan_output.1 as usize ;

    let mut output:Vec<i32> = Vec::with_capacity( output_len);
    unsafe { output.set_len(output_len); }
    let mut output_ptr = cwslice::UnsafeSlice::new(output.as_mut_slice());

    //loop
    plus_scan_output.0.par_iter().enumerate().into_par_iter().for_each(|(index,&ele)|{
        if flags[index] == 1 {
            unsafe { output_ptr.write(ele as usize,xs[index]); }
        }
    });
    output
}

pub fn is_even(n : i32) -> bool { n % 2 == 0 }

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


pub fn isOdd(input: i32) -> bool{
    if input%2 != 0{
        return true
    } else{
        return false
    }
}


#[cfg(test)]
mod tests {
    use rayon::iter::*;
    use crate::filter::{isOdd, par_filter, plus_scan};

    #[test]
    fn my_test() {
        assert_eq!(vec![1,3,5],par_filter(&[1,2,3,4,5], isOdd));
        println!("output: {:?}", par_filter(&vec![2, 5, 8, 6, 7, 4, 9, 1, 16], isOdd));
    }
}
