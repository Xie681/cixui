"""
AI服务API测试脚本
"""
import requests
import json
import sys


def test_health_check():
    """测试健康检查接口"""
    print("\n" + "="*50)
    print("测试 1: 健康检查")
    print("="*50)

    try:
        response = requests.get('http://localhost:8001/health', timeout=5)
        print(f"状态码: {response.status_code}")
        print(f"响应: {json.dumps(response.json(), indent=2, ensure_ascii=False)}")
        return response.status_code == 200
    except Exception as e:
        print(f"❌ 错误: {str(e)}")
        return False


def test_image_to_embroidery():
    """测试图片转刺绣接口"""
    print("\n" + "="*50)
    print("测试 2: 图片转刺绣")
    print("="*50)

    # 使用一个公开的测试图片URL
    test_url = "https://picsum.photos/200/200"

    payload = {
        "image_url": test_url,
        "style": "SU",
        "return_base64": False
    }

    try:
        response = requests.post(
            'http://localhost:8001/api/image-to-embroidery',
            json=payload,
            timeout=30
        )
        print(f"状态码: {response.status_code}")
        result = response.json()
        print(f"响应: {json.dumps(result, indent=2, ensure_ascii=False)}")

        if result.get('success'):
            print(f"✅ 处理成功!")
            print(f"   - 针数: {result.get('stitch_count')}")
            print(f"   - 颜色数: {result.get('color_count')}")
            print(f"   - 结果URL: {result.get('result_url')}")
            return True
        else:
            print(f"❌ 处理失败: {result.get('error')}")
            return False

    except Exception as e:
        print(f"❌ 错误: {str(e)}")
        return False


def test_text_to_embroidery():
    """测试文字转刺绣接口"""
    print("\n" + "="*50)
    print("测试 3: 文字转刺绣")
    print("="*50)

    payload = {
        "text": "测试文字",
        "font": "Arial",
        "size": 48,
        "style": "SU"
    }

    try:
        response = requests.post(
            'http://localhost:8001/api/text-to-embroidery',
            json=payload,
            timeout=30
        )
        print(f"状态码: {response.status_code}")
        result = response.json()
        print(f"响应: {json.dumps(result, indent=2, ensure_ascii=False)}")

        if result.get('success'):
            print(f"✅ 处理成功!")
            return True
        else:
            print(f"❌ 处理失败: {result.get('error')}")
            return False

    except Exception as e:
        print(f"❌ 错误: {str(e)}")
        return False


def test_oss_image():
    """测试从OSS下载图片"""
    print("\n" + "="*50)
    print("测试 4: 阿里云OSS图片处理（需要真实OSS URL）")
    print("="*50)

    # 这里需要替换为实际的OSS URL
    oss_url = "https://jy-javaweb.oss-cn-beijing.aliyuncs.com/images/test.png"

    print(f"测试URL: {oss_url}")
    print("⚠️  请确保该URL存在且可访问，否则此测试将失败")

    payload = {
        "image_url": oss_url,
        "style": "SU"
    }

    try:
        response = requests.post(
            'http://localhost:8001/api/image-to-embroidery',
            json=payload,
            timeout=30
        )
        print(f"状态码: {response.status_code}")
        result = response.json()

        if result.get('success'):
            print(f"✅ OSS图片处理成功!")
            return True
        else:
            print(f"⚠️  OSS图片处理失败（可能URL不存在）: {result.get('error')}")
            return False

    except Exception as e:
        print(f"⚠️  错误（可能URL不存在）: {str(e)}")
        return False


def run_all_tests():
    """运行所有测试"""
    print("\n" + "="*60)
    print("刺绣AI服务 API 测试")
    print("="*60)

    results = []

    # 测试1: 健康检查
    results.append(("健康检查", test_health_check()))

    # 测试2: 图片转刺绣
    results.append(("图片转刺绣", test_image_to_embroidery()))

    # 测试3: 文字转刺绣
    results.append(("文字转刺绣", test_text_to_embroidery()))

    # 测试4: OSS图片（可选）
    # results.append(("OSS图片处理", test_oss_image()))

    # 输出测试结果
    print("\n" + "="*60)
    print("测试结果汇总")
    print("="*60)

    for name, success in results:
        status = "✅ 通过" if success else "❌ 失败"
        print(f"{name}: {status}")

    passed = sum(1 for _, success in results if success)
    total = len(results)

    print("\n" + "-"*60)
    print(f"总计: {passed}/{total} 通过")
    print("-"*60)

    return passed == total


if __name__ == '__main__':
    success = run_all_tests()
    sys.exit(0 if success else 1)
