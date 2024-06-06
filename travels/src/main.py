from fastapi import FastAPI
from fastapi.exceptions import RequestValidationError
from fastapi.responses import JSONResponse
from starlette.exceptions import HTTPException as StarletteHTTPException
from travels.src.passenger_application_app.router import router as passenger_application_router
from travels.src.travel_create_app.router import router as travel_create_router

app = FastAPI()

app.include_router(passenger_application_router)
app.include_router(travel_create_router)


@app.exception_handler(StarletteHTTPException)
async def http_exception_handler(request, exc):
    return JSONResponse(exc.detail, status_code=exc.status_code)


@app.exception_handler(RequestValidationError)
async def validation_exception_handler(request, exc):
    return JSONResponse(exc.detail, status_code=exc.status_code)
