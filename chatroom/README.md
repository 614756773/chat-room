## Websocket API
#### 建立连接
ws://localhost:8888/cr/chat/{userId}
<br><br>

#### 发送消息
- 私聊
    ```
        {
            "fromUserId": "123",
            "toUserId": "789",
            "content": "789你好啊，我是123",
            "type": "SINGLE_SENDING"
        }
    ```
- 群聊
    ```
        {
            "fromUserId": "123",
            "toGroupId": "1",
            "content": "hello，1组的伙伴们，我是123",
            "avatarUrl": "/xxx/yyy/a",
            "type": "GROUP_SENDING"
        }
    ```
<br><br>

#### 发送文件
> 发送文件会有2次请求，先使用http请求把文件上传至服务端，
再使用websocket将文件的url发给聊天室的其他用户
- 上传文件到服务器
    - POST请求 localhost:8888/cr/file/upload
- 发送文件url
    ```
        {
            "fromUserId": "123",
            "avatarUrl": "/xxx/yyy/a",
            "toGroupId": "1",
            "originalFilename": "测试.txt",
            "fileUrl": "/file/123",
            "fileSize": "1024",
            "type": "GROUP_SENDING"
        }
    ```
<br><br>

#### 接收消息
> `群聊`,`私聊`,`文件传输`的接收数据格式都是一样的，区别在于`type`字段

```
    {
        "fromUserId": "123",
        "avatarUrl": "/xxx/yyy/a",
        "toGroupId": "1",
        "content": "hello，1组的伙伴们，我是123",
        "originalFilename": "",
        "fileUrl": "",
        "fileSize": "",
        "type": "GROUP_SENDING"
    }
```