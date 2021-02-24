### error1

* 网络协议
  + dataLen + type + data

protected void decode(ctx, in, out) {
    int len = in.readableBytes();
    if (len < 4)
        return;
    
    // in buffer: ridx:0, widx:176
    
    in.markReaderIndex();
    
    int dataLen = in.readInt();
    
    if dataLen < 0 || dataLen > 1024 * 1024 {
        ctx.close();
        throw Error();
    }
    
    // 这有问题：
    // in buffer: ridx:4, widx:176
    // dataLen = 172
    // 此时：dataLen == in.readableBytes()
    if dataLen > in.readableBytes()
        in.resetReaderIndex();
        return;
        
    // 此时读取1个字节 
    byte type = in.readByte();
    
    // in buffer: ridx:5, widx:176
    // in.readableBytes() == 171了，所以dataLen > 171 报错。由于没有caughtException导致异常丢失，一时找不到问题
    byte[] data = new byte[bodyLen];
    in.readBytes(data);
}
