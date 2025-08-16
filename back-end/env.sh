export AI_DASHSCOPE_API_KEY=sk-50431ef9331643cba1e8c6899d561b84
docker run -d \
  --name my_postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 15432:5432 \
  postgres