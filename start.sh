#!/bin/bash
set -e

cd /app/python-rag
uvicorn app:app --host 127.0.0.1 --port 8000 &
PYTHON_PID=$!

cd /usr/local/tomcat
catalina.sh run &
TOMCAT_PID=$!

trap 'kill "$PYTHON_PID" "$TOMCAT_PID" 2>/dev/null || true' TERM INT

wait -n "$PYTHON_PID" "$TOMCAT_PID"