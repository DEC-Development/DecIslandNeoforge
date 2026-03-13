import os
import json
import re
from pathlib import Path

def parse_lang_file(file_path):
    """解析.lang文件并返回字典"""
    lang_dict = {}
    with open(file_path, 'r', encoding='utf-8') as f:
        for line in f:
            line = line.strip()
            if line and '=' in line:
                key, value = line.split('=', 1)
                lang_dict[key] = value
    return lang_dict

def generate_item_registration_codes():
    # 获取所有纹理文件 - 使用正确的路径
    texture_dir = Path("./pyscript/mask/data/textures").resolve()
    print(f"Looking for textures in: {texture_dir}")
    
    # 检查目录是否存在
    if not texture_dir.exists():
        print(f"Texture directory does not exist: {texture_dir}")
        return
        
    texture_files = [f for f in texture_dir.iterdir() if f.suffix == '.png']
    
    # 提取不带扩展名的文件名
    texture_names = []
    for file in texture_files:
        texture_names.append(file.stem)  # stem是不带扩展名的文件名
    
    # 读取语言文件 - 使用正确的路径
    en_lang_path = Path("./pyscript/mask/data/en_US.lang")
    zh_lang_path = Path("./pyscript/mask/data/zh_CN.lang")
    
    print(f"Looking for EN lang file: {en_lang_path}")
    print(f"Looking for ZH lang file: {zh_lang_path}")
    
    if not en_lang_path.exists():
        print(f"EN lang file does not exist: {en_lang_path}")
        return
        
    if not zh_lang_path.exists():
        print(f"ZH lang file does not exist: {zh_lang_path}")
        return
    
    en_data = parse_lang_file(en_lang_path)
    zh_data = parse_lang_file(zh_lang_path)
    
    # 提取语言文件中的物品键值对
    en_items = {}
    zh_items = {}
    
    for key, value in en_data.items():
        if key.startswith('item.dec:') and key.endswith('.name'):
            item_name = key.replace('item.dec:', '').replace('.name', '')
            en_items[item_name] = value
    
    for key, value in zh_data.items():
        if key.startswith('item.dec:') and key.endswith('.name'):
            item_name = key.replace('item.dec:', '').replace('.name', '')
            zh_items[item_name] = value
    
    # 准备输出结果
    output_lines = []
    missing_lang_entries = []
    
    for texture_name in sorted(texture_names):  # 排序以保持一致的输出顺序
        # 检查是否在语言文件中有条目
        # 纹理文件名转换为对应的物品键名（例如：frank_mask -> frank_mask）
        en_name = en_items.get(texture_name)
        zh_name = zh_items.get(texture_name)
        
        if en_name is None or zh_name is None:
            missing_lang_entries.append(texture_name)
        else:
            # 生成注册代码
            # 将文件名转换为大写常量格式（如frank_mask -> FRANK_MASK）
            const_name = texture_name.upper().replace('-', '_')  # 替换连字符为下划线
            
            # 确定物品类型（根据名称判断是否是特殊类型，如mask）
            item_type = "Item::new"  # 默认类型
            if 'mask' in texture_name.lower():
                item_type = "com.dec.decisland.item.custom.Mask::new"
            
            registration_code = f'''    public static final DeferredItem<Item> {const_name} = ModItems.registerItem(
            new ItemConfig.Builder("{texture_name}", Map.of(
                    "en_us", "{en_name}",
                    "zh_cn", "{zh_name}"
            )).func({item_type})
                    .creativeTab(creativeTab).build()
    );'''
            
            output_lines.append(registration_code)
    
    # 写入output.txt
    output_file = Path("./pyscript/mask/output.txt")
    with open(output_file, 'w', encoding='utf-8') as f:
        for line in output_lines:
            f.write(line + '\n\n')
        
        if missing_lang_entries:
            f.write("// 以下纹理文件在语言文件中没有对应的条目：\n")
            for entry in missing_lang_entries:
                f.write(f"// {entry}\n")
    
    print(f"生成了 {len(output_lines)} 个物品注册代码")
    if missing_lang_entries:
        print(f"发现 {len(missing_lang_entries)} 个缺少语言条目的纹理文件：")
        for entry in missing_lang_entries:
            print(f"  - {entry}")
    else:
        print("所有纹理文件都有对应的语言条目")

if __name__ == "__main__":
    generate_item_registration_codes()