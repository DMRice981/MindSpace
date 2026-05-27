# MindSpace 项目 Code Wiki

## 1 项目概述

### 1.1 项目简介

MindSpace 是一款专注于心情记录与灵感管理的 Android 移动应用，采用 **Android + Java + XML** 技术栈开发。应用提供心情记录、灵感笔记和数据可视化三大核心功能，帮助用户追踪情绪变化、管理灵感想法，并通过图表直观展示数据趋势。

### 1.2 项目特性

| 特性 | 说明 |
|------|------|
| MVVM 架构 | 采用 Model-View-ViewModel 设计模式，代码结构清晰，关注点分离 |
| Room 数据库 | 本地数据持久化，支持复杂查询和异步操作 |
| 数据可视化 | 集成 MPAndroidChart，支持折线图、饼图等多种图表 |
| Material Design | 遵循 Material Design 设计规范，界面美观一致 |
| 主题支持 | 支持日间和夜间模式自动切换 |

### 1.3 技术栈

| 组件 | 版本 | 说明 |
|------|------|------|
| Android Studio | Arctic Fox (2020.3.1) 或更高 | 集成开发环境 |
| JDK | JDK 8 或 JDK 11 | Java 编译环境 |
| Android SDK | API 23 - API 34 | 目标平台支持 |
| Gradle | 8.0 或更高 | 构建工具 |
| Room | 2.6.1 | 本地数据库 |
| MPAndroidChart | v3.1.0 | 图表库 |
| Material Components | 1.11.0 | UI 组件库 |
| Glide | 4.16.0 | 图片加载库 |

## 2 项目架构

### 2.1 架构设计

MindSpace 采用 **MVVM（Model-View-ViewModel）** 架构模式，实现关注点分离：

| 层级 | 职责 | 组件 |
|------|------|------|
| View (UI) | 负责界面展示和用户交互 | Activities、Fragments、Adapters |
| ViewModel | 业务逻辑处理，持有 UI 数据 | MoodViewModel、NoteViewModel、DataViewModel |
| Repository | 数据仓库，协调数据源 | MoodRepository、NoteRepository |
| Model | 数据模型定义 | Entity、DAO 接口定义 |

### 2.2 目录结构

项目采用标准 Android 项目结构，按功能模块组织代码：

```
app/
├── src/main/java/com/mindspace/app/
│   ├── ui/                          # 界面层
│   │   ├── activities/              # Activity 页面
│   │   │   ├── MainActivity.java    # 主活动
│   │   │   ├── MoodActivity.java    # 心情记录页
│   │   │   ├── NoteActivity.java    # 笔记页面
│   │   │   └── DataActivity.java    # 数据统计页
│   │   ├── fragments/               # Fragment 片段
│   │   │   ├── HomeFragment.java    # 首页片段
│   │   │   ├── NotesFragment.java   # 笔记片段
│   │   │   └── DataFragment.java    # 数据片段
│   │   └── adapters/                # 适配器
│   │       ├── MoodAdapter.java     # 心情列表适配器
│   │       └── NoteAdapter.java     # 笔记列表适配器
│   ├── data/                        # 数据层
│   │   ├── local/                   # 本地数据库
│   │   │   ├── AppDatabase.java     # 数据库实例
│   │   │   ├── MoodDao.java         # 心情数据访问对象
│   │   │   ├── NoteDao.java         # 笔记数据访问对象
│   │   │   └── UserDao.java         # 用户数据访问对象
│   │   ├── model/                   # 数据模型
│   │   │   ├── MoodRecord.java      # 心情记录实体
│   │   │   ├── Note.java            # 笔记实体
│   │   │   └── User.java            # 用户实体
│   │   └── repository/              # 仓库层
│   │       ├── MoodRepository.java  # 心情仓库
│   │       └── NoteRepository.java  # 笔记仓库
│   ├── viewmodel/                   # ViewModel 层
│   │   ├── MoodViewModel.java       # 心情视图模型
│   │   ├── NoteViewModel.java       # 笔记视图模型
│   │   └── DataViewModel.java       # 数据视图模型
│   ├── utils/                       # 工具类
│   │   ├── DateUtils.java           # 日期工具
│   │   ├── ChartUtils.java          # 图表工具
│   │   └── Constants.java           # 常量定义
│   └── service/                     # 服务
│       ├── NotificationService.java  # 通知服务
│       └── BackupService.java       # 备份服务
├── src/main/res/                    # 资源文件
│   ├── layout/                      # 布局文件
│   ├── drawable/                    # 图片资源
│   ├── values/                      # 值资源
│   └── menu/                        # 菜单
└── build.gradle                     # 模块构建配置
```

### 2.3 当前项目状态

当前代码仓库处于项目初始化阶段，包含以下基础文件：

| 文件路径 | 说明 |
|----------|------|
| `MainActivity.java` | 主活动入口 |
| `activity_main.xml` | 主界面布局 |
| `themes.xml` | 日间主题配置 |
| `build.gradle.kts` | 构建配置 |

项目框架已搭建完成，待后续开发逐步添加业务模块代码。

## 3 依赖关系管理

### 3.1 依赖配置

项目使用 Gradle 的 Kotlin DSL 进行依赖管理，版本信息集中配置在 `gradle/libs.versions.toml` 中。

### 3.2 核心依赖库

```gradle
dependencies {
    // Room 数据库
    implementation "androidx.room:room-runtime:2.6.1"
    annotationProcessor "androidx.room:room-compiler:2.6.1"
    
    // Lifecycle 组件
    implementation "androidx.lifecycle:lifecycle-viewmodel:2.7.0"
    implementation "androidx.lifecycle:lifecycle-livedata:2.7.0"
    
    // MPAndroidChart 图表库
    implementation "com.github.PhilJay:MPAndroidChart:v3.1.0"
    
    // Material Design
    implementation "com.google.android.material:material:1.11.0"
    
    // Glide 图片加载
    implementation "com.github.bumptech.glide:glide:4.16.0"
    annotationProcessor "com.github.bumptech.glide:compiler:4.16.0"
    
    // ConstraintLayout
    implementation "androidx.constraintlayout:constraintlayout:2.2.1"
}
```

### 3.3 依赖版本说明

| 依赖库 | 版本 | 用途 |
|--------|------|------|
| androidx.room | 2.6.1 | 本地数据库 ORM 框架 |
| androidx.lifecycle | 2.7.0 | ViewModel 和 LiveData |
| MPAndroidChart | v3.1.0 | 数据可视化图表 |
| material | 1.11.0 | Material Design 组件 |
| glide | 4.16.0 | 图片加载和缓存 |
| junit | 4.13.2 | 单元测试框架 |
| espresso | 3.7.0 | UI 测试框架 |

## 4 核心功能模块

### 4.1 心情记录功能

**功能描述**：用户可以选择表情图标记录当日心情，并添加文字描述，支持查看最近的心情记录列表。

**心情类型定义**：

| 类型 | 说明 | 分数 |
|------|------|------|
| HAPPY | 开心 | 5 |
| EXCITED | 兴奋 | 5 |
| NORMAL | 平静 | 3 |
| SAD | 难过 | 2 |
| ANGRY | 生气 | 1 |

**心情记录实体类**：

```java
@Entity(tableName = "mood_records")
public class MoodRecord {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @ColumnInfo(name = "mood_type")
    private String moodType;
    
    @ColumnInfo(name = "mood_score")
    private int moodScore;
    
    private String content;
    
    @ColumnInfo(name = "created_at")
    private long createdAt;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getMoodType() { return moodType; }
    public void setMoodType(String moodType) { this.moodType = moodType; }
    
    public int getMoodScore() { return moodScore; }
    public void setMoodScore(int moodScore) { this.moodScore = moodScore; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
```

**心情数据访问接口**：

```java
@Dao
public interface MoodDao {
    @Query("SELECT * FROM mood_records ORDER BY created_at DESC")
    LiveData<List<MoodRecord>> getAllMoods();
    
    @Query("SELECT * FROM mood_records WHERE created_at >= :startTime")
    List<MoodRecord> getMoodsSince(long startTime);
    
    @Insert
    void insert(MoodRecord mood);
    
    @Delete
    void delete(MoodRecord mood);
}
```

### 4.2 灵感笔记功能

**功能描述**：用户可以创建、编辑、删除笔记，支持分类标签和颜色标记。

**笔记分类**：

| 分类 | 说明 |
|------|------|
| 工作 | 工作相关笔记 |
| 学习 | 学习和技能提升 |
| 生活 | 日常生活记录 |
| 创意 | 创意想法和灵感 |

**笔记实体类**：

```java
@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @ColumnInfo(name = "title")
    private String title;
    
    @ColumnInfo(name = "content")
    private String content;
    
    @ColumnInfo(name = "category")
    private String category;
    
    @ColumnInfo(name = "color_tag")
    private String colorTag;
    
    @ColumnInfo(name = "created_at")
    private long createdAt;
    
    @ColumnInfo(name = "updated_at")
    private long updatedAt;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getColorTag() { return colorTag; }
    public void setColorTag(String colorTag) { this.colorTag = colorTag; }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
```

### 4.3 数据可视化功能

**心情趋势折线图**：

```java
// 初始化折线图
LineChart lineChart = findViewById(R.id.line_chart);
lineChart.setTouchEnabled(true);
lineChart.setDragEnabled(true);
lineChart.setScaleEnabled(true);

// 准备数据
List<Entry> entries = new ArrayList<>();
for (int i = 0; i < moodData.size(); i++) {
    entries.add(new Entry(i, moodData.get(i).getMoodScore()));
}

// 创建数据集
LineDataSet dataSet = new LineDataSet(entries, "心情趋势");
dataSet.setColor(Color.parseColor("#6C63FF"));
dataSet.setCircleColor(Color.parseColor("#FF6584"));
dataSet.setLineWidth(2f);
dataSet.setCircleRadius(4f);

// 设置数据
LineData lineData = new LineData(dataSet);
lineChart.setData(lineData);
lineChart.invalidate();
```

**灵感分类饼图**：

```java
// 初始化饼图
PieChart pieChart = findViewById(R.id.pie_chart);
pieChart.setUsePercentValues(true);
pieChart.getDescription().setEnabled(false);

// 准备数据
List<PieEntry> entries = new ArrayList<>();
entries.add(new PieEntry(workCount, "工作"));
entries.add(new PieEntry(studyCount, "学习"));
entries.add(new PieEntry(lifeCount, "生活"));
entries.add(new PieEntry(ideaCount, "创意"));

// 创建数据集
PieDataSet dataSet = new PieDataSet(entries, "笔记分类");
dataSet.setColors(Color.parseColor("#FF6584"), 
                   Color.parseColor("#6C63FF"),
                   Color.parseColor("#4CAF50"), 
                   Color.parseColor("#FF9800"));

// 设置数据
PieData pieData = new PieData(dataSet);
pieChart.setData(pieData);
pieChart.invalidate();
```

## 5 数据库操作

### 5.1 Room 数据库配置

**数据库类定义**：

```java
@Database(entities = {MoodRecord.class, Note.class, User.class}, 
          version = 1, 
          exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    
    private static volatile AppDatabase INSTANCE;
    
    public abstract MoodDao moodDao();
    public abstract NoteDao noteDao();
    public abstract UserDao userDao();
    
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class, 
                            "mindspace_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
```

### 5.2 常用数据库操作

| 操作 | 同步方式 | 异步方式 |
|------|----------|----------|
| 插入 | `dao.insert(record)` | AsyncTask 或 RxJava |
| 查询 | `dao.getAll()` | LiveData 自动更新 |
| 更新 | `dao.update(record)` | @Update 注解 |
| 删除 | `dao.delete(record)` | @Delete 注解 |

### 5.3 数据库迁移

当数据库结构变更时，需要实现 Migration：

```java
static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
        database.execSQL("ALTER TABLE mood_records "
            + " ADD COLUMN weather TEXT");
    }
};

// 在数据库构建时添加迁移
Room.databaseBuilder(context, AppDatabase.class, "mindspace_db")
    .addMigrations(MIGRATION_1_2)
    .build();
```

## 6 开发规范

### 6.1 编码规范

**命名规范**：

| 类型 | 规范 | 示例 |
|------|------|------|
| 类名 | 大驼峰命名 | MoodRecord, NoteAdapter |
| 方法名 | 小驼峰命名 | getMoodById(), saveNote() |
| 变量名 | 小驼峰命名 | moodList, noteTitle |
| 常量 | 全大写下划线 | MAX_MOOD_COUNT, DB_NAME |
| 布局文件 | 小写下划线 | activity_main.xml, item_mood.xml |

**代码格式**：

- 使用 4 个空格缩进（不使用 Tab）
- 每行代码不超过 120 个字符
- 类和方法必须添加 JavaDoc 注释
- 使用有意义的变量名，避免单字母命名

### 6.2 Git 工作流

采用 Git Flow 分支模型：

```bash
# 主分支
main        - 生产环境代码，永远可发布
develop     - 开发分支，集成所有功能

# 临时分支
feature/*   - 功能分支，从 develop 创建
release/*   - 发布分支，从 develop 创建
hotfix/*    - 热修复分支，从 main 创建

# 开发流程
git checkout develop
git checkout -b feature/mood-record
git commit -m "feat: 添加心情记录功能"
git push origin feature/mood-record
# 创建 Pull Request 合并到 develop
```

### 6.3 提交规范

使用 Conventional Commits 规范：

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Type 类型**：

| 类型 | 说明 |
|------|------|
| feat | 新功能 |
| fix | 修复 |
| docs | 文档更新 |
| style | 代码格式（不影响功能） |
| refactor | 重构 |
| test | 测试相关 |
| chore | 构建/工具相关 |

**提交示例**：

```
feat(mood): 添加心情记录保存功能
fix(database): 修复数据库升级崩溃问题
docs(readme): 更新安装说明
```

## 7 调试与测试

### 7.1 调试技巧

**日志输出**：

```java
// 使用 Log 类输出调试信息
Log.d(TAG, "调试信息");
Log.i(TAG, "普通信息");
Log.w(TAG, "警告信息");
Log.e(TAG, "错误信息", exception);

// 在 Logcat 中过滤
// 标签过滤：tag:MindSpace
// 级别过滤：level:debug
```

**断点调试**：

- 在代码行号处点击设置断点
- 点击 Debug 按钮启动调试模式
- 使用 F8（Step Over）、F7（Step Into）进行单步调试
- 在 Variables 窗口查看变量值

### 7.2 单元测试

```java
public class MoodRepositoryTest {
    
    @Test
    public void testInsertMood() {
        // Given
        MoodRecord mood = new MoodRecord();
        mood.setMoodType("HAPPY");
        mood.setMoodScore(5);
        
        // When
        repository.insert(mood);
        
        // Then
        List<MoodRecord> result = repository.getAllMoods().getValue();
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
```

### 7.3 性能优化

| 优化项 | 建议 |
|--------|------|
| 列表滚动 | 使用 RecyclerView 的 ViewHolder 模式，避免频繁创建视图 |
| 图片加载 | 使用 Glide 加载图片，设置合适的图片尺寸 |
| 数据库查询 | 避免在主线程执行耗时查询，使用 LiveData 异步加载 |
| 内存泄漏 | 使用 WeakReference 避免匿名内部类持有 Activity 引用 |

## 8 构建与发布

### 8.1 应用签名

**生成签名密钥**：

```bash
keytool -genkey -v \
    -keystore mindspace.keystore \
    -alias mindspace \
    -keyalg RSA \
    -keysize 2048 \
    -validity 10000
```

**Gradle 签名配置**：

```gradle
android {
    signingConfigs {
        release {
            storeFile file("mindspace.keystore")
            storePassword "your_store_password"
            keyAlias "mindspace"
            keyPassword "your_key_password"
        }
    }
    
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 
                         'proguard-rules.pro'
        }
    }
}
```

### 8.2 构建发布版本

```bash
# 清理并构建发布版本
./gradlew clean assembleRelease

# 输出 APK 路径
app/build/outputs/apk/release/app-release.apk
```

### 8.3 Google Play 上架

1. 注册 Google Play 开发者账号（$25 一次性费用）
2. 准备应用素材
   - 应用图标（512x512 PNG）
   - 截图（至少 2 张，推荐 1080x1920）
   - 功能介绍视频（可选）
3. 填写应用信息
   - 应用名称、简短描述、完整描述
   - 隐私政策链接
   - 内容分级问卷
4. 上传 APK 并发布

## 9 常见问题

### 9.1 构建问题

**Q: Gradle 同步失败？**

A: 检查网络连接，尝试以下方法：

- File → Invalidate Caches / Restart
- 检查 gradle-wrapper.properties 中的 distributionUrl
- 使用阿里云镜像加速

```gradle
allprojects {
    repositories {
        maven { url 'https://maven.aliyun.com/repository/public' }
        maven { url 'https://maven.aliyun.com/repository/google' }
        google()
        mavenCentral()
    }
}
```

**Q: 依赖库冲突？**

A: 使用 exclude 排除冲突依赖：

```gradle
implementation('com.some.library:library:1.0.0') {
    exclude group: 'com.android.support'
    exclude module: 'support-v4'
}
```

### 9.2 运行时问题

**Q: 应用崩溃，如何查看日志？**

A: 使用 Logcat 查看崩溃日志：

- Android Studio 底部工具栏打开 Logcat
- 选择设备和应用进程
- 过滤 Error 级别日志
- 查看堆栈跟踪信息定位问题

**Q: 数据库操作导致 ANR？**

A: 确保数据库操作在后台线程执行：

```java
new AsyncTask<Void, Void, Void>() {
    @Override
    protected Void doInBackground(Void... voids) {
        moodDao.insert(mood);
        return null;
    }
}.execute();
```

## 10 附录

### 10.1 配色方案

| 用途 | 色值 | 说明 |
|------|------|------|
| 主色 | #6C63FF | 温柔紫蓝，用于顶部栏、主按钮 |
| 强调色 | #FF6584 | 粉红，用于保存按钮、浮动按钮 |
| 背景色 | #F5F5F5 | 浅灰，页面背景 |
| 文字色 | #333333 | 深灰，主要文字 |
| 次要文字 | #666666 | 中灰，次要信息 |

### 10.2 项目配置清单

| 文件路径 | 用途 |
|----------|------|
| `app/build.gradle.kts` | 应用模块构建配置 |
| `app/src/main/AndroidManifest.xml` | 应用组件声明 |
| `app/src/main/java/com/example/mindspace/MainActivity.java` | 主活动代码 |
| `app/src/main/res/layout/activity_main.xml` | 主界面布局 |
| `app/src/main/res/values/themes.xml` | 日间主题配置 |
| `app/src/main/res/values-night/themes.xml` | 夜间主题配置 |
| `app/src/main/res/values/strings.xml` | 字符串资源 |
| `app/src/main/res/values/colors.xml` | 颜色资源 |
| `gradle/libs.versions.toml` | 依赖版本目录 |
| `settings.gradle.kts` | 项目设置 |
| `gradle.properties` | Gradle 全局属性 |
| `proguard-rules.pro` | 代码混淆规则 |

### 10.3 参考资源

| 资源 | 链接 |
|------|------|
| Android 开发者文档 | https://developer.android.com |
| Room 数据库指南 | https://developer.android.com/training/data-storage/room |
| MPAndroidChart 文档 | https://github.com/PhilJay/MPAndroidChart |
| Material Design 组件 | https://material.io/develop/android |
| Glide 图片加载 | https://github.com/bumptech/glide |

### 10.4 版本历史

| 版本 | 日期 | 变更内容 |
|------|------|----------|
| v1.0 | 2026-05 | 初始版本，包含心情记录、灵感笔记、数据可视化三大核心功能 |

---

*本文档版本：1.0*  
*生成日期：2026-05-24*  
*项目版本：MindSpace v1.0*
