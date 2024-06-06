from fastapi import APIRouter, Depends
from fastapi.exceptions import HTTPException

from src.database import get_async_session
from src.models import travel_applications, travel_routes
from src.passenger_application_app.schemas import PassengerApplyForTravel

from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import insert

from src.passenger_application_app.utils import check_application_for_errors

router = APIRouter(
    prefix="/api/travel-application",
    tags=["Passenger Application For Travel"]
)

@router.post("/passenger-apply-travel")
async def passenger_apply_for_travel(new_application: PassengerApplyForTravel, session: AsyncSession = Depends(get_async_session)):
    await check_application_for_errors(new_application, session)
    try:
        statement = insert(travel_applications).values(**new_application.model_dump())
        await session.execute(statement)
        await session.commit()
        return {"status": "success",
                "data": None,
                "details": "Your application was successfully submitted"}
    except Exception:
        raise HTTPException(status_code=400, detail={
            "status": "error",
            "data": None,
            "details": "Wrong information was sent"
        })


@router.post("/passenger-cancel-application")
async def passenger_cancel_application():
    pass

@router.post("/driver-accept-passenger")
async def driver_accept_passenger():
    pass

@router.post("/driver-decline-passenger")
async def driver_decline_passenger():
    pass