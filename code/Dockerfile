FROM python:3.10-slim

WORKDIR /app

COPY requirements_api.txt /app/requirements.txt

RUN pip install --no-cache-dir -r /app/requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple

COPY . /app

EXPOSE 8001

CMD ["python", "api_server.py"]