-- sys_permission 表
CREATE TABLE sys_permission (
                                permission_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '权限ID',
                                permission_name VARCHAR(255) NOT NULL COMMENT '权限名称',
                                permission_url VARCHAR(255) COMMENT '权限对应的URL',
                                permission_method VARCHAR(50) COMMENT '权限对应的HTTP方法'
) COMMENT='权限表';

-- sys_role 表
CREATE TABLE sys_role (
                          role_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
                          role_name VARCHAR(255) NOT NULL COMMENT '角色名称',
                          role_description VARCHAR(255) COMMENT '角色描述'
) COMMENT='角色表';

-- sys_user 表
CREATE TABLE sys_user (
                          user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
                          user_account VARCHAR(255) NOT NULL UNIQUE COMMENT '用户账号',
                          user_avatar_url VARCHAR(255) COMMENT '用户头像URL',
                          user_password VARCHAR(255) NOT NULL COMMENT '用户密码',
                          user_email VARCHAR(255) COMMENT '用户邮箱',
                          is_account_non_expired BOOLEAN DEFAULT TRUE COMMENT '账号是否未过期',
                          is_account_non_locked BOOLEAN DEFAULT TRUE COMMENT '账号是否未锁定',
                          is_credentials_non_expired BOOLEAN DEFAULT TRUE COMMENT '凭据是否未过期',
                          is_enabled BOOLEAN DEFAULT TRUE COMMENT '账号是否启用',
                          user_name VARCHAR(255) COMMENT '用户名称'
) COMMENT='用户表';

-- user_menu 表
CREATE TABLE user_menu (
                           user_menu_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '菜单ID',
                           user_menu_name VARCHAR(255) NOT NULL COMMENT '菜单名称',
                           user_menu_path VARCHAR(255) COMMENT '菜单路径',
                           user_menu_component VARCHAR(255) COMMENT '菜单组件',
                           permission_id BIGINT COMMENT '权限ID',
                           user_menu_icon VARCHAR(255) COMMENT '菜单图标',
                           parent_id BIGINT COMMENT '父菜单ID',
                           FOREIGN KEY (permission_id) REFERENCES sys_permission(permission_id)
) COMMENT='用户菜单表';

-- user_role 表
CREATE TABLE user_role (
                           user_id BIGINT NOT NULL COMMENT '用户ID',
                           role_id BIGINT NOT NULL COMMENT '角色ID',
                           PRIMARY KEY (user_id, role_id),
                           FOREIGN KEY (user_id) REFERENCES sys_user(user_id),
                           FOREIGN KEY (role_id) REFERENCES sys_role(role_id)
) COMMENT='用户角色关联表';

-- role_permission 表
CREATE TABLE role_permission (
                                 role_id BIGINT NOT NULL COMMENT '角色ID',
                                 permission_id BIGINT NOT NULL COMMENT '权限ID',
                                 PRIMARY KEY (role_id, permission_id),
                                 FOREIGN KEY (role_id) REFERENCES sys_role(role_id),
                                 FOREIGN KEY (permission_id) REFERENCES sys_permission(permission_id)
) COMMENT='角色权限关联表';