from fastapi import FastAPI, HTTPException
from fastapi.responses import PlainTextResponse
from pydantic import BaseModel
from rag_service import RAGPipeline
import os
from dotenv import load_dotenv

load_dotenv()

app = FastAPI()
rag = RAGPipeline()

class QueryRequest(BaseModel):
    query: str
    top_k: int = 3

@app.post("/chat", response_class=PlainTextResponse)
def chat_endpoint(req: QueryRequest):
    try:
        answer = rag.query(req.query, top_k=req.top_k)
        return str(answer)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/")
def health_check():
    return {"status": "Python RAG Service is running!"}