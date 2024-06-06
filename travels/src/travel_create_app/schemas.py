from pydantic import BaseModel, Field
from datetime import datetime


class CreateNewTrip(BaseModel):
    driver_id: int = Field(examples=["45367"])
    routes: list = Field(examples=[["Moscow", "Vladimir", "Nizhniy Novgorod"]])
    departure_date: datetime = Field(examples=["2024-06-05T23:39:47"])
    car_id: int = Field(examples=["567"])
    passenger_capacity: int = Field(examples=["3"])
    travel_cost: int = Field(examples=["30000"]) # cost 300.00 is written like 30000


class GetTripInfo(BaseModel):
    user_id: int = Field(examples=["45367"])


class FindTrip(BaseModel):
    origin: str = Field(examples=["Moscow"])
    destination: str = Field(examples=["Nizhniy Novgorod"])
    date_time: datetime = Field(examples=["2024-06-05"])