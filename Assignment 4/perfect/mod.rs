use std::{cmp::Ordering, ops::Range};
//use std::slice::range;

#[allow(dead_code)]
#[derive(Debug, Eq, PartialEq)] // make this enum type support equality test (i.e., ==)
pub enum Classification {
    Perfect,
    Deficient,
    Excessive,
}

#[allow(dead_code)]
pub fn classify_perfect(n: u64) -> Classification {
    let sum: u64 = (1..n).fold(0, |acc,num| if n%num == 0 {acc+num} else {acc});
    // for num in 1..n {
    //     if n%num==0 { sum += num };
    // }
    match sum {
        num if num == n  => Classification::Perfect,
        num if num > n => Classification::Excessive,
        _ => Classification::Deficient,
    }
}

#[allow(dead_code)]
pub fn select_perfect(range: Range<u64>, kind: Classification) -> Vec<u64> {
    range.into_iter().filter(|each| classify_perfect(*each) == kind).collect()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn basic_classify() {
        use Classification::*;
        assert_eq!(classify_perfect(1), Deficient);
        assert_eq!(classify_perfect(6), Perfect);
        assert_eq!(classify_perfect(12), Excessive);
        assert_eq!(classify_perfect(28), Perfect);
        assert_eq!(classify_perfect(36), Excessive);
        assert_eq!(classify_perfect(102), Excessive);
    }

    #[test]
    fn basic_select() {
        use Classification::*;
        assert_eq!(select_perfect(1..10_000, Perfect), vec![6, 28, 496, 8128]);
        assert_eq!(
            select_perfect(1..50, Excessive),
            vec![12, 18, 20, 24, 30, 36, 40, 42, 48]
        );
        assert_eq!(
            select_perfect(1..11, Deficient),
            vec![1, 2, 3, 4, 5, 7, 8, 9, 10]
        );

        assert_eq!(
            select_perfect(1..100, Excessive),
            vec![12, 18, 20, 24, 30, 36, 40, 42, 48, 54, 56, 60, 66, 70, 72, 78, 80, 84, 88, 90, 96]
        );
        assert_eq!(
            select_perfect(1..20, Deficient),
            vec![1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 19]
        );
    }
}
