-- 为项目订单添加审核权限

-- 首先查找项目订单菜单的menu_id
SELECT menu_id FROM sys_menu WHERE menu_name = '项目单主';

-- 假设项目订单菜单的menu_id是X，我们需要为其添加审核按钮权限
-- 请根据实际查询结果替换下面的@projectOrderMenuId

-- 插入审核按钮权限
INSERT INTO sys_menu (
    menu_name, 
    parent_id, 
    order_num, 
    path, 
    component, 
    is_frame, 
    is_cache, 
    menu_type, 
    visible, 
    status, 
    perms, 
    icon, 
    create_by, 
    create_time
) VALUES (
    '项目单审核', 
    (SELECT menu_id FROM sys_menu WHERE menu_name = '项目单主'), 
    5, 
    '#', 
    '', 
    1, 
    0, 
    'F', 
    '0', 
    '0', 
    'projectOrder:projectOrder:audit', 
    '#', 
    'admin', 
    NOW()
);

-- 为admin2角色分配审核权限
INSERT INTO sys_role_menu (
    role_id, 
    menu_id
) VALUES (
    (SELECT role_id FROM sys_role WHERE role_key = 'admin2'), 
    (SELECT menu_id FROM sys_menu WHERE perms = 'projectOrder:projectOrder:audit')
);
