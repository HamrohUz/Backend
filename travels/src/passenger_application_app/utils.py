from fastapi import APIRouter, Depends
from fastapi.exceptions import HTTPException

from src.database import get_async_session
from src.models import travel_applications, travel_routes, travels, users
from src.passenger_application_app.schemas import PassengerApplyForTravel

from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select


async def check_application_for_errors(new_application: PassengerApplyForTravel, session: AsyncSession = Depends(get_async_session)):
    values = new_application.model_dump()
    
    # check if travel exists and status not started
    query = select(travels.c.travel_status).where(travels.c.id == values["travel_id"])
    result = await session.execute(query)
    result_data = result.all()
    if len(result_data) == 0:   
        raise HTTPException(status_code=400, detail={
            "status": "fail",
            "data": None,
            "details": f"travel with id={values['travel_id']} does not exist"
        })
    
    if result_data[0][0] != "not started":
        raise HTTPException(status_code=400, detail={
            "status": "fail",
            "data": None,
            "details": f"Travel with id={values['travel_id']} has status='{result_data[0][0]}', thus does not accept applications anymore."
        })
    
    # check if user exists
    query = select(users).where(users.c.id == values["user_id"])
    result = await session.execute(query)
    
    if len(result.all()) == 0:
        raise HTTPException(status_code=400, detail={
            "status": "fail",
            "data": None,
            "details": f"User with id={values['user_id']} does not exist"
        })

    # check if user already applied
    query = select(travel_applications.c.user_id).where(travel_applications.c.user_id == values["user_id"])
    result = await session.execute(query)
    
    if len(result.all()) == 1:
        raise HTTPException(status_code=400, detail={
            "status": "fail",
            "data": None,
            "details": f"User already applied for this travel before."
        })

    # check if there are any space for application
    query = select(travel_applications.c.id).where(travel_applications.c.travel_id == values["travel_id"]).where(travel_applications.c.application_status == "accepted")
    current_capacity = await session.execute(query)
    query_max_capacity = select(travels.c.passenger_capacity).where(travels.c.id == values["travel_id"])
    max_capacity = await session.execute(query_max_capacity)
    
    if max_capacity.one()[0] - len(current_capacity.all()) == 0:
        raise HTTPException(status_code=400, detail={
            "status": "fail",
            "data": None,
            "details": f"Passenger capacity is already full. More passengers cannot be added."
        })

    # check if travel_origin and travel_destination are in travel_routes and check priorities
    query_routes = select(travel_routes.c.id, travel_routes.c.priority).where(travel_routes.c.travel_id == values["travel_id"])
    routes = await session.execute(query_routes)
    route_values = routes.all()
    if len(route_values) < 2:
        raise HTTPException(status_code=400, detail={
            "status": "fail",
            "data": None,
            "details": f"There is no travel_id={values['travel_id']} that contains route_ids=[{values['travel_origin_id']}, {values['travel_destination_id']}]."
        })
    
    routes_dict = {}
    for route in route_values:
        routes_dict[route[0]] = route[1]
    
    if routes_dict.get(values["travel_origin_id"]) is None or routes_dict.get(values["travel_destination_id"]) is None:
        raise HTTPException(status_code=400, detail={
            "status": "fail",
            "data": {
                "travel_origin_id": "Not found" if routes_dict.get(values["travel_origin_id"]) is None else values["travel_origin_id"],
                "travel_destination_id": "Not found" if routes_dict.get(values["travel_destination_id"]) is None else values["travel_destination_id"]
            },
            "details": f"Wrong route id(s) were intoduced."
        })

    if routes_dict.get(values["travel_origin_id"]) > routes_dict.get(values["travel_destination_id"]):
        raise HTTPException(status_code=400, detail={
            "status": "fail",
            "data": {
                f"travel_origin_id[{values['travel_origin_id']}].priority": routes_dict.get(values["travel_origin_id"]),
                f"travel_destination_id[{values['travel_destination_id']}].priority": routes_dict.get(values["travel_destination_id"])
            },
            "details": f"Driver is travelling in reverse direction. Please check  travel_origin_id and travel_destination_id again."
        })

    
