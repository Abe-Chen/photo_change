#!/usr/bin/env python3

"""
拆分Java文件中的多个public类到单独的文件中
"""

import os
import re
import sys

def main():
    # 设置工作目录
    model_dir = "src/main/java/com/photochange/model"
    source_file = os.path.join(model_dir, "PoseModels.java")
    
    # 检查源文件是否存在
    if not os.path.exists(source_file):
        print(f"错误：源文件 {source_file} 不存在")
        return 1
    
    # 读取源文件内容
    with open(source_file, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # 提取包名
    package_match = re.search(r'package\s+([^;]+);', content)
    if not package_match:
        print("错误：无法找到包声明")
        return 1
    
    package_name = package_match.group(1)
    
    # 提取导入语句
    imports = re.findall(r'import\s+[^;]+;', content)
    imports_text = '\n'.join(imports)
    
    # 查找所有public类
    class_pattern = re.compile(r'/\*\*[^*]*\*+(?:[^/*][^*]*\*+)*/\s*public\s+class\s+(\w+)[^{]*\{', re.DOTALL)
    class_matches = list(class_pattern.finditer(content))
    
    if not class_matches:
        print("错误：未找到public类")
        return 1
    
    # 为每个类创建文件
    for i, match in enumerate(class_matches):
        class_name = match.group(1)
        start_pos = match.start()
        
        # 确定类的结束位置（处理嵌套括号）
        depth = 0
        end_pos = start_pos
        in_comment = False
        in_string = False
        escape = False
        
        for j, char in enumerate(content[start_pos:]):
            if in_string:
                if char == '\\' and not escape:
                    escape = True
                elif char == '"' and not escape:
                    in_string = False
                else:
                    escape = False
            elif in_comment:
                if char == '*' and j + 1 < len(content[start_pos:]) and content[start_pos + j + 1] == '/':
                    in_comment = False
            elif char == '/' and j + 1 < len(content[start_pos:]) and content[start_pos + j + 1] == '*':
                in_comment = True
            elif char == '"' and not in_comment:
                in_string = True
            elif char == '{' and not in_comment and not in_string:
                depth += 1
            elif char == '}' and not in_comment and not in_string:
                depth -= 1
                if depth == 0:
                    end_pos = start_pos + j + 1
                    break
        
        # 提取类的完整内容
        class_content = content[start_pos:end_pos]
        
        # 创建新文件
        new_file_path = os.path.join(model_dir, f"{class_name}.java")
        with open(new_file_path, 'w', encoding='utf-8') as f:
            f.write(f"package {package_name};\n\n")
            f.write(f"{imports_text}\n\n")
            f.write(class_content)
        
        print(f"已创建 {new_file_path}")
    
    # 备份原始文件
    backup_file = f"{source_file}.bak"
    os.rename(source_file, backup_file)
    print(f"原始文件已备份为 {backup_file}")
    
    return 0

if __name__ == "__main__":
    sys.exit(main())