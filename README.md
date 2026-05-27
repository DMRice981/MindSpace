# MindSpace - 心情记录与灵感笔记 App

一款专注于心情记录与灵感管理的 Android 移动应用，帮助用户追踪情绪变化、管理灵感想法，并通过图表直观展示数据趋势。

## 📱 功能特性

- **心情记录** - 选择表情图标记录每日心情，支持文字描述和心情评分
- **灵感笔记** - 创建、编辑、删除笔记，支持分类标签和颜色标记
- **数据可视化** - 通过折线图和饼图直观展示心情趋势和笔记分类统计
- **主题支持** - 自动适配日间和夜间模式
- **本地存储** - 使用 Room 数据库本地持久化数据

## 🛠 技术栈

- **平台**: Android
- **语言**: Java
- **架构**: MVVM (Model-View-ViewModel)
- **数据库**: Room
- **图表**: MPAndroidChart
- **UI**: Material Design 3, XML Layout
- **图片加载**: Glide
- **构建工具**: Gradle (Kotlin DSL)

## 📦 核心依赖

| 依赖库 | 版本 | 说明 |
|--------|------|------|
| Room | 2.6.1 | 本地数据库 ORM |
| Lifecycle | 2.7.0 | ViewModel & LiveData |
| MPAndroidChart | v3.1.0 | 数据可视化图表 |
| Material | 1.11.0 | Material Design 组件 |
| Glide | 4.16.0 | 图片加载库 |

## 🚀 快速开始

### 环境要求

- Android Studio Arctic Fox (2020.3.1) 或更高版本
- JDK 8 或 JDK 11
- Android SDK API 23-34
- Gradle 8.0 或更高版本

### 构建项目

1. **克隆项目**
   ```bash
   git clone https://github.com/your-repo/mindspace.git
   cd mindspace
   ```

2. **使用 Android Studio 打开**
   - File → Open → 选择项目根目录
   - 等待 Gradle 同步完成

3. **同步 Gradle 依赖**
   - 点击 "Sync Now" 或执行:
   ```bash
   ./gradlew build
   ```

4. **运行项目**
   - 连接设备或启动模拟器
   - 点击 Run 按钮 (▶)

## 📂 项目结构

```
app/
├── src/main/java/com/mindspace/app/
│   ├── ui/                      # 界面层
│   │   ├── activities/          # Activity 页面
│   │   ├── fragments/           # Fragment 片段
│   │   └── adapters/            # RecyclerView 适配器
│   ├── data/                    # 数据层
│   │   ├── local/              # Room 数据库
│   │   ├── model/              # 实体类
│   │   └── repository/         # 数据仓库
│   ├── viewmodel/              # ViewModel 层
│   ├── utils/                   # 工具类
│   └── service/                # 服务
├── src/main/res/               # 资源文件
│   ├── layout/                 # 布局文件
│   ├── values/                 # 值资源
│   └── menu/                   # 菜单
└── build.gradle.kts            # 构建配置
```

## 🎨 主要模块

### 心情记录 (MoodRecord)
- 支持 5 种心情类型：开心、兴奋、平静、难过、生气
- 心情评分 1-5 分
- 记录时间和文字内容

### 灵感笔记 (Note)
- 4 种分类：工作、学习、生活、创意
- 颜色标签标记
- 支持编辑和删除

### 数据统计 (Data)
- 心情趋势折线图
- 心情分布饼图
- 平均心情指数计算

## 📋 开发规范

### 编码规范
- 使用 4 个空格缩进
- 类名使用大驼峰命名
- 方法和变量使用小驼峰命名
- 常量使用全大写下划线命名

### Git 提交规范
使用 Conventional Commits:
```
feat(mood): 添加心情记录保存功能
fix(database): 修复数据库升级崩溃问题
docs(readme): 更新安装说明
```

## 🔧 构建与发布

### 构建 Debug 版本
```bash
./gradlew assembleDebug
```

### 构建 Release 版本
```bash
./gradlew assembleRelease
```

### 生成签名密钥
```bash
keytool -genkey -v \
    -keystore mindspace.keystore \
    -alias mindspace \
    -keyalg RSA \
    -keysize 2048 \
    -validity 10000
```

## 📚 文档

详细开发文档请查看 [MindSpace 开发手册](./MindSpace_开发手册.docx)

完整项目文档请查看 [Code Wiki](./CODE_WIKI.md)

## 🐛 常见问题

**Q: Gradle 同步失败？**
- 检查网络连接
- 尝试 File → Invalidate Caches / Restart
- 使用阿里云镜像加速

**Q: 应用崩溃？**
- 使用 Logcat 查看崩溃日志
- 检查设备兼容性（需要 Android 7.0+）

## 📄 许可证

本项目仅供学习交流使用。

## 👤 作者

MindSpace Team - 2026

---

*如果您觉得这个项目有用，请给个 Star ⭐*
