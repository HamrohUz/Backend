from pydantic import BaseModel

class PassengerApplyForTravel(BaseModel):
    travel_id: int
    user_id: int
    travel_origin_id: int
    travel_destination_id: int

class PassengerCancelApplication(BaseModel):
    application_id: int