from fastapi import status
from fastapi.exceptions import HTTPException

from travels.src.models import travel_applications, travel_routes, travels, users, cars, drivers
from travels.src.travel_create_app.schemas import CreateNewTrip

from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select, Table

from datetime import datetime


trip_status_translator = {
            "Trip was created": "not_started",
            "Started": "started",
            "Completed": "completed",
            "Cancelled": "cancelled"
        }

# internal server error
async def internal_server_error(e: Exception):
    # TODO: change exceptions to export to log file in the future
    print(e)
    raise HTTPException(
        status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
        detail={
            "statusCode": 500,
            "status": "INTERNAL_SERVER_ERROR",
            "success": False,
            "message": "Внутренняя ошибка сервера!",
            "data": None
        }
    )


# data validation based on database
async def trip_create_error_check(trip_data: CreateNewTrip, session: AsyncSession) -> list:
    trip_data_dict = trip_data.model_dump()
    # check if user exists
    await check_item_id_exists(trip_data_dict["driver_id"], users, session)

    # check if car exists
    await check_item_id_exists(trip_data_dict["car_id"], cars, session)

    # check if car exists and that it belongs to the user
    await check_car_belongs_driver(trip_data_dict["car_id"], trip_data_dict["driver_id"], session)

    # check if date is not from the past
    await check_departure_date(trip_data_dict["departure_date"], session)

    # check routes qty
    await check_routes_qty(trip_data_dict["routes"], session)

    routes = trip_data_dict["routes"]
    trip_data_dict.pop("routes")
    trip_data_dict.setdefault("travel_origin", routes[0])
    trip_data_dict.setdefault("travel_destination", routes[-1])
    trip_data_dict.setdefault("travel_status", "Trip was created")

    return [routes, trip_data_dict]


# Check if item exists in database
async def check_item_id_exists(item_id: int, table: Table, session: AsyncSession):
    query = select(table.c.id).where(table.c.id == item_id)

    # get user data from database
    try:
        query_result = await session.execute(query)
        query_data = query_result.fetchone()
    except Exception as e:
        await internal_server_error(e)

    if not query_data:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail={
                "satusCode": 404,
                "status": "NOT_FOUND",
                "success": False,
                "message": f"{table.name[:-1]} with id:{item_id} does not exist",
                "data": None}
        )


async def check_car_belongs_driver(car_id: int, driver_id: int, session: AsyncSession):
    query = select(drivers).where((drivers.c.user_id == driver_id) & (drivers.c.car_id == car_id))

    try:
        query_result = await session.execute(query)
        query_data = query_result.fetchone()
    except Exception as e:
        await internal_server_error(e)

    if not query_data:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail={
                "satusCode": 404,
                "status": "NOT_FOUND",
                "success": False,
                "message": f"Driver with id:{driver_id} does not have a car with id:{car_id}",
                "data": None}
        )


# check departure date
async def check_departure_date(departure_time: datetime, session: AsyncSession):
    current_time = datetime.now()
    if current_time > departure_time:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail={
                "statusCode": 400,
                "status": "BAD_REQUEST",
                "success": "false",
                "message": "К сожалению создать поездку в прошлом нельзя!",
                "data": None
            }
        )


# check if routes are more than 2
async def check_routes_qty(routes: list, session: AsyncSession):
    if routes is None or len(routes) < 2:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail={
                "statusCode": 400,
                "status": "BAD_REQUEST",
                "success": "false",
                "message": "Поездка должна содержать хотя бы начальную и конечную остановку!",
                "data": None
            }
        )
