include "common.thrift"

namespace * com.bmn.thrift.cs

const i16 BMN_VERSION = 3


enum RequestType {
    ALL
    HEARTBEAT
}

struct BmnRequest {
    1: required i16 version = BMN_VERSION
    2: required i64 tid
    3: required RequestType type
    4: optional bool success
    5: optional i32 cp = 100
    6: optional map<string, string> properties
    7: optional byte flag = 1 // v1|v2|v3|v4
    8: optional binary data
}

struct BmnResponse {
    1: required i64 tid
    2: required common.ErrorCode errorCode
    3: optional string message
    4: optional binary data
    5: optional string did
}