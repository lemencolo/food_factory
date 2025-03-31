# Food Factory

## food_factory_spring

用于构建项目后端代码

### 文件说明

-src - main - java -com -food_factory_spring - 存放的所有主要方法

-src - main - java -com -food_factory_spring - common -存放所有通用代码

- CodeGenerator 存放生成代码 当有一个新的表时 只需要修改表名称 即可快速生成所需mapper层等内容
- generate_Hash 生成Hash值 用于数据库中密码的存放
- MybatisPlusConfig 用于控制分页的代码
- QueryPageParam 查询页面所需要的参数
- Result 页面返回结果封装
- TableNameGenerator 生成实时表名
- TableNameValidator 验证表名

-src - main - java -com -food_factory_spring - controller 控制层代码

-src - main - java -com -food_factory_spring - entity 实例层代码

-src - main - java -com -food_factory_spring -mapper mapper层代码

-src - main - java -com -food_factory_spring -mappers 自定义查询函数代码

-src - main - java -com -food_factory_spring -service 服务层代码

-src - main - java -com -food_factory_spring -FoodFactorySpringApplication.java 启动文件



src/main/resources/application.properties 存放服务的一般地址 例如数据库地址，数据库的名称和密码

pom.xml 存放所需要的依赖包文件

## food_factory_web

用于构建项目前端代码 启动方式

```bash
cd food_factory_web
npm run dev
```

如果要局域网连接 --host

```bash
cd food_factory_web
npm run dev -- --host
```

使用Vue CLI 、Vue、element ui进行构建

### 文件说明

src\components\Management.vue  实时任务 整个管理页面的总体布局

src\components\Counts_View.vue  实现测量 当有数字输入时 进行拍照并传入后端

src\components\TodoList.vue 显示当前测量和下一个测量

src\components\Video_View.vue 显示图像

src\components\Login.vue 登录页面

src\components\Index.vue 登录后的初始页面



/Index

src\components\Index\Main.vue 登陆后页面布局

src\components\Index\Aside.vue 侧栏

src\components\Index\Header.vue 头部



src\components\Index\SudoSetting.vue 超级管理员管理

src\components\Index\FormulaManagement 配方管理

src\components\Index\TodoManagement 任务管理



src\js\ocrRequest.js 封装好的OCR请求函数

src\js\request.js 封装好的一般请求函数

src\router\index.ts 路由管理

src\store\index.ts 动态路由实现



src\App.vue 总呈现

src\main.ts 所需要的依赖配置

## paddle_ocr

原本存放的是paddle的模型文件 但更改后变为了保存识别图的文件夹

## food_factory.sql

存放的是目前所有的存在的表

## Food_Factory-Todos - 02_04

存放我能想到的未完成的各项事务