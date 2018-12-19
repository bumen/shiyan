namespace * com.baw.thrift



enum Language {
    ALL
    ZH
    EN
}

struct BawResult {
    1: required i32 code
    2: required string tip
    3: required string type
    4: required string data
    5: required string m
}
