#!/bin/bash

# 设置工作目录
MODEL_DIR="src/main/java/com/photochange/model"
cd "$(dirname "$0")"

# 确保目录存在
mkdir -p "$MODEL_DIR"

# 源文件
SOURCE_FILE="$MODEL_DIR/PoseModels.java"

# 检查源文件是否存在
if [ ! -f "$SOURCE_FILE" ]; then
    echo "错误：源文件 $SOURCE_FILE 不存在"
    exit 1
fi

# 提取包名
PACKAGE_NAME=$(grep -m 1 "^package" "$SOURCE_FILE" | sed 's/package \([^;]*\);/\1/')

# 创建临时目录
TEMP_DIR="model_temp"
mkdir -p "$TEMP_DIR"

# 提取所有类名和它们的内容
echo "正在分析源文件..."

# 使用awk提取所有public类
awk '/public class ([A-Za-z0-9_]+)/ {
    match($0, /public class ([A-Za-z0-9_]+)/, arr);
    class_name = arr[1];
    file_name = class_name ".java";
    in_class = 1;
    depth = 0;
    print "package " ENVIRON["PACKAGE_NAME"] ";\n\n" > ENVIRON["TEMP_DIR"] "/" file_name;
    # 复制导入语句
    system("grep -E \"^import\" " ENVIRON["SOURCE_FILE"] " >> " ENVIRON["TEMP_DIR"] "/" file_name);
    print "\n" >> ENVIRON["TEMP_DIR"] "/" file_name;
}

# 如果在类内部，则复制内容
{
    if (in_class) {
        print $0 >> ENVIRON["TEMP_DIR"] "/" file_name;
        if ($0 ~ /{/) depth++;
        if ($0 ~ /}/) depth--;
        if (depth == 0 && $0 ~ /}/) in_class = 0;
    }
}' PACKAGE_NAME="$PACKAGE_NAME" "$SOURCE_FILE"

# 移动文件到模型目录
echo "正在创建单独的类文件..."
for file in "$TEMP_DIR"/*.java; do
    if [ -f "$file" ]; then
        filename=$(basename "$file")
        echo "创建 $MODEL_DIR/$filename"
        mv "$file" "$MODEL_DIR/$filename"
    fi
done

# 清理临时目录
rmdir "$TEMP_DIR"

# 重命名原始文件作为备份
mv "$SOURCE_FILE" "${SOURCE_FILE}.bak"

echo "完成！所有类已分离到单独的文件中。"
echo "原始文件已备份为 ${SOURCE_FILE}.bak"