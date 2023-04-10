use std::borrow::Borrow;
use std::collections::HashMap;

#[allow(dead_code)]
pub fn to_roman(n: u16) -> String {
    let rom_vec: Vec<(&str,u16)> = vec![("M",1000),("CM",900),("D",500),("CD",400),("C",100),("XC",90),("L",50),("XL",40),("X",10),("IX",9),("V",5),("IV",4),("I",1)];
    let mut given_n: u16 = n.clone();
    let mut to_return: String = String::from("");
    while given_n > 0 {
        for &(alpha,number) in rom_vec.iter() {
            if given_n >= number {
                to_return += alpha;
                given_n -= number;
                break;
            }
        }
    }
    to_return
}

#[allow(dead_code)]
pub fn parse_roman(roman_number: &str) -> u16 {
    //let rom: Vec<(&str,u16)> = vec![("M",1000),("CM",900),("D",500),("CD",400),("C",100),("XC",90),("L",50),("XL",40),("X",10),("IX",9),("V",5),("IV",4),("I",1)];
    let rom: Vec<(char,u16)> = vec![('M',1000),('D',500),('C',100),('L',50),('X',10),('V',5),('I',1)];

    let mut romMap: HashMap<char,u16> = HashMap::new();
    for &(ch,n) in rom.iter() { romMap.insert(ch,n); }
    //let mut given_rom:&str = roman_number.clone();
    let mut count:u16 = 0;
    // while !given_rom.is_empty() {
    //     for &(alpha,number) in rom.iter() {
    //         if given_rom.len() > 1 && (& given_rom[0..2]).eq(alpha) {
    //             count+=number;
    //             given_rom = &given_rom[2..];
    //         } else if given_rom.chars().nth(0).unwrap().to_string().eq(alpha) {
    //             count+=number;
    //             given_rom = &given_rom[1..];
    //         }
    //     }
    // }

    let mut prev_temp: u16 = 0;
    for i in roman_number.chars() {
        let cur = *romMap.get(&i).unwrap();
        if prev_temp < cur { count += (cur-2*(prev_temp))}
        else {count += cur;}
        prev_temp = *romMap.get(&i).unwrap();
    }

    count
}

#[cfg(test)]
mod tests {
    use super::{parse_roman, to_roman};

    #[test]
    fn basic_digits() {
        assert_eq!("I", to_roman(1));
        assert_eq!("V", to_roman(5));
        assert_eq!("X", to_roman(10));
        assert_eq!("L", to_roman(50));
        assert_eq!("C", to_roman(100));
    }

    #[test]
    fn basic_mixture() {
        assert_eq!("II", to_roman(2));
        assert_eq!("IV", to_roman(4));
        assert_eq!("IX", to_roman(9));
        assert_eq!("XII", to_roman(12));
        assert_eq!("XIV", to_roman(14));
        assert_eq!("MCMLIV", to_roman(1954));
    }

    #[test]
    fn Mudmi_test() {
        assert_eq!("LXXXVII", to_roman(87));
        assert_eq!("CMXCIX", to_roman(999));
        assert_eq!("MDLXIX", to_roman(1569));
        assert_eq!(1569, parse_roman("MDLXIX"));
        assert_eq!(999, parse_roman("CMXCIX"));
        assert_eq!(87, parse_roman("LXXXVII"));
    }

        #[test]
    fn basic_parsing() {
        assert_eq!(3, parse_roman("III"));
        assert_eq!(4, parse_roman("IV"));
        assert_eq!(8, parse_roman("VIII"));
        assert_eq!(19, parse_roman("XIX"));
    }

}
