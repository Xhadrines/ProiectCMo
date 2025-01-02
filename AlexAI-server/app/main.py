from fastapi import FastAPI
from app.routes.llama_routes import router

app = FastAPI()

# Include rutelor din llama_routes
app.include_router(router)
