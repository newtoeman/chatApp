# ChatApp 项目说明

## 项目概述

ChatApp 是一个使用 Android Jetpack Compose 构建的聊天应用程序。该项目使用 Kotlin 语言开发，采用现代 Android 开发技术栈，包括 Compose、ViewModel、Navigation、Room 数据库和 KSP (Kotlin Symbol Processing)。

主要特性：
- 仿微信的登录界面，支持手机号缓存和密码保存功能
- 使用 Jetpack Compose 进行现代化 UI 开发
- 响应式界面设计
- 使用 SharedPreferences 和 JSON 进行本地数据存储
- 集成 Room 数据库用于本地数据持久化
- 支持登录和注册功能
- 包含消息、联系人、朋友圈三大模块
- 支持顶部搜索和操作菜单
- 基于 Gson 的账号缓存系统，支持按最近使用时间排序

## 技术栈

- **语言**: Kotlin
- **框架**: Android Jetpack Compose
- **架构**: MVVM (Model-View-ViewModel)
- **依赖注入**: KSP (Kotlin Symbol Processing)
- **导航**: Jetpack Navigation Compose
- **数据库**: Room (SQLite object mapping)
- **构建系统**: Gradle (Kotlin DSL)
- **网络**: Mock API implementation with interface for real server integration
- **图片加载**: Coil Compose
- **图标库**: Material Icons Core & Extended
- **JSON处理**: Gson for account caching and management

## 项目结构

```
ChatApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/chatapp/
│   │   │   ├── data/               # 数据层 (Room database, DAO, entities, repository)
│   │   │   │   └── repository/     # 数据仓库接口及实现 (PreferencesRepository, PreferencesRepositoryImpl)
│   │   │   ├── json/               # JSON 相关模型 (AccountCache, AccountInfo)
│   │   │   ├── network/            # 网络层 (API service interface, mock implementation)
│   │   │   ├── ui/                 # UI 层 (包含 login, main, theme, components 等模块)
│   │   │   │   ├── components/     # 可复用 UI 组件 (CircularAvatar)
│   │   │   │   ├── login/          # 登录相关 UI 和 ViewModel
│   │   │   │   │   ├── LoginScreen.kt          # 登录界面 Composable
│   │   │   │   │   ├── LoginViewModel.kt       # 登录 ViewModel (管理登录状态和业务逻辑)
│   │   │   │   │   ├── LoginViewModelFactory.kt # 登录 ViewModel 工厂 (依赖注入)
│   │   │   │   │   ├── LoginUiState.kt         # 登录界面状态数据类
│   │   │   │   │   └── LoginState.kt           # 登录状态密封类
│   │   │   │   ├── main/           # 主界面相关 UI (ContactListScreen, MessageListScreen, MomentListScreen)
│   │   │   │   └── theme/          # 主题配置
│   │   │   ├── utils/              # 工具类 (PreferencesManager, TimeUtils)
│   │   │   ├── MainActivity.kt     # 主 Activity
│   │   │   └── MainScreen.kt       # 主界面
│   │   ├── res/                    # 资源文件
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml          # 依赖版本管理
├── build.gradle.kts                # 项目级构建配置
├── settings.gradle.kts             # 模块配置
└── gradlew                         # Gradle 包装器
```
```

## 核心功能

### 1. 登录/注册系统

- **登录界面** (`ui/login/LoginScreen.kt`): 使用 Compose 实现的现代化登录界面
- **账号缓存**: 支持缓存多个手机号，通过下拉框选择，按最近使用时间排序
- **密码保存**: 可为每个手机号单独保存密码
- **记住密码**: 通过复选框控制是否保存当前账号密码
- **注册功能**: 支持新用户注册
- **智能密码填充**: 选择不同手机号时，自动识别并填充对应的密码

### 2. 配置管理

- **PreferencesManager** (`utils/PreferencesManager.kt`): 单例类，负责管理本地存储
  - 缓存手机号列表
  - 为特定手机号保存/获取密码
  - 独立管理每个账号的密码
  - 清除缓存功能
  - 基于 Gson 实现的 JSON 序列化缓存系统
  - 使用 LRU (最近最少使用) 算法管理账号列表

### 3. 数据层架构

- **UserRepository** (`data/UserRepository.kt`): 用户数据仓库，负责用户数据的本地存储操作
- **PreferencesRepository** (`data/repository/PreferencesRepository.kt`): 偏好设置数据仓库接口
- **PreferencesRepositoryImpl** (`data/repository/PreferencesRepositoryImpl.kt`): 偏好设置数据仓库实现，封装了 PreferencesManager 的操作

### 4. 状态管理

- **LoginUiState** (`ui/login/LoginUiState.kt`): 登录界面状态数据类，统一管理登录/注册界面的所有状态
- **ViewModel 状态管理**: LoginViewModel 现在通过 LoginUiState 统一管理界面状态，避免了状态分散的问题



### 5. 主界面功能

- **底部导航栏**: 消息、联系人、朋友圈三大功能模块
- **顶部工具栏**: 搜索图标和更多操作下拉菜单（发起群聊、添加朋友、扫一扫）
- **消息列表**: `MessageListScreen.kt` - 展示聊天记录列表，支持长按菜单操作
- **联系人列表**: `ContactListScreen.kt` - 展示联系人列表
- **朋友圈列表**: `MomentListScreen.kt` - 展示朋友圈动态
- **登出功能**: 一键返回登录界面

## 构建和运行

### 环境要求
- Android Studio Hedgehog 或更高版本
- Android SDK 36 (编译版本)
- Android SDK 24+ (最低支持版本)
- Java 11 (或更高版本)
- Kotlin 2.0.10
- Gradle 8.13.0

### 构建命令
```bash
# 同步项目依赖
./gradlew sync

# 构建 Debug 版本
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug

# 运行测试
./gradlew test

# 清理项目
./gradlew clean
```

### 依赖管理

项目使用 `libs.versions.toml` 统一管理依赖版本：

- **Android Gradle Plugin**: 8.13.0
- **Kotlin**: 2.0.10
- **KSP**: 2.0.10-1.0.24
- **AndroidX Core KTX**: 1.10.1
- **Compose BOM**: 2024.09.00
- **Room**: 2.6.1
- **Navigation**: 2.9.6
- **Lifecycle**: 2.6.1
- **Activity Compose**: 1.11.0
- **Foundation**: 1.9.5
- **Material Icons Core**: 1.7.0
- **Material Icons Extended**: 1.7.0
- **Coil Compose**: 2.6.0
- **Gson**: 2.10.1

## 开发约定

### 代码风格
- 使用 Kotlin 代码风格指南
- Compose 组件使用 `@Composable` 注解
- 使用 `remember`、`mutableStateOf`、`StateFlow`/`MutableStateFlow` 等状态管理 API
- 遵循单向数据流原则
- 使用协程进行异步操作
- 使用 Material Design 3 组件和主题

### 资源管理
- 图片资源放在 `res/drawable/` 目录
- 字符串资源放在 `res/values/strings.xml`
- 颜色定义放在 `res/values/colors.xml`
- 主题定义放在 `res/values/themes.xml`

### 测试
- 单元测试使用 JUnit 4
- UI 测试使用 Compose 测试框架
- 集成测试使用 Espresso

## 网络和数据架构

### API 设计
- 使用 `ApiService` 接口定义网络请求
- 提供 `MockApiService` 实现用于开发和测试
- 支持登录、注册、获取用户信息等功能

### 数据模型
- **User**: 用户实体类，包含 id、手机号、密码、用户名等字段
- **ChatItem**: 聊天项实体类
- **ContactItem**: 联系人实体类
- **MomentItem**: 朋友圈动态实体类

### 数据库结构
- **UserDao**: 用户数据访问对象，提供登录、注册等数据库操作
- **AppDatabase**: 应用数据库抽象，集成所有 DAO
- **UserRepository**: 用户仓库，统一管理用户相关的数据操作

## 安全考虑

- **密码存储**: 当前实现中密码以明文形式存储在 SharedPreferences 中（适用于演示，生产环境应考虑加密）
- **数据隔离**: 每个手机号的密码独立存储，避免不同账号间密码混淆
- **隐私保护**: 用户可随时取消"记住密码"选项，系统将立即删除相应密码缓存
- **本地数据安全**: Room 数据库使用应用私有存储
- **JSON 缓存安全**: 使用 Gson 进行账号信息缓存，支持访问令牌和刷新令牌的管理

## 特色功能

### 1. 智能密码填充
- 选择不同手机号时，自动识别并填充对应的密码
- 如果当前手机号没有保存密码，清空密码框并取消记住密码选项

### 2. 独立缓存管理
- 每个手机号的密码独立存储和管理
- 避免了多个账号间密码互相影响的问题

### 3. LRU 账号缓存
- 基于 Gson 实现的 JSON 缓存系统
- 按最近使用时间对账号进行排序
- 最多缓存 10 个账号，超出时删除最久未使用的账号
- 支持访问令牌和刷新令牌的管理

### 4. 用户体验优化
- 初始加载时自动显示最近使用的账号
- 记住密码选项状态随手机号选择而自动调整
- 下拉列表清晰显示所有已缓存的手机号
- 支持登录/注册模式切换
- 仿微信界面设计，包含消息、联系人、朋友圈三大模块

### 5. 主界面功能
- 底部导航栏，支持三个主要功能模块切换
- 顶部搜索功能和更多操作菜单
- 登出按钮，一键返回登录界面
- 圆形头像组件，提供用户友好的视觉体验
- 消息列表支持未读数徽章和长按菜单操作

## 架构改进与优化

### 1. UI 与数据存储解耦
- **已解决**: 原先 LoginScreen 中直接创建 PreferencesManager 实例的问题已通过 Repository 模式解决
- **实现**: 创建了 PreferencesRepository 接口及其实现类，LoginViewModel 通过依赖注入获取 PreferencesRepository 实例，实现了解耦

### 2. ViewModel 职责明确化
- **已解决**: LoginViewModel 现在统一管理登录/注册的所有业务逻辑和状态
- **实现**: UI 层不再直接操作数据存储，而是通过 ViewModel 提供的方法进行交互

### 3. 状态管理统一化
- **已解决**: 通过 LoginUiState 数据类统一管理登录界面的所有状态
- **实现**: 所有状态（phone、selectedPhone、password、isRememberPassword 等）都通过 ViewModel 的 StateFlow 进行管理，避免了状态分散的问题

### 4. 下拉列表实现优化
- **已解决**: 原先使用硬编码位置实现的下拉列表已替换为 Compose 官方的 DropdownMenu 组件
- **实现**: 使用标准的 DropdownMenu/DropdownMenuItem 组件，自动处理位置计算和屏幕适配

### 5. 副作用优化
- **已解决**: 原先 LaunchedEffect 中直接修改 UI 状态的问题已解决
- **实现**: 副作用逻辑已移至 ViewModel 初始化和状态更新中，UI 层仅负责观察状态变化

### 6. 数据层架构完善
- **已解决**: 实现了完整的 Repository 模式
- **实现**: 通过 UserRepository 和 PreferencesRepository 为 UI 层提供统一的数据访问接口，整合本地数据源和偏好设置

### 7. 待改进方向

#### 7.1. 引入依赖注入框架
- **问题**: 当前项目仍使用手动依赖注入，组件间耦合度较高
- **改进**: 引入 Hilt 作为依赖注入框架，实现松耦合的架构设计

#### 7.2. 增强测试覆盖
- **问题**: 当前项目缺少单元测试和集成测试
- **改进**: 添加对 ViewModel 和 Repository 层的单元测试，提高代码质量

## 注意事项

1. 当前实现中密码以明文形式存储，生产环境应考虑加密存储
2. SharedPreferences 适用于轻量级数据存储，大量数据应考虑使用数据库
3. 应用卸载后，所有缓存数据将被清除
4. 不同用户之间的数据通过手机号隔离，确保数据独立性
5. 模拟 API 服务用于开发测试，需要替换为真实服务器实现
6. Material Icons Extended 提供了更多图标资源，如 Chat、Contacts、PhotoCamera 等
7. 使用 Coil 库进行图片加载和显示
8. Gson 库用于账号信息的 JSON 序列化和反序列化