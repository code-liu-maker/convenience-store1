🏪 便利店管理系统

基于 Vue3 + Spring Boot 的全栈项目，包含商品、订单、库存、供应商管理，支持三级角色权限（顾客/操作员/管理员）。

 📦 技术栈

- 前端：Vue3 + Element Plus + Axios + Vite
- 后端：Spring Boot 3 + MyBatis + MySQL


🚀 本地运行

 1. 后端

1. 在 MySQL 中创建数据库 `mydb`，导入 `db/mydb.sql` 文件
2. 打开 `backend/src/main/resources/application.properties`，修改数据库密码
3. 在 IDEA 中运行 `ConvenienceStoreApplication.java`

 2. 前端

```bash
cd frontend
npm install
npm run dev
```

访问 http://localhost:5173


🧪 测试账号

角色 账号 密码
超级管理员 admin 123456
门店操作员 operator 123456
普通顾客 customer 123456


✨ 项目亮点

· 逻辑删除：用 is_deleted 字段替代物理删除，解决外键约束冲突，历史数据可恢复
· 双重权限校验：前端根据用户角色动态渲染菜单 + 后端拦截器校验接口权限
· 库存审计追溯：入库/出库自动记录操作人和操作时间，满足审计需求