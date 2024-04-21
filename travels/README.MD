# Travel Application / Registration Service API
This API contains 6 apps that provide functionality for driver and passenger:
1) <b>TravelCreateApp:</b>
    - create_travel
    - edit_travel
    - remove_travel
2) <b>PassengerApplicationApp:</b>
    - passenger_apply_travel
    - passenger_cancel_application
    - driver_accept_passenger
    - driver_decline_passenger
3) <b>TravelProgressApp:</b>
    - passenger_cancel_travel_registration
    - driver_start_travel
    - driver_missing_passenger
    - driver_travel_end
4) <b>RatingApp:</b>
    - passenger_rate_driver
    - driver_rate_passenger
5) <b>NotificationApp:</b>
    - passenger_notify
    - driver_notify
6) <b>TravelDataApp:</b>
    - passenger_travel_history
    - passenger_search_travel
    - passenger_get_travel_info
    - driver_travel_history
    - driver_get_travel_info


## Project Structure
```
travels
├── src
│   ├── travel_create_app
│   │   ├── models.py
│   │   ├── schemas.py
│   │   ├── router.py
│   │   └── utils.py
│   ├── passenger_application_app
│   │   ├── models.py
│   │   ├── schemas.py
│   │   ├── router.py
│   │   └── utils.py
│   ├── travel_progress_app
│   │   ├── models.py
│   │   ├── schemas.py
│   │   ├── router.py
│   │   └── utils.py
│   ├── rating_app
│   │   ├── models.py
│   │   ├── schemas.py
│   │   ├── router.py
│   │   └── utils.py
│   ├── notification_app
│   │   ├── models.py
│   │   ├── schemas.py
│   │   ├── router.py
│   │   └── utils.py
│   ├── travel_data_app
│   │   ├── models.py
│   │   ├── schemas.py
│   │   ├── router.py
│   │   └── utils.py
│   ├── config.py
│   ├── database.py
│   └── main.py
├── tests
│   ├── test_travel_create_app.py
│   ├── test_passenger_application_app.py
│   ├── test_travel_progress_app.py
│   ├── test_rating_app.py
│   ├── notification_app
│   └── travel_data_app
├── requirements.txt
├── .env
├── .gitignore
└── alembic.ini



```