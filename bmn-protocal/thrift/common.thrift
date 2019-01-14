namespace * com.bmn.thrift

/**
 * Struct defined
 */

enum BmnPartner {
    ALL
    BAIDU
    TENCENT
    ALIBABA
    LENOVO
}


struct BmnAnswer {
    1: required string r
    2: required string c
    3: optional string t
    4: optional string u
}


struct BmnResult {
    1: required string q
    2: required string op
    3: required string type
    4: required list<BmnAnswer> answer
}
