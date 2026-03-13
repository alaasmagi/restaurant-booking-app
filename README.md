# restaurant-booking-app

## Short description
* UI language: English
* Development year: **2026**
* Languages and technologies: **Backend: Spring Boot, Java, JPA, SQLite & Frontend: Vue.js, TypeScript**

## How to run - the easy way
### Prerequisites

* Docker
* Moder web browser

The project includes a root-level `docker-compose.yml` file which starts both the backend and the frontend together.

Optional environment variables can be provided in your shell or in a root `.env` file before starting Docker:
```bash
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin-password
API_PORT=8080
FRONTEND_PORT=5173
FRONTEND_URL=http://localhost:5173
```

If you do not provide them, Docker Compose uses the default values shown above.

### Running the app

To build and start the full application stack, run from the project root:
```bash
docker compose up --build
```

After startup:
* frontend is available at `http://localhost:5173`
* backend is available at `http://localhost:8080`

The SQLite database is stored in a Docker volume called `api-data`, so backend data persists between container restarts.

To stop the containers:
```bash
docker compose down
```

## How to run - the normal way
### Prerequisites

* Java SDK 25 LTS
* Node.js 
* Modern web browser

Backend should have .env file in the backend root folder `/restaurant-booking-api` which has following content:
```bash
ADMIN_USERNAME=<your-chosen-username>
ADMIN_PASSWORD=<your-chosen-password>
SERVER_PORT=<your-server-port>
FRONTEND_URL=<your-frontend-url>
```
The example has been provided in `/restaurant-booking-api/.env.example`

Frontend should also have .env file in the frontend root folder `/restaurant-booking-client` which has following content:
```bash
FRONTEND_PORT=<your-frontend-port>
VITE_API_URL=<your-server-url>/api
```
The example has been provided in `/restaurant-booking-client/.env.example`

8080 is the default port on which the backend runs. 5173 is the default port on which the frontend runs.

### Running the app

After meeting all prerequisites above - 
* backend can be run via terminal/cmd open in the `/restaurant-booking-api` folder by executing command:  
```bash
./gradlew bootRun
```
* frontend can be run via terminal/cmd open in the `/restaurant-booking-client` folder by executing command:  
```bash
npm i; npm run dev 
```

## Structure

### Data model

<img width="610" height="360" alt="image" src="https://github.com/user-attachments/assets/93e5ea14-3f9b-4fb7-a484-101b1bbc2918" />

* **Many-to-one relatiosnhip**- multiple bookings can be linked to one table (assuming that bookings' timestamps do not match or overlap).

### Backend structure

```
restaurant_booking_api
    ├── RestaurantBookingApiApplication.java
    ├── application
    │   ├── BookingService.java
    │   ├── TableService.java
    │   ├── contracts
    │   │   ├── IBookingRepository.java
    │   │   ├── IRepository.java
    │   │   └── ITableRepository.java
    │   ├── dtos
    │   │   ├── BookingDto.java
    │   │   ├── CreateBookingDto.java
    │   │   ├── PositionDto.java
    │   │   ├── TableDto.java
    │   │   └── VerifyPasswordDto.java
    │   ├── exceptions
    │   │   ├── ApiException.java
    │   │   ├── ConflictException.java
    │   │   ├── NotFoundException.java
    │   │   └── ValidationException.java
    │   └── mappers
    │       ├── BookingMapper.java
    │       └── TableMapper.java
    ├── domain
    │   ├── BaseEntity.java
    │   ├── BookingEntity.java
    │   ├── TableEntity.java
    │   └── enums
    │       ├── EBookingStatus.java
    │       └── ESeatFeature.java
    └── infrastructure
        ├── config
        │   └── SecurityConfig.java
        ├── initialization
        │   └── DbInitializerService.java
        ├── repository
        │   ├── BookingJpaRepository.java
        │   ├── BookingRepository.java
        │   ├── TableJpaRepository.java
        │   └── TableRepository.java
        └── web_api
            ├── ApiErrorResponse.java
            ├── GlobalExceptionHandler.java
            └── controllers
```

#### Domain layer

* **BaseEntity:**

```java
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String createdBy = "system";
    private LocalDateTime updatedAt = LocalDateTime.now();
    private String updatedBy = "system";
    private boolean deleted = false;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
```
  
* **BookingEntity:**

```java
@Getter
@Setter
@Entity
public class BookingEntity extends BaseEntity {
    private UUID tableId;
    @Enumerated(EnumType.STRING)
    private EBookingStatus status;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private int peopleCount;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<ESeatFeature> preferences;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public void activate() {
        this.status = EBookingStatus.ACTIVE;
    }

    public boolean canBeCancelled() {
        return status != EBookingStatus.CANCELLED;
    }

    public void cancel() {
        this.status = EBookingStatus.CANCELLED;
    }

    public boolean overlaps(LocalDateTime startTime, LocalDateTime endTime) {
        return this.startTime != null
                && this.endTime != null
                && startTime != null
                && endTime != null
                && this.startTime.isBefore(endTime)
                && this.endTime.isAfter(startTime);
    }
}
```

* **TableEntity:**

```java
@Getter
@Setter
@Entity
public class TableEntity extends BaseEntity {
    private int seats;
    private String zone;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<ESeatFeature> features;
    private int x;
    private int y;

    public boolean canAccommodate(int partySize) {
        return partySize > 0 && partySize <= seats;
    }
}
```
* **EBookingStatus:**

```java
public enum EBookingStatus {
    ACTIVE,
    CANCELLED
}
```

* **ESeatFeature:**

```java
public enum ESeatFeature {
    WINDOW,
    PRIVATE,
    KIDS_CORNER,
    ACCESSIBLE,
    OUTDOOR,
    BAR_SEATING
}
```

#### Service layer
* **BookingService** - Responsible for validating external input, fetching data via IBookingRepository, mapping data into Data Transfer Objects(DTOs) via BookingMapper. 
* **TableService** - Responsible for validating external input, fetching data via ITableRepository, mapping data into Data Transfer Objects(DTOs) via TableMapper. 
* **Contracts** - IRepository, IBookingRepository, ITableRepository
* **DTOs** - BookingDto, CreateBookingDto, PositionDto, TableDto, VerifyPasswordDto
* **Exceptions** - ApiException, ConflictException, NotFoundException, ValidationException
* **Mappers** - BookingMapper, TableMapper

#### Infrastructure layer
* **Config** - Has SecurityConfig class which is responsible for applying Spring Security on required API endpoints and setting up CORS policy.
* **Initialization** - Has `DbInitializerService` which directly inserts example data into database as the application launches.
* **Repository** - Consists of repository that are responsible for database data retrieval and modification.
* **Web API** - Consists of controllers with endpoints that are used in communication with frontend.

#### Endpoints
* **GET** - `/api/bookings/{ID}`: Fetches booking by its ID.
* **POST** - `/api/bookings`: Creates the booking.
* **PATCH** - `/api/bookings/{ID}/cancel`: Cancels the booking (NB! Sets booking status to "CANCELLED", does not delete it from DB).

* **GET** - `api/tables?startTime=....&endTime=....`: Gets all tables and their availability statuses between certain times. Query parameters are optional, if no parameters are provided, it fetches returns tables with current availability status.
* **PATCH** - `/api/tables/{ID}/position`: Changes the position of a table, uses basic auth - requires username and password. 

* **POST** - `/api/auth/verify`: Checks if user's provided username and password are correct.

### Frontend structure
```
src
  ├── App.vue
  ├── api
  │   └── index.ts
  ├── components
  │   ├── BookingForm.vue
  │   ├── FilterPanel.vue
  │   ├── FloorPlan.vue
  │   └── TableCard.vue
  ├── main.ts
  ├── router
  │   └── index.ts
  ├── types
  │   └── index.ts
  ├── utils
  │   ├── recommendation.ts
  │   └── visual.ts
  └── views
      ├── AdminView.vue
      ├── BookingDetailView.vue
      └── BookingView.vue
```

* **Api** - Uses Axios methods to fetch necessary data from backend.
* **Router** - Handles navigation between views.
* **Types** - Consists of DTOs and data models for internal use.
* **Utils** - Consists of table recommendation logic and visual numeric values calculation logic for FloorPlan and TableCard components.

#### Components
* **BookingForm** - Visual and functional component for obtaining customers' name, email, telephone number and final number of guests.
* **FilterPanel** - Visual and functional component for choosing filters for tables.
* **FloorPlan** - Visual component to represent the canvas for table positioning.
* **TableCard** - Visual component for table itself.

#### Views
* **AdminView** - View for admins to change positions of tables on canvas.
* **BookingDetailView** - View for users to represent the confirmation of successful booking.
* **BookingView** - View for users to choose suitable table and make the booking.

#### Table recommendation logic

```typescript
export function scoreTable(
  table: TableDto,
  peopleCount: number,
  desiredFeatures: SeatFeature[],
): number {
  const seatScore = 1 / (1 + (table.seats - peopleCount))
  const matchCount = desiredFeatures.filter((f) => table.features.includes(f)).length
  const featureScore = desiredFeatures.length > 0 ? matchCount / desiredFeatures.length : 1
  return seatScore + featureScore
}

export function recommendTables(
  tables: TableDto[],
  peopleCount: number,
  desiredFeatures: SeatFeature[],
  topN = 1,
): TableDto[] {
  return tables
    .filter((t) => t.available && t.seats >= peopleCount)
    .map((t) => ({ table: t, score: scoreTable(t, peopleCount, desiredFeatures) }))
    .sort((a, b) => b.score - a.score)
    .slice(0, topN)
    .map((s) => s.table)
}
```

This algorithm chooses scoring system which is well-known and simple heuristic approach. At first it filters out all unavailable tables and tables which do not have required number of seats. Then it computes the score for each available and initially suitable table (by seat numbers): it tries to keep the number of extra seats down (there is no point to recommend 4-seat table for 2-seat booking if there is a suitable 2-seat table (by selected features) available already) by giving higher scores to these tables which have less extra seats, then it counts the matches, normalizes the total number of matches and adds it to the seat score. Eventually the table with higher score wins and gets recommendation.

## Design choices

### Database  
I went for a simple approach by keeping data offline using SQLite. I tried to make database as minimal as possible by keeping things uncomplicated and the number of entities minimal. For IDs I went for classic integer approach because database operations with integers are faster and more optimal than with UUID or GUID. For seat class I like to use enums, because the values are set strongly and there is less chance of a mistake to occur. Each flight has a fligh-specific fee and seat has an extra fee. Extra fee depends on the class of the seat. And the total price of the journey is the sum of flight fee and seat(s') fee(s).

### Backend
Even though it's my first time writing Java and Spring Boot web app, I took the challenge and found that there is some similarities between Spring Boot and my current tech stack involving mostly the world of .NET. Java has JPAs, which were useful, so I used them and it made communication with DB a lot easier (almost as easy as with .NET Entity Framework). The overall design approach (controllers, DB entities, services) is the same I use always for my web applications. It keeps things untangled and relatively easy.

### Frontend
I have been searching for opportunity to use a new frontend framework called Svelte, which is currently not so popular, but it's simplicty is making it gain more popularity over time. I thought that this was the best opportunity to find out if that framework is any good. And I quite enjoyed using it. I like keeping business logic, data fetches from the backend and pages separate, so this is what I just did there.

## Features


## Testing

### Unit tests
### Manual API tests (via Bruno)

## Visuals

## Improvements & scaling possibilities

### The use of Tailwind CSS
* I was not able to get Tailwind CSS and as I did not have that much time, I started writing my own CSS instead.

### Error handling
* Error handling could be better with detailed error coding system and translation files that translate them to different languages.


### Fetching data from one of real flight company APIs
* Application could be made to fetch data from real world flight company API to get real world flight data making the UX more realistic.
### Mobile Application
* Separate mobile application could be made with React Native or Flutter to make UX better on mobile interfaces.

## Assistance used during this project
### AI assistance
### Other assistance
* [YouTube video](https://www.youtube.com/watch?v=O_XL9oQ1_To&list=WL&index=3&t=620s)- to learn the main differences and approaches of building good Java and Spring Boot fullstack web application.
* [Google Gemini](https://gemini.google.com/)- it helped me hustling with date and time formats.

## Personal opinion
Although this is my first project with Java and Spring Boot, it was easy to adapt form .NET to Spring Boot. The most annoying things were getters and setters, in C# we don't write them out as we do in Java. Svelte impressed me because it made it easy to set up the frontend for this webapp. Routing was far easier than with Svelte's main rivals- React, Vue.js and Angular. I think that the project turned out relatively well and this is one of those that can be added to GitHub with pride. This project just proves my point even more - **You don't have to learn all of the languages and frameworks. Being very good in one of the frameworks will get You further than being moderate in all of them.**

