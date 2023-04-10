//use std::simd::usizex2;

#[allow(dead_code)]
pub fn is_palindrome(s: &str) -> bool {
    let mut adjusted_str = s.chars().filter(|cha| !cha.is_whitespace() ).collect::<String>().to_lowercase();
    adjusted_str.chars().rev().eq(adjusted_str.chars())
}

#[allow(dead_code)]
pub fn is_pangram(s: &str) -> bool {
    //let lower = s.to_lowercase().chars().collect::<Vec<char>>();
    let lower = s.to_lowercase();
    ('a'..='z').into_iter().all(|ch| lower.contains(ch))
}

#[cfg(test)]
mod tests {
    use crate::pangrindrome::{is_palindrome, is_pangram};

    #[test]
    fn basic_is_palindrome() {
        assert_eq!(true, is_palindrome("r"));
        assert_eq!(true, is_palindrome("abba"));
        assert_eq!(true, is_palindrome("abcba"));
        assert_eq!(false, is_palindrome("abc"));
        assert_eq!(true, is_palindrome("Was it a car or a cat I saw"));
    }

    #[test]
    fn basic_pangram() {
        let quick_brown_fox = "The quick brown fox jumps over the lazy Dog";
        assert_eq!(true, is_pangram(quick_brown_fox));
        let quick_prairie_dog = "The quick prairie dog jumps over the lazy fox";
        assert_eq!(false, is_pangram(quick_prairie_dog));
        let quick_prairie_az = "abcdefghijklmnopqrstuvwxyz";
        assert_eq!(true, is_pangram(quick_prairie_az));
        let quick_prairie_ay = "abcdefghijklmnopqrstuvwxy";
        assert_eq!(false, is_pangram(quick_prairie_ay));
    }
}
