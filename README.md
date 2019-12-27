# chat-room
聊天室
- 运行条件
    - jdk8+
    - mysql
    - chrome浏览器`非必须，只是chrome浏览器下访问效果更好`
- 运行前
    - 修改application.yml文件中的数据库配置
    - 在你自己的mysql中创建一个名为`chat_room`的数据库<br>
    最好使用utf8mb4字符集，后期会考虑存储emoji
- 运行
    - 运行Application的main方法
    - 访问[localhost:8888/cr/chatroom.html](localhost:8888/cr/chatroom.html)