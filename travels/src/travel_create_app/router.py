from fastapi import APIRouter, Depends, HTTPException, status

from travels.src.database import get_async_session
from travels.src.models import travels, travel_progress, travel_routes, passengers

from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import insert, select, extract

from travels.src.travel_create_app.utils import trip_create_error_check
from travels.src.travel_create_app.schemas import CreateNewTrip, GetTripInfo, FindTrip
from travels.src.travel_create_app.utils import internal_server_error, trip_status_translator
from datetime import datetime

router = APIRouter(
    prefix="/api/trip",
    tags=["Trips"]
)


@router.post("/create")
async def trip_create(trip_data: CreateNewTrip, session: AsyncSession = Depends(get_async_session)):
    # check all prerequisites for all possible errors
    routes, trip_data = await trip_create_error_check(trip_data, session)

    statement_trip_create = insert(travels).values(**trip_data).returning(travels.c.id)

    try:
        travel_id_data = await session.execute(statement_trip_create)
        travel_id = travel_id_data.fetchone()[0]
        await session.commit()

        statement_travel_progress_add = insert(travel_progress).values(
            travel_id=travel_id, progress_status="Not_started"
        )

        await session.execute(statement_travel_progress_add)
        await session.commit()

        for i, route in enumerate(routes):
            statement_travel_route = insert(travel_routes).values(travel_id=travel_id, route_address=route, priority=i)
            await session.execute(statement_travel_route)
            await session.commit()

    except Exception as e:
        await internal_server_error(e)

    # if everything is alright proceed with adding info to database
    return {
        "satusCode": status.HTTP_200_OK,
        "status": "OK",
        "success": True,
        "message": f"Trip was created successfully!",
        "data": {
            "trip_id": travel_id
        }
    }


@router.get("/user-trips-info")
async def user_trips_info(user_id: int,  session: AsyncSession = Depends(get_async_session)):
    try:
        query_as_passenger = select(passengers.c.travel_id).where(passengers.c.passenger_id == user_id)
        query_as_passenger_result = await session.execute(query_as_passenger)
        query_as_passenger_data = query_as_passenger_result.fetchall()

        data_as_passenger = tuple([i[0] for i in query_as_passenger_data])

        query_get_travels = select(travels).where((travels.c.id.in_(data_as_passenger)) | (travels.c.driver_id == user_id))
        query_get_travels_result = await session.execute(query_get_travels)
        query_get_travels_data = query_get_travels_result.fetchall()

        result_data_dict = {"trips": []}
        for travel in query_get_travels_data:
            result_data_dict["trips"].append(
                {
                    "trip_driver_id": travel[1],
                    "trip_id": travel[0],
                    "trip_origin": travel[2],
                    "trip_destination": travel[3],
                    "trip_date": travel[4],
                    "trip_car_id": travel[5],
                    "trip_status": trip_status_translator[travel[6]],
                    "trip_passenger_qty": travel[8],
                    "trip_cost": travel[9]
                }
            )

    except Exception as e:
        await internal_server_error(e)

    if len(result_data_dict["trips"]) == 0:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail={
                "satusCode": status.HTTP_404_NOT_FOUND,
                "status": "NOT_FOUND",
                "success": False,
                "message": "У вас нет никаких поездок!",
                "data": None
            }
        )

    return {
        "satusCode": status.HTTP_200_OK,
        "status": "OK",
        "success": True,
        "message": f"Data was exported successfully!",
        "data": result_data_dict
    }

@router.post("/search")
async def find_matching_trips(lookup_trip_data: FindTrip, session: AsyncSession = Depends(get_async_session)):
    trip_data = lookup_trip_data.model_dump()
    try:
        query = select(travels).where(
            (travels.c.travel_origin == trip_data["origin"]) &
            (travels.c.travel_destination == trip_data["destination"]) &
            (extract("year", travels.c.departure_date) == trip_data["date_time"].year) &
            (extract("month", travels.c.departure_date) == trip_data["date_time"].month) &
            (extract("day", travels.c.departure_date) == trip_data["date_time"].day) &
            ((travels.c.passenger_capacity - travels.c.passenger_actual) > 0)
        )

        query_result = await session.execute(query)
        query_data = query_result.fetchall()

        found_trips = {
            "trips": []
        }

        for trip in query_data:
            found_trips["trips"].append(
                {
                    "trip_driver_id": trip[1],
                    "trip_id": trip[0],
                    "trip_origin": trip[2],
                    "trip_destination": trip[3],
                    "trip_date": trip[4],
                    "trip_car_id": trip[5],
                    "trip_status": trip_status_translator[trip[6]],
                    "trip_passenger_qty": trip[8],
                    "trip_passenger_seats_left": trip[7] - trip[8],
                    "trip_cost": trip[9]
                }
            )
    except Exception as e:
        await internal_server_error(e)

    if len(found_trips["trips"]) == 0:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail={
                "satusCode": status.HTTP_404_NOT_FOUND,
                "status": "NOT_FOUND",
                "success": False,
                "message": "Поездок по маршруту не найдено!",
                "data": None
            }
        )

    return found_trips



# TODO 1: async def trip_edit()
# TODO 2: async def trip_delete()
