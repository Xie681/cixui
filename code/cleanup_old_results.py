"""
定期清理旧的AI处理结果文件
可以设置为定时任务运行（如每天凌晨2点）

Windows 计划任务：
schtasks /create /tn "清理AI结果文件" /tr "python E:\Projects\Java projects\cixiu\code\cleanup_old_results.py" /sc daily /st 02:00

Linux cron：
0 2 * * * cd /path/to/cixiu/code && python cleanup_old_results.py
"""
import os
import time
from pathlib import Path
import logging

# 配置
RESULTS_DIR = './results/api_results'
MAX_AGE_DAYS = 7

# 日志配置
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)


def cleanup_old_files():
    """删除超过指定天数的文件"""
    if not os.path.exists(RESULTS_DIR):
        logger.warning(f"目录不存在: {RESULTS_DIR}")
        return

    now = time.time()
    max_age_seconds = MAX_AGE_DAYS * 24 * 60 * 60
    deleted_count = 0
    total_size = 0

    logger.info(f"开始清理 {RESULTS_DIR} 中超过 {MAX_AGE_DAYS} 天的文件...")

    for file_path in Path(RESULTS_DIR).glob('*'):
        if file_path.is_file():
            file_age = now - file_path.stat().st_mtime
            if file_age > max_age_seconds:
                try:
                    file_size = file_path.stat().st_size
                    file_path.unlink()
                    deleted_count += 1
                    total_size += file_size
                    logger.info(f"✅ 删除: {file_path.name} ({file_size / 1024:.2f} KB)")
                except Exception as e:
                    logger.error(f"❌ 删除失败: {file_path.name}, 错误: {e}")

    if deleted_count > 0:
        logger.info(f"清理完成: 删除 {deleted_count} 个文件，释放空间 {total_size / 1024 / 1024:.2f} MB")
    else:
        logger.info("没有需要清理的文件")


if __name__ == '__main__':
    try:
        cleanup_old_files()
    except Exception as e:
        logger.error(f"清理过程出错: {e}")
        raise
