use std::{fs, io};
use chashmap::CHashMap;
use rayon::iter::*;
use rayon::prelude::*;
mod cwslice;

#[derive(Debug,PartialEq)]
struct FlightRecord {
    unique_carrier: String,
    actual_elapsed_time: i32,
    arrival_delay: i32,
}

#[allow(dead_code)]
fn parse_line(line: &str) -> Option<FlightRecord> {
    let adjusted_line = par_split(line, ',');
    Some(
        FlightRecord{
            unique_carrier: String::from(adjusted_line[8]),

            actual_elapsed_time: if adjusted_line[11].parse::<i32>().is_ok() {
                adjusted_line[11].parse().unwrap()
            } else { 0},

            arrival_delay: if adjusted_line[14].parse::<i32>().is_ok() {
                adjusted_line[14].parse().unwrap() } else { 0 },
        }
    )

}

#[allow(dead_code)]
pub fn ontime_rank(filename: &str) -> Result<Vec<(String, f64)>, io::Error> {
    let contents = fs::read_to_string(filename)?;
    let slice:&str = &contents;
    let all_lines = slice.lines().collect::<Vec<&str>>();
    let data_store = CHashMap::new();
    all_lines.into_par_iter().for_each( |line|{
        if parse_line(line).is_some() {
            let record = parse_line(line).unwrap();
            if record.arrival_delay <= 0 {
                data_store.upsert(record.unique_carrier,
                                  ||(1,1),
                                  |(on_time,total)| {
                                      *on_time += 1;
                                      *total += 1;
                                  })
            } else {
                data_store.upsert(record.unique_carrier,
                                  ||(0,1),
                                  |(on_time,total)| *total += 1)
            }
        }
    });

    data_store.remove("UniqueCarrier");
    Ok(
        data_store.into_iter()
            .map(|(key, (on_time,total))| (key, on_time as f64 / total as f64))
            .collect()
    )
}


//from lecture
pub fn plus_scan(xs:&[i32]) -> (Vec<i32>,i32) {
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
    (pfs,c_sum.clone())
}
pub fn par_split(st_buf: &str, split_char: char) -> Vec<&str> {
    let mut one_zero_vec:Vec<i32> = st_buf
        .par_chars()
        .into_par_iter()
        .map(|char|{ ( char == split_char) as i32 })
        .collect::<Vec<i32>>();
    let (mut index_vec, _ ) = plus_scan(one_zero_vec.as_slice());
    let mut to_return = vec![];
    let mut count = 0;
    (1..index_vec.len() - 1).into_iter().for_each(|cur| {
        if index_vec[cur] < index_vec[cur + 1] {
            let str = &st_buf[count..cur];
            to_return.push(str.clone());
            count = cur + 1;
        }
    });

    to_return.push(&st_buf[count..index_vec.len()]);
    to_return
}

