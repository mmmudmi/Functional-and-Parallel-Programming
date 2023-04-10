use std::collections::HashMap;
use std::ffi::c_int;
use std::vec;
use rayon::iter::*;
use rayon::prelude::*;

#[allow(dead_code)]
pub fn par_encode_base64(bytes: &[u8]) -> String {
    //vector of index matches its character Base64
    let Base64_list:Vec<char> = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".chars().collect::<Vec<char>>();

    //turn bytes to long string of binary numbers "11010010101010101001001111000101010010001...."
    let mut unadjusted_bi: String = bytes.par_iter().fold( String::new, |acc: String, ele: &u8| acc + &(to_binary(*ele)) ).collect();
    let mut padding: String = "".to_string();

    //check it unadjusted_bi can devide by 6 perfectly or not if not add '0' to make up the missing space of 6 at the end making them all even when being sliced
    let check_if_miss = unadjusted_bi.len() % 6;
    if check_if_miss != 0 {
        unadjusted_bi += &("0".repeat(6-check_if_miss));
            //padding "=" 00 -> once padding  0000 -> twice padding
        padding = "=".repeat((6-check_if_miss)/2);
    }

    //chucks unadjusted_bi into group of string of 6
    let mut adjusted_bi:Vec<String> = Vec::new();
    let binding: Vec<char> = unadjusted_bi.chars().collect::<Vec<char>>(); //turn String into vec<char> -> ['1','1','0','1','0',...]

   // let b:Vec<String> = binding.clone().chunks(6).fold(Vec::new(), |mut acc,ele| {  acc.push(ele.iter().collect::<String>()); acc });
    binding.chunks(6).into_iter().for_each(|chunk| adjusted_bi.push(chunk.par_iter().collect())); // ["011010", "000110", "010101"...]

    //match each of binary in adjusted_bi to char and append to to_return string
    let mut to_return:String = adjusted_bi.into_par_iter().fold(String::new,|mut acc,ele| {
        acc.push(*Base64_list.get( bi_to_index(ele.to_string()) ).unwrap());
        acc
    }).collect();

    to_return + &padding
}

//turn byte to binary
    //O(1)
fn to_binary (byte: u8) -> String { format!("{:08b}", byte ) }
    //O()
fn bi_to_index (binary: String) -> usize {
    let size: usize = binary.len()-1;  //O(1)
    binary.chars().enumerate().map(|(nth,b)| (nth,b,0)).reduce(|acc:(usize,char,i32),ele:(usize,char,i32)| add_helper(acc,ele,size)).unwrap().2 as usize
}
    //O(1)
fn add_helper(a:(usize,char,i32) , b:(usize,char,i32), l:usize) -> (usize,char,i32) {
    let mut count:i32 = 0;
    if a.1 == '1' { count+= 2_i32.pow((l-a.0) as u32); }
    if b.1 == '1' { count+= 2_i32.pow((l-b.0) as u32); }
    (0,'0',a.2+b.2+count)
}

fn cut_end(s : String) -> String {
    let mut string:String = s.clone();
    let mut count: usize = s.len();
    if s.len()>4 { count = 4;}
    while count!=0  {
        count -= 1;
        if string.chars().last().unwrap().eq(&'=') { string.pop();}
        else { count = 0;};
    }
    string
}

#[allow(dead_code)]
pub fn par_decode_base64(code: &str) -> Option<Vec<u8>> {
    //vector of index matches its character Base64
    let Base64_list:Vec<char> = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".chars().collect::<Vec<char>>();

    //let removed_padd  ing: String = code.chars().filter(|each| *each != '=').collect::<String>();
    let removed_padding: String = cut_end(code.to_string());
    let valid_check: Vec<char> = removed_padding.par_chars().filter(|each| !Base64_list.contains(each)).collect();

    if removed_padding.is_empty() || !valid_check.is_empty() || code.len()-removed_padding.len()>4 { return None; }
    let count_added_zero: usize = (code.len() - removed_padding.len()) * 2;
    let matching_with_index: Vec<(char, usize)> = Base64_list.into_par_iter().enumerate().map(|(n, c)| (c, n)).collect::<Vec<(char, usize)>>(); // Vec[('A',0),('U',20) ....]
    let Base64_get_index: HashMap<char, usize> = matching_with_index.into_par_iter().collect(); //{'A': 0, 'U': 20, 'O': 14, 'e': 30, 'i': 34, 'j': 35, 'K': 10 ....

    // long string of all binary
    let mut unadjusted_bi: String = removed_padding.par_chars().fold(String::new, |acc, each| {
        acc + &(to_binary06(*Base64_get_index.get(&each).unwrap()))
    }).collect();
    unadjusted_bi = unadjusted_bi[..unadjusted_bi.len() - count_added_zero].to_string();

    //chucks into size of 8
    let mut adjusted_bi: Vec<String> = Vec::new(); //["11011110", "10010110", "01111101", "011
    unadjusted_bi.par_chars().collect::<Vec<char>>().chunks(8).into_iter().for_each(|c| adjusted_bi.push(c.par_iter().collect()));

    //turn each binary into decimal number
    let to_return: Vec<u8> = adjusted_bi.par_iter().map(|st| bi_to_index(st.clone()) as u8).collect();

    Some(to_return)
}

fn to_binary06 (byte: usize) -> String { format!("{:06b}", byte ) }

#[cfg(test)]
mod tests {
    use crate::base64::{par_encode_base64, par_decode_base64};

    #[test]
    fn basic_encode() {
        assert_eq!(&"aGVsbG8xNTAxKys9", &par_encode_base64(b"hello1501++="));
        assert_eq!(&"bGlnaHQgd29yaw==", &par_encode_base64(b"light work"));
        assert_eq!(&"IBBnAwJnZw==", &par_encode_base64(b"\x20\x10g\x03\x02gg"));
        assert_eq!(&"bGlnaHQgdw==", &par_encode_base64(b"light w"));
        assert_eq!(&"bGlnaHQgd28=", &par_encode_base64(b"light wo"));
        assert_eq!(&"bGlnaHQgd29yay4=", &par_encode_base64(b"light work."));
    }

    #[test]
    fn basic_decode() {
        assert_eq!(Some(b"light work".to_vec()), par_decode_base64("bGlnaHQgd29yaw=="));
        assert_eq!(Some(b"hello1501++=".to_vec()), par_decode_base64("aGVsbG8xNTAxKys9"));
        assert_eq!(Some(b"\x20\x10g\x03\x02gg".to_vec()), par_decode_base64("IBBnAwJnZw=="));
        assert_eq!(None, par_decode_base64("IBBnAwJnZw-="));
        assert_eq!(None, par_decode_base64("IBBnAw=JnZw="));
        assert_eq!(None, par_decode_base64(""));
        assert_eq!(None, par_decode_base64("Hi Charlie"));
        assert_eq!(None, par_decode_base64("==="));
        assert_eq!(None, par_decode_base64("dfg====="));

    }
}

//    let mut Base64_list:Vec<char> = Vec::new();
//     Base64_list.append(&mut ('A'..='Z').collect::<Vec<char>>());
//     Base64_list.append(&mut ('a'..='z').collect::<Vec<char>>());
//     Base64_list.append(&mut ('0'..='9').collect::<Vec<char>>());
//     Base64_list.push('+');
//     Base64_list.push('/');
