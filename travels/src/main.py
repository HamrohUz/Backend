from fastapi import FastAPI, Request
from src.passenger_application_app.router import router as passenger_application_router

app = FastAPI()

app.include_router(passenger_application_router)
