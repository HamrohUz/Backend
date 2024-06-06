from sqlalchemy import Table, Column, BigInteger, Integer, String, ForeignKey, TIMESTAMP, BOOLEAN, FLOAT, MetaData

metadata = MetaData()

users = Table(
    "users",
    metadata,    
    Column("id", BigInteger, primary_key=True),
    Column("first_name", String),
    Column("last_name", String),
    Column("middle_name", String),
    Column("gender", BOOLEAN, nullable=False),
    Column("date_of_birth", TIMESTAMP, nullable=False),
    Column("email", String, nullable=False),
    Column("phone_number", String, nullable=False),
    Column("user_photo", String),
    Column("about_myself", String),
    Column("is_driver", BOOLEAN, default=False),
    Column("salt", String),
    Column("password_hash", String),
    Column("temp_token", String),
    Column("token_valid_till", TIMESTAMP)
)

documents = Table(
    "documents",
    metadata,
    Column("id", BigInteger, primary_key=True),
    Column("user_id", BigInteger, ForeignKey("users.id")),
    Column("driver_license_photo", String, nullable=False),
    Column("driver_license_type", String, nullable=False),
    Column("driver_license_number", String, nullable=False),
    Column("driver_license_issue_date", TIMESTAMP, nullable=False),
    Column("driver_license_valid_till", TIMESTAMP, nullable=False),
    Column("passport_photo", String, nullable=False),
    Column("passport_number", String, nullable=False),
    Column("verification_status", String, default="Not verified")
)


cars = Table(
    "cars",
    metadata,
    Column("id", BigInteger, primary_key=True),
    Column("plate_number", String, nullable=False),
    Column("car_model", String, nullable=False),
    Column("car_body", String, nullable=False),
    Column("car_color", String, nullable=False),
    Column("car_mileage", Integer, nullable=False),
    Column("car_year", Integer, nullable=False),
    Column("car_photo", String, nullable=False)
)


drivers = Table(
    "drivers",
    metadata,
    Column("user_id", BigInteger, ForeignKey("users.id")),
    Column("car_id", BigInteger, ForeignKey("cars.id"))
)

travels = Table(
    "travels",
    metadata,
    Column("id", BigInteger, primary_key=True),
    Column("driver_id", BigInteger, ForeignKey("users.id")),
    Column("travel_origin", String, nullable=False),
    Column("travel_destination", String, nullable=False),
    Column("departure_date", TIMESTAMP, nullable=False),
    Column("car_id", BigInteger, ForeignKey("cars.id")),
    Column("travel_status", String, default="active"),
    Column("passenger_capacity", Integer, nullable=False),
    Column("travel_cost", Integer, nullable=False)
)

travel_routes = Table(
    "travel_routes",
    metadata,
    Column("id", BigInteger, primary_key=True),
    Column("travel_id", BigInteger, ForeignKey("travels.id")),
    Column("route_address", String, nullable=False),
    Column("priority", Integer, nullable=False)
)

passengers = Table(
    "passengers",
    metadata,
    Column("travel_id", BigInteger, ForeignKey("travels.id")),
    Column("passenger_id", BigInteger, ForeignKey("users.id")),
    Column("passenger_origin_id", BigInteger, ForeignKey("travel_routes.id")),
    Column("passenger_destination_id", BigInteger, ForeignKey("travel_routes.id")),
    Column("passenger_status", String, default="registered for travel")
)

travel_applications = Table(
    "travel_applications",
    metadata,
    Column("id", BigInteger, primary_key=True),
    Column("travel_id", BigInteger, ForeignKey("travels.id")),
    Column("user_id", BigInteger, ForeignKey("users.id")),
    Column("travel_origin_id", BigInteger, ForeignKey("travel_routes.id")),
    Column("travel_destination_id", BigInteger, ForeignKey("travel_routes.id")),
    Column("application_status", String, default="applied")
)

travel_progress = Table(
    "travel_progress",
    metadata,
    Column("travel_id", BigInteger, ForeignKey("travels.id")),
    Column("start_time", TIMESTAMP),
    Column("finish_time", TIMESTAMP),
    Column("progress_status", String)
)

rating = Table(
    "rating",
    metadata,
    Column("user_id", ForeignKey("users.id")),
    Column("driver_rating", FLOAT, default=0),
    Column("driver_rating_qty", Integer, default=0),
    Column("passenger_rating", FLOAT, default=0),
    Column("passenger_rating_qty", Integer, default=0)
)
