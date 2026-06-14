# MindSpace

MindSpace 是一款面向个人情绪管理、笔记记录和社交陪伴的 Android 应用。项目以本地 Room 数据库为基础，结合 Supabase 云服务提供联网好友、消息中心和一对一聊天能力，适合用于心理健康记录、学习实践、课程设计或 Android 综合项目展示。

## 项目状态

当前版本：`3.0.0`

当前已验证：

```bash
./gradlew.bat :app:testDebugUnitTest --no-daemon
./gradlew.bat :app:assembleDebug --no-daemon
```

两项均已通过，可正常编译并生成 Debug APK。

## 核心功能

### 1. 用户系统

- 用户注册
- 用户登录
- 管理员登录
- 登录会话保持
- 用户头像选择
- 普通用户和管理员角色区分
- 用户封禁状态支持
- 登录/注册后自动同步 Supabase 云端用户资料

默认测试账号：

| 角色 | 用户名 | 密码 |
| --- | --- | --- |
| 普通用户 | `user` | `user123` |
| 管理员 | `admin` | `admin123` |

### 2. 心情记录

- 支持多种心情类型记录
- 支持填写心情文字说明
- 支持历史心情记录查看
- 使用 Room 本地数据库保存记录
- 可用于后续统计分析和趋势展示

### 3. 笔记管理

- 创建笔记
- 编辑笔记
- 删除笔记
- 查看笔记列表
- 支持本地持久化保存

### 4. 数据统计

- 心情趋势统计
- 心情分布统计
- 图表可视化展示
- 基于 MPAndroidChart 实现统计图表

### 5. 社区功能

- 心情动态/帖子展示
- 社区内容浏览
- 评论、点赞、举报等社区能力预留或部分实现
- 管理员可用于内容管理场景

### 6. 好友系统

好友系统已接入 Supabase 云服务，支持联网用户互动。

已实现：

- 搜索云端用户
- 发送好友申请
- 查看收到的好友申请
- 同意好友申请
- 拒绝好友申请
- 查看好友列表
- 删除好友
- 从好友列表进入聊天

入口：

```text
个人中心 → 好友管理
```

### 7. 消息中心

新增独立消息中心，用于集中查看好友申请和最近聊天。

已实现：

- 好友申请入口
- 好友申请数量角标
- 最近聊天列表
- 最近一条消息预览
- 最近消息时间展示
- 未读消息数量角标
- 点击会话进入聊天页
- 手动刷新消息列表

入口：

```text
个人中心 → 消息中心
```

### 8. 一对一聊天

聊天功能基于 Supabase REST API 实现。

已实现：

- 好友一对一聊天
- 拉取聊天记录
- 发送文字消息
- 消息空状态提示
- 发送中状态
- 防止重复发送
- 进入聊天页自动刷新消息
- 进入聊天页自动标记该好友消息为已读

### 9. 个人中心

- 用户资料展示
- 头像展示
- 主题/设置入口
- 数据备份和恢复入口
- 数据同步入口
- 心情提醒入口
- 消息中心入口
- 好友管理入口
- 退出登录

### 10. 管理员功能

- 管理员登录
- 管理后台入口
- 用户或内容管理能力预留
- 支持区分管理员账号和普通用户账号

## 技术栈

### Android

| 技术 | 当前配置 |
| --- | --- |
| 语言 | Java |
| 最低版本 | minSdk 24 |
| 目标版本 | targetSdk 33 |
| 编译版本 | compileSdk 33 |
| Java 编译目标 | Java 17 |
| Android Gradle Plugin | 8.7.3 |
| Gradle Wrapper | 8.13 |

### 主要依赖

| 依赖 | 版本 | 用途 |
| --- | --- | --- |
| AppCompat | 1.5.1 | 兼容 Android UI 基础能力 |
| Material Components | 1.7.0 | Material 风格控件 |
| RecyclerView | 1.3.2 | 列表展示 |
| Room | 2.4.3 | 本地数据库 |
| Lifecycle | 2.5.1 | 生命周期组件 |
| Navigation | 2.5.3 | 导航组件 |
| MPAndroidChart | 3.1.0 | 数据图表 |
| Glide | 4.15.0 | 图片加载 |
| Retrofit | 2.9.0 | 网络请求 |
| OkHttp | 4.10.0 | HTTP 客户端 |
| Gson Converter | 2.9.0 | JSON 转换 |
| JUnit | 4.13.2 | 单元测试 |

### 云服务

项目使用 Supabase 提供联网能力：

- 云端用户资料表
- 好友申请表
- 好友关系表
- 聊天消息表
- 未读消息状态

当前 Android 端通过 Supabase REST API 访问云端数据。

## 项目结构

```text
MindSpace1/
├── app/
│   ├── src/main/
│   │   ├── java/com/mindspace/app/
│   │   │   ├── data/
│   │   │   │   ├── dao/              # Room DAO
│   │   │   │   ├── database/         # Room 数据库
│   │   │   │   ├── model/            # 本地数据模型
│   │   │   │   └── repository/       # 本地和云端数据仓库
│   │   │   ├── network/
│   │   │   │   └── supabase/         # Supabase API、DTO、Client
│   │   │   ├── ui/
│   │   │   │   ├── activities/       # Activity 页面
│   │   │   │   ├── adapters/         # RecyclerView 适配器
│   │   │   │   └── fragments/        # Fragment 页面
│   │   │   ├── utils/                # 会话、主题、备份、提醒、UI工具
│   │   │   └── viewmodel/            # ViewModel
│   │   └── res/
│   │       ├── drawable/             # 背景、角标、头像等资源
│   │       ├── layout/               # 页面和列表项布局
│   │       ├── menu/                 # 菜单资源
│   │       └── values/               # 颜色、字符串、样式
│   └── build.gradle
├── gradle/wrapper/
│   └── gradle-wrapper.properties
├── build.gradle
├── gradle.properties
└── README.md
```

## 关键页面

| 页面 | 文件 | 说明 |
| --- | --- | --- |
| 登录页 | `LoginActivity.java` | 用户/管理员登录 |
| 注册页 | `RegisterActivity.java` | 新用户注册 |
| 主页面 | `MainActivity.java` | 底部导航主容器 |
| 个人中心 | `ProfileFragment.java` | 用户资料、设置、好友和消息入口 |
| 好友管理 | `FriendsActivity.java` | 搜索用户、好友申请、好友列表 |
| 消息中心 | `MessagesActivity.java` | 最近聊天、未读数、好友申请角标 |
| 聊天页 | `ChatActivity.java` | 一对一文字聊天 |
| 管理后台 | `AdminActivity.java` | 管理员功能入口 |
| 笔记页 | `NoteActivity.java` | 笔记编辑和查看 |

## 快速开始

### 1. 环境要求

推荐使用：

- Android Studio 最新稳定版
- Gradle JDK 选择 Android Studio Embedded JDK / JBR
- 不建议使用 JDK 25 构建本项目

Android Studio 设置路径：

```text
File → Settings → Build, Execution, Deployment → Build Tools → Gradle → Gradle JDK
```

请选择：

```text
Embedded JDK
```

### 2. 编译项目

Windows：

```bash
./gradlew.bat :app:assembleDebug --no-daemon
```

macOS / Linux：

```bash
./gradlew :app:assembleDebug --no-daemon
```

### 3. 运行单元测试

Windows：

```bash
./gradlew.bat :app:testDebugUnitTest --no-daemon
```

macOS / Linux：

```bash
./gradlew :app:testDebugUnitTest --no-daemon
```

### 4. 安装到设备

Windows：

```bash
./gradlew.bat :app:installDebug --no-daemon
```

macOS / Linux：

```bash
./gradlew :app:installDebug --no-daemon
```

## Supabase 配置说明

Supabase 配置位于：

```text
app/src/main/java/com/mindspace/app/network/supabase/SupabaseConfig.java
```

当前项目使用 Supabase anon public key 访问 REST API。anon key 属于客户端公开 key，但仍建议：

- 不要把 service_role key 放进 Android 客户端
- 不要在日志里输出敏感信息
- 生产环境应配合 Row Level Security 规则限制数据访问

## Supabase 数据表建议

当前 Android 端依赖以下云端表结构：

```text
profiles
friend_requests
friendships
chat_messages
```

建议字段：

```text
profiles:
- id
- username
- email
- avatar
- is_admin
- is_banned
- created_at

friend_requests:
- id
- from_user_id
- to_user_id
- status
- created_at
- updated_at

friendships:
- id
- user_id
- friend_id
- friend_username
- created_at

chat_messages:
- id
- sender_id
- receiver_id
- content
- is_read
- created_at
```

## 构建问题解决

如果遇到以下错误：

```text
JdkImageTransform
core-for-system-modules.jar
jlink.exe
androidJdkImage
```

请确认工具链版本：

```text
Android Gradle Plugin: 8.7.3
Gradle Wrapper: 8.13
Gradle JDK: Embedded JDK / JBR
Java target: 17
```

本项目已经将这些配置固定在仓库中。通常不要随意切换到 JDK 25。

如果 Gradle 下载超时，可以确认：

```text
gradle/wrapper/gradle-wrapper.properties
```

当前下载超时配置为：

```properties
networkTimeout=120000
```

## 最近完成的功能

- Supabase 好友系统
- Supabase 一对一聊天
- 消息中心
- 最近聊天列表
- 未读消息角标
- 好友申请角标
- 聊天消息自动标记已读
- 登录/注册加载态
- 好友页刷新和空状态
- 聊天页发送状态和空状态
- Gradle / AGP / JDK 构建链路稳定化

## 后续可扩展方向

- Supabase Realtime 实时消息推送
- 图片消息
- 语音消息
- 好友备注
- 拉黑和举报
- 社区云端化
- 心情日历
- 周报/月报
- 数据云同步
- 通知栏消息提醒
- 更完整的管理员后台

## 许可证

本项目用于学习、课程设计和功能演示。若用于正式发布，请根据实际情况补充许可证、隐私政策和用户协议。

## 维护说明

如果继续开发，建议每次修改后至少运行：

```bash
./gradlew.bat :app:testDebugUnitTest --no-daemon
./gradlew.bat :app:assembleDebug --no-daemon
```

确保单元测试和 Debug APK 打包均通过后再继续扩展。