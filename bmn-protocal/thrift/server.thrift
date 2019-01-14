include "common.thrift"

namespace * com.bmn.thrift.cs

const i16 BMN_VERSION = 3


struct BmnRequest {
    1: required string key
    2: required bool success
    3: optional i32 cp        // d|s
    4: optional map<string, string> properties
    5: optional byte flag = 1 // v1|v2|v3|v4
    6: required binary data
}

