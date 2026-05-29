# Cixiu Docker Deployment

This project is deployed as one Docker Compose stack:

- `frontend`: Nginx static site and `/api` reverse proxy
- `backend`: Spring Boot API
- `ai-service`: Flask AI service
- `mysql`: MySQL 8
- `redis`: Redis 7
- `elasticsearch`: Elasticsearch 8.10.2
- local uploads: files served from `/uploads`

## 1. Prepare `.env`

Edit `.env` in the same directory as `docker-compose.yml`.

Replace every `change_me` value before starting the stack:

```env
MYSQL_ROOT_PASSWORD=change_me_mysql_root_password
MYSQL_PASSWORD=change_me_mysql_app_password
REDIS_PASSWORD=change_me_redis_password
DASHSCOPE_API_KEY=change_me_dashscope_api_key
```

`VITE_API_BASE_URL` should stay empty for the default single-server deployment. The frontend Nginx will proxy browser requests from `/api` to the backend container.

For local server deployment, keep file storage local:

```env
FILE_STORAGE_TYPE=local
FILE_STORAGE_PUBLIC_URL=/uploads
```

When `FILE_STORAGE_TYPE=local`, Aliyun OSS variables can stay empty. Only set `ALIYUN_OSS_BUCKET`, `ALIYUN_OSS_ACCESS_KEY_ID`, and `ALIYUN_OSS_ACCESS_KEY_SECRET` if you later switch `FILE_STORAGE_TYPE=oss`.

## 2. Start

Create local upload directories before the first start:

```bash
mkdir -p data/uploads code/uploads code/results
```

```bash
docker compose up -d --build
```

Open:

```text
http://SERVER_IP/
```

## 3. Check Health

```bash
docker compose ps
docker compose logs -f backend
docker compose logs -f ai-service
curl http://localhost/api/health
curl http://localhost/
```

The backend health endpoint is:

```text
/api/health
```

Do not use `/actuator/health`; this project does not include Spring Boot Actuator.

## 4. Rebuild After Code Changes

```bash
docker compose up -d --build
```

To rebuild one service:

```bash
docker compose up -d --build backend
docker compose up -d --build frontend
docker compose up -d --build ai-service
```

## 5. Data Persistence

Persistent Docker volumes:

- `mysql_data`
- `redis_data`
- `es_data`

AI model and generated files are mounted from the workspace:

- `./code/checkpoints:/app/checkpoints`
- `./code/uploads:/app/uploads`
- `./code/results:/app/results`

Uploaded platform files are mounted from:

- `./data/uploads:/app/uploads`
- `./data/uploads:/usr/share/nginx/uploads:ro`

## 6. Useful Commands

```bash
docker compose ps
docker compose logs -f
docker compose logs -f backend
docker compose restart backend
docker compose down
docker compose up -d
```

Backup MySQL:

```bash
set -a; source .env; set +a
docker exec cixiu-mysql mysqldump -uroot -p"$MYSQL_ROOT_PASSWORD" cixiu > cixiu_backup.sql
```

Restore MySQL:

```bash
set -a; source .env; set +a
docker exec -i cixiu-mysql mysql -uroot -p"$MYSQL_ROOT_PASSWORD" cixiu < cixiu_backup.sql
```
