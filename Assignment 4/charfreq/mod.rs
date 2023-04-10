use chashmap::CHashMap;
use rayon::iter::{IntoParallelRefIterator, ParallelIterator};
use std::collections::HashMap;


//"Hello, world!".as_bytes() gives a byte slice [72, 101, 108, 108, 111, 44, 32, 119, 111, 114, 108, 100, 33]
pub fn par_char_freq(chars: &[u8]) -> HashMap<u8, u32> {
    let mut store:CHashMap<u8, u32> = CHashMap::new();
    chars.par_iter().for_each(|ch| store.upsert(*ch, || 1, |v| *v += 1) );

    let toHashMap = store.into_iter().collect();
    toHashMap
}

#[cfg(test)]
mod tests {
    use super::{par_char_freq};
    use std::collections::HashMap;

    #[test]
    fn basic_tests() {
        let map1:HashMap<u8, u32> = [(98,1), (110,2), (97,3)].iter().cloned().collect();
        assert_eq!(map1,par_char_freq("banana".as_bytes()));
        let map2:HashMap<u8, u32> = [(115,4), (109,1), (105,4), (112,2)].iter().cloned().collect();
        assert_eq!(map2,par_char_freq("mississippi".as_bytes()));
    }
}
