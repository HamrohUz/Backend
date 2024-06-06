from fastapi import APIRouter, Depends, HTTPException, status

from travels.src.database import get_async_session
from travels.src.models import travels, travel_progress, travel_routes
from travels.src.passenger_application_app.schemas import PassengerApplyForTravel

from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import insert

from travels.src.travel_create_app.utils import trip_create_error_check
from travels.src.travel_create_app.schemas import CreateNewTrip
from travels.src.travel_create_app.utils import internal_server_error

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
            travel_id=travel_id, progress_status=trip_data["travel_status"]
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
        "data": None
    }

# TODO 1: async def trip_edit()
# TODO 2: async def trip_delete()
