# Tasks

## Part 1: Project Setup, Planning, and Wireframing

- [X] Create a team name.
  - [X] DNA: Developers Need Arrays

- [x] Setup Github project repository for FavoSphere application (0.17 hrs)
- [x] Add team members as read/write collaborators. (0.03 hrs)
- [x] Add sample markdown files from cohort-56 repo (0.08 hrs)
- [x] Add README.md for tasklist (0.03 hrs)
- [x] Add `BrityHemming` and `scertian` as read/write collaborators to repo. (0.03 hrs)
- [x] Edit .gitignore and file management setup (0.17 hrs)
- [x] Detail discussion on application purpose and user flow. (0.17 hrs)
- [x] Detail discussion on application MVP, features, and stretch goals by milestone/release version. (0.17 hrs)
- [x] Discussion on roles- vs permissions-based access for database design. (0.17 hrs)

- [x] Construct a user story (0.33 hrs)
  - As a casual user, I would like to create an account so that I will be able to save my media favorites to the service in the future.
  - As an authenticated user, I would like to be able to save different forms of media (videos, articles, blog posts, etc…) that I find on the internet so that I can reference them later in an organized manner.
  - As an authenticated user, I would like to be able to view all of the favorites I have saved and edit and delete any such favorite.
  - As an admin, in addition to all of the capabilities of an authenticated user, I would also like to be able to view all users’ favorites so I can scan for any nefarious links a user may post to there; as such, I would like to be able to edit and delete all users’ favorites
  - Stretch Goal: As a group user, I will be able to view the favorites of any other user within my group; I will only be able to add, edit, and delete for myself and/or to the group’s page

- [x] Create a database schema diagram (2 hrs)
  - [x] Upload schema/ERD diagram to Github

- [X] Construct a wireframe
  - [X] Display Home/Gallery Screen (0.75 hrs)
  - [X] Add a basic user flow (0.75 hrs)
  - [X] Favoriting a favorite user flow (0.75 hrs)
  - [X] Editing a favorite user flow (0.75 hrs)
  - [X] Searching a favorite user flow (0.75 hrs)
  - [X] Sharing a favorite user flow (0.75 hrs)

### Package/Class Overview

```text

src
├───main
    ├───java
       └───learn
           └───favorite
               │      App.java
               │      AppConfig.java
               │
               ├───controllers
               │       AuthController.java
               │       EmailController.java
               │       FavoriteController.java
               │       GlobalExceptionHandler.java
               |       TestHelpers.java
               │
               ├───data
                    └───mappers
                        │    AppUserMapper.java
                        │    FavoriteMapper.java
                        │    EmailMapper.java
                        |
               │       AppUserJdbcTemplateRepository.java
               │       AppUserRepostiroy.java (Interface)
               |       EmailJdbcTemplateRepository.java
               |       EmailRepository.java (Interface)
               │       FavoriteJdbcTemplateRepository.java
               │       FavoriteRepository.java (Interface)
               │
               ├───domain
               |       EmailService.java
               │       FavoriteResult.java
               │       FavoriteService.java
               │       Result.java
               │       ResultType.java (Enum)
               │       Validations.java
               │
               ├───models
               |       Email.java
               |       Favorite.java
               |
               ├───security
               |       AppUser.java
               |       AppUserService.java
               |       Credentials.java
               |       JwtConverter.java
               |       JwtRequestFilter.java
               |       SecurityConfig.java
               |
    └───reources
        |       application.properties
└───test
    └───java
        └───learn
            └───favorite
                ├───controllers
                │       FavoriteControllerTest.java
                │
                ├───data
                |        AppUserJdbcTemplateRepositoryTest.
                |        FavoriteJdbcTemplateRepositoryTest.
                |
                ├───domain
                |        HostServiceTest.java
                |
    └───reources
        |       application.properties
```

### App

- `public static void main(String[])` -- Instantiates all required classes with valid arguments, dependency injection annotation

### AppConfig

- `public PasswordEncoder getEncoder()` -- Returns a new BCryptPasswordEncoder

### controllers.AuthController

- `public ResponseEntity<Map<String, String>> authenticate(@RequestBody Map<String, String> credentials)` -- Checks for a valid user
- `public ResponseEntity<Map<String, String>> refreshToken(@AuthenticationPrincipal AppUser appUser)` -- Refreshes the JWT Token
- `public ResponseEntity<?> createAccount(@RequestBody Map<String, String> credentials)` -- Accepts a username and email from user to create an account

### controllers.EmailController

### controllers.FavoriteController

- `public List<Favorite> findAll()` -- Finds all of the existing Favorites
- `public ResponseEntity<Favorite> findById(@PathVariable int favoriteId)` -- Finds a particular Favorite by its ID
- `public ResponseEntity<Favorite> add(@RequestBody Favorite favorite)` -- Adds a new Favorite
- `public ResponseEntity<Void> update(@PathVariable int favoriteId, @RequestBody Favorite favorite)` -- Edits an existing Favorite
- `public ResponseEntity<Void> delete(@PathVariable int favoriteId)` -- Upon Confirmation, Deletes an existing Favorite
- `private HttpStatus getStatus(Result<Favorite> result, HttpStatus statusDefault)` -- Returns a particular HttpStatus depending on the result.getStatus() value

### controllers.GlobalExceptionHandler

- `public ResponseEntity<?> handleException(DuplicateKeyException ex)` -- Checks for a duplicate entry
- `public ResponseEntity<?> handleException(HttpMessageNotReadableException ex)` -- Checks for a JSON error
- `public ResponseEntity<?> handleException(Exception ex)` -- Generic error ("Something went wrong")
- `private ResponseEntity<?> reportException(String message)` -- Displays a list of the errors

### controllers.TestHelpers

- `public static String serializeObjectToJson(Object o) throws JsonProcessingException`

### data.mappers.AppUserMapper

- `public AppUser mapRow(ResultSet rs, int i) throws SQLException` -- Vacillates between IntelliJ and MySQL Workbench

### data.mappers.FavoriteMapper

- `public Favorite mapRow(ResultSet resultSet, int i) throws SQLException` -- Vacillates between IntelliJ and MySQL Workbench

### data.mappers.EmailMapper

- `public Email mapRow(ResultSet rs, int rowNum) throws SQLException` -- Vacillates between IntelliJ and MySQL Workbench

### data.AppUserJdbcTemplateRepository

- `public AppUser findByUsername(String username)` -- Searches for a user by their username
- `public AppUser create(AppUser user)` -- Creates a new user from the credentials passed to it
- `public boolean update(AppUser user)` -- Updates an existing user
- `private void updateRoles(AppUser user)` -- Updates the roles that an existing user has
- `private List<String> getRolesByUsername(String username)` -- Returns a list of all the roles that a given username has

### data.AppUserRepository

- `AppUser findByUsername(String username)`
- `AppUser create(AppUser user)`

- `boolean update(AppUser user)`

### data.EmailJdbcTemplateRepository

- `List<Email> findAll()` -- Finds all the emails

- `Email findById(BigInteger emailId)` -- Finds one particular email

- `List<Email> findByUserId(BigInteger emailId)` -- Finds a user by Id

- `Email create(Email email)` -- Creates an email from email

- `boolean deleteById(BigInteger emailId)` -- Deletes an email

### data.EmailRepository

- `List<Email> findAll()`

- `Email findById(BigInteger emailId)`

- `List<Email> findByUserId(BigInteger emailId)`

- `Email create(Email email)`

- `boolean deleteById(BigInteger emailId)`

### data.FavoriteJdbcTemplateRepository

- `public List<Favorite> findAll()` -- Displays all of the favorites
- `public Favorite findById(int favoriteId)` -- Finds a particular Favorite by its ID
- `public Favorite add(Favorite favorite)` -- Adds a new Favorite
- `public boolean update(Favorite favorite)` -- Updates an existing Favorite
- `public boolean deleteById(int favoriteId)` -- Upon Confirmation, Deletes an existing Favorite

### data.FavoriteRepository

- `List<Favorite> findAll()`
- `Favorite findById(int favoriteId)`
- `Favorite add(Favorite favorite)`
- `boolean update(Favorite favorite)`
- `boolean deleteById(int favoriteId)`

### domain.FavoriteResult

- `public void addMessage(String message)`
- `public void addMessage(String message, ResultType resultType)`
- `public boolean isSuccess()`

### domain.FavoriteService

- `public List<Favorite> findAll()` -- Displays all of the Favorites
- `public Favorite findById(int favoriteId)` -- Finds a particular Favorite based on its ID
- `public Result<Favorite> add(Favorite favorite)` -- Adds a new Favorite
- `public Result<Favorite> update(Favorite favorite)` -- Updates an existing Favorite
- `public Result<Favorite> deleteById(int favoriteId)` -- Upon Confirmation, Deletes an existing Favorite
- `private Result<Favorite> validate(Favorite favorite)` -- Ensures that all of the required inputs are filled in, no incorrect information is inputted, and no duplicates are created

### domain.Result

- `private ActionStatus status`
- `private ArrayList<String> messages`
- `private T payload`
- `public boolean isSuccess()` -- Success if the messages field length is 0; failure otherwise

### domain.ResultType

- Enum for SUCCESS, INVALID, DUPLICATE, NOT_FOUND

### domain.Validations

- `public static boolean isValidUrl(String input)` -- Checks for a valid URL

### models.Favorite

- `private BigInteger favoriteId`
- `private BigInteger userId`
- `private String url`
- `private String source`
- `private String creator`
- `private String title`
- `private String description`
- `private String gifUrl`
- `private String imageUrl`
- `private LocalDate createdOn`
- `private LocalDate updatedOn`
- `private boolean isCustomTitle`
- `private boolean isCustomDescription`
- `private boolean isCustomImage`
- `private boolean isCustomGif`
- Empty Constructor and "All of the Above" Constructor
- Full Getters and Setters
- Override equals and hashcode

### security.AppUser

- `private BigInteger appUserId`
- `private final String username`
- `private final String password`
- `private boolean enabled`
- `private final Collection<GrantedAuthority> authorities`
- `public AppUser(int appUserId, String username, String password, boolean enabled, List<String> roles)`
- `private static Collection<GrantedAuthority> convertRolesToAuthorities(List<String> roles)`
- `public Collection<GrantedAuthority> getAuthorities()`
`Getters and setters for all fields`

### security.AppUserService

- `private final AppUserRepository repository`
- `private final PasswordEncoder encoder`
- `public AppUserService(AppUserRepository repository, PasswordEncoder encoder)`
- `public UserDetails loadUserByUsername(String username)`
- `public Result<AppUser> create(Credentials credentials)`
- `private Result<AppUser> validate(Credentials credentials)`
- `private boolean isValidPassword(String password)`

### security.Credentials

- `private String email`
- `private String password`
- `public String getEmail()`
- `public void setEmail(String email)`
- `public String getPassword()`
- `public void setPassword(String password)`

### security.JwtConverter

- `private Key key`
- `private final String ISSUER`
- `private final int EXPIRATION_MINUTES`
- `private final int EXPIRATION_MILLIS`
- `public String getTokenFromUser(AppUser user)`
- `public AppUser getUserFromToken(String token)`

### security.JwtRequestFilter

- `private final JwtConverter converter;`
- `public JwtRequestFilter(AuthenticationManager authenticationManager, JwtConverter converter)` -- constructor
- `protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)`

### security.SecurityConfig

- `private final JwtConverter jwtConverter;`
- `public SecurityConfig(JwtConverter jwtConverter)` -- constructor
- `public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authConfig)`
- `public AuthenticationManager authenticationManager(AuthenticationConfiguration config)`

### Part 2: Backend

- [X] Set-up project backend (Maven project; packages & classes) (0.17 hrs)
- [X] Write `App.java` (0.17 hrs)
- [X] Write `AppConfig` (0.17 hrs)
- [X] Write `AuthController` (0.25 hrs)
- [X] Write `EmailController` (4 hrs)
- [X] Write `FavoriteController` (1.5 hrs)
- [X] Write `GlobalExceptionHandler` (0.25 hrs)
- [X] Write `TestHelpers` (0.1 hrs)
- [X] Write `AppUsermapper` (0.25 hrs)
- [X] Write `EmailMapper` (0.5 hrs)
- [X] Write `FavoriteMapper` (0.25 hrs)
- [X] Write `AppUserJdbcTemplateRepository` (0.5 hrs)
- [X] Extract `AppUserRepository` (1 hrs)
- [X] Write `EmailJdbcTemplateRepository` (4 hrs)
- [X] Extract `EmailRepository` (0.5 hrs)
- [X] Write `FavoriteJdbcTemplateRepository` (0.5 hrs)
- [X] Extract `FavoriteRepository` (0.5 hrs)
- [X] Write `AppGmail` (3 hrs)
- [X] Write `EmailService` (1 hr)
- [X] Write `FavoriteResult`  (0.25 hrs)
- [X] Write `FavoriteService` (1.5 hrs)
- [X] Write `Result` (0.33 hrs)
- [X] Write `ResultType` (0.1 hrs)
- [X] Write `Validations` (0.1 hrs)
- [X] Write `Email` (0.5 hrs)
- [X] Write `Favorite` (0.25 hrs)
- [X] Write `Link` (0.5 hrs)
- [X] Write `AppUser` (0.25 hrs)
- [X] Write `AppUserService` (1 hr)
- [X] Write `Credentials` (0.25 hrs)
- [X] Write `JwtConverter` (1 hr)
- [X] Write `JwtRequestFilter` (0.5 hr)
- [X] Write `SecurityConfig` (0.75 hr)

### Part 3: Testing

- [X] Write `FavoriteControllerTest` (4 hrs)
- [X] Write `AppUserJdbcTemplateRepositoryTest` (4 hrs)
- [X] Write `EmailJdbcTemplateRepositoryTest` (1 hr)
- [X] Write `FavoriteJdbcTemplateRepositoryTest` (3 hrs)
- [X] Write `EmailServiceTest` (1 hr)
- [X] Write `FavoriteServiceTest` (3 hrs)
- [X] Write `AppUserServiceTest` (4 hrs)

### Part 4: HTTP Requests

- [X] Create requests.http in http directory (0.17 hrs)
- [X] Retrieve a favorite using a  `GET` request (0.17 hrs)
- [X] Use `POST` to add a favorite (0.25 hrs)
- [X] Use `PUT` to edit a favorite with an ID (0.25 hrs)
- [X] Display a list of all favorites for an authorized user (0.25 hrs)

### Part 5: Frontend

- [X] Create react app and set-up client side (0.17 hrs)
- [X] Remove unnecessary files (0.067 hrs)
- [X] Create an `index.html` and `App.js` file as a starting point for your project (0.25 hrs)
- [X] Add Bootstrap to the `public/index.html` file (0.08 hrs)
- [X] Components:
  - [X] Left Panel (0.5 hrs)
  - [ ] Filter Bar Component (3 hrs)
  - [X] Home Component - Gallery View (3 hrs)
  - [X] Login Page (2 hrs)
  - [X] Favorites Gallery Grid Component (1 hr)
  - [X] Favorites Staging/Inbox Component (1.5 hrs)
  - [X] Edit Favorite Page (1.5 hrs)
  - [X] Stub out click event handlers for the "Add Favorite", "Edit Favorite", and "Delete Favorite" buttons (1 hr)
- [X] Conditionally render sections of the page (1.5 hr)
  - [X] Add a state variable to track the current view
  - [X] Add a method to update the current view and conditionally render the list or the form
  - [X] Call the method to update the current where needed

### Part 6: Finishing Touches

- [X] Choose an appropriate CSS framework for the style desired and implement (0.25 hrs)

  - [X] Style all buttons (0.5 hrs)
  - [X] Style the favorites list (grid) in gallery view (0.75 hrs)
  - [ ] Style the form (0.75 hrs)
  - [X] Add onsubmit event handler to the form element (be sure to prevent the form from submitting!) (0.25 hrs)
  - [X] Create a favorite object (0.25 hrs)
  - [X] Use `fetch` to `POST` the new favorite’s information to the FavoSphere API (0.5 hrs)
  - [X] On success, refresh the favorites list, or on failure, display any validation errors from the API in the UI (1 hr)
- [X] Support deleting favorites
  - [X] Confirm the deletion with the user (0.25 hrs)
  - [X] Use `fetch` to `DELETE` the favorite from the FavoSphere API (0.33 hrs)
  - [X] On success, refresh the favorites list (0.33 hrs)
        **Commit all changes and push to GitHub**

## High-Level Requirements

Implement a full CRUD UI for favorites:

- Display all favorites (user, admin)
- Add a favorite (user, admin)
- Update a favorite (user, admin)
- Delete a favorite (user, admin)
- CRUD for other users (admin)
- Stretch Goal: CRUD for other users within the same group (group user)

## Technical Requirements

- Always use semantically correct markup.
- With the exception of Bootstrap (or another CSS framework) for styles, don't use any libraries or frameworks.
- Use `fetch` for async HTTP.
- Scrapers specific to website links copied to FavoSphere
