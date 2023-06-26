# FavoSphere Capstone Application

## Tasks

### Part 1: Project Setup

- [ ] Create a team name.
  - [ ] The Favorites, The Spheres, The Spheres of Influence, The Spherical Cows, The Spherical Earthers, The AtmoSpheres

### Part 2: Planning and Wireframing

- [x] Setup Github project repository for FavoSphere application
- [x] Add team members as read/write collaborators.
- [x] Add sample markdown files from cohort-56 repo
- [x] Add README.md for tasklist
- [x] Add `BrityHemming` and `scertian` as read/write collaborators to repo.

- [x] Detail discussion on .gitignore and file management setup.
- [x] Detail discussion on application purpose and user flow.
- [x] Detail discussion on application MVP, features, and stretch goals by milestone/release version.
- [x] Detail discussion on roles vs permissions based access for database design.

- [x] Construct a user story
      As a casual user, I would like to create an account so that I will be able to save my media favorites to the service in the future.
      As an authenticated user, I would like to be able to save different forms of media (videos, articles, blog posts, etc…) that I find on the internet so that I can reference them later in an organized manner.
      As an authenticated user, I would like to be able to view all of the favorites I have saved and edit and delete any such favorite.
      As an admin, in addition to all of the capabilities of an authenticated user, I would also like to be able to view all users’ favorites so I can scan for any nefarious links a user may post to there; as such, I would like to be able to edit and delete all users’ favorites
      Stretch Goal: As a group user, I will be able to view the favorites of any other user within my group; I will only be able to add, edit, and delete for myself and/or to the group’s page

- [x] Create a database schema diagram

  - [x] Upload schema/ERD diagram to Github

- [ ] Construct a wireframe
  - [x] Display Home/Gallery Screen
  - [x] Add a basic user flow
  - [ ] Favoriting a favorite user flow
  - [ ] Editing a favorite user flow
  - [ ] Searching a favorite user flow
  - [ ] Sharing a favorite user flow

### Part 3: Set up the project (Backend)

## Package/Class Overview

```
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
               │       FavoriteController.java
               │
               ├───data
               │       AppUserJdbcTemplateRepository.java
               │       AppUserMapper.java
               │       AppUserRepostiroy.java (Interface)
               │       FavoriteJdbcTemplateRepository.java
               │       FavoriteMapper.java
               │       FavoriteRepository.java (Interface)
               │
               ├───domain
               │       ActionStatus.java (Enum)
               │       FavoriteService.java
               │       Result.java
               │
               ├───models
               |       AppUser.java
               |       Favorite.java
               |
               ├───security
               |       AppUserService.java
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
                │       AuthControllerTest.java
                │       FavoriteControllerTest.java
                │       TestHelpers.java
                │
                ├───data
                |        AppUserJdbcTemplateRepositoryTest.
                |        FavoriteJdbcTemplateRepositoryTest.
                |
                ├───domain
                |        HostServiceTest.java
                |
                ├───security
                |        AppUserServiceTest.java
                |
    └───reources
        |       application.properties
```

### App

- `public static void main(String[])` -- Instantiates all required classes with valid arguments, dependency injection. run controller

### AppConfig

- `public PasswordEncoder getEncoder()` -- Returns a new BCryptPasswordEncoder

### controllers.AuthController

- `public ResponseEntity<Map<String, String>> authenticate(@RequestBody Map<String, String> credentials)` -- Checks for a valid user

- `public ResponseEntity<Map<String, String>> refreshToken(@AuthenticationPrincipal AppUser appUser)` -- Refreshes the JWT Token

- `public ResponseEntity<?> createAccount(@RequestBody Map<String, String> credentials)` -- Accepts a username and email from user to create an account

### controllers.FavoriteController

- `public List<Favorite> findAll()` -- Finds all of the existing Favorites

- `public ResponseEntity<Favorite> findById(@PathVariable int favoriteId)` -- Finds a particular Favorite by its ID

- `public ResponseEntity<Favorite> add(@RequestBody Favorite favorite)` -- Adds a new Favorite

- `public ResponseEntity<Void> update(@PathVariable int favoriteId, @RequestBody Favorite favorite)` -- Edits an existing Favorite

- `public ResponseEntity<Void> delete(@PathVariable int favoriteId)` -- Upon Confirmation, Deletes an existing Favorite

- `private HttpStatus getStatus(Result<Favorite> result, HttpStatus statusDefault)` -- Returns a particular HttpStatus depending on the result.getStatus() value

### data.AppUserJdbcTemplateRepository

- `public AppUser findByUsername(String username)` -- Searches for a user by their username

- `public AppUser create(AppUser user)` -- Creates a new user from the credentials passed to it

- `public boolean update(AppUser user)` -- Updates an existing user

- `private void updateRoles(AppUser user)` -- Updates the roles that an existing user has

- `private List<String> getRolesByUsername(String username)` -- Returns a list of all the roles that a given username has

### data.AppUserMapper

- `public AppUser mapRow(ResultSet rs, int i) throws SQLException` -- Vasilates between IntelliJ and MySQL Workbench

### data.AppUserRepository

- `AppUser findByUsername(String username)`

- `AppUser create(AppUser user)`

- `boolean update(AppUser user)`

### data.FavoriteJdbcTemplateRepository

- `public List<Favorite> findAll()` -- Displays all of the favorites

- `public Favorite findById(int favoriteId)` -- Finds a particular Favorite by its ID

- `public Favorite add(Favorite favorite)` -- Adds a new Favorite

- `public boolean update(Favorite favorite)` -- Updates an existing Favorite

- `public boolean deleteById(int favoriteId)` -- Upon Confirmation, Deletes an existing Favorite

### data.FavoriteMapper

- `public Favorite mapRow(ResultSet resultSet, int i) throws SQLException` -- Vasilates between IntelliJ and MySQL Workbench

### data.FavoriteRepository

- `List<Favorite> findAll()`

- `Favorite findById(int favoriteId)`

- `Favorite add(Favorite favorite)`

- `boolean update(Favorite favorite)`

- `boolean deleteById(int favoriteId)`

### domain.ActionStatus

- Enum for SUCCESS, INVALID, DUPLICATE, NOT_FOUND

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

### models.Favorite

### security.AppUser
- `private int appUserId`
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
- `private String username`
- `private String password`
- `public String getUsername()`
- `public void setUsername(String username)`
- `public String getPassword()`
- `public void setPassword(String password)`

### security.JwtConverter
- `private Key key` -- 
- `private final String ISSUER`
- `private final int EXPIRATION_MINUTES`
- `private final int EXPIRATION_MILLIS`
- `public String getTokenFromUser(AppUser user)`
- `public AppUser getUserFromToken(String token)`

### security.JwtRequestFilter
- `private final JwtConverter converter;` -- 
- `public JwtRequestFilter(AuthenticationManager authenticationManager, JwtConverter converter)` -- constructor
- `protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)` -- 

### security.SecurityConfig
- `private final JwtConverter jwtConverter;`
- `public SecurityConfig(JwtConverter jwtConverter)` -- constructor
- `public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authConfig)` -- 
- `public AuthenticationManager authenticationManager(AuthenticationConfiguration config)` -- 

### Part 4: Make requests to the server

- [ ]Create requests.http in http directory
- [ ] Retrieve the favorite to edit
- [ ] Update the form with the favorite’s property values
- [ ] Update the onsubmit event handler to handle both `POST` and `PUT` requests
- [ ] Set the favorite’s ID on the favorite object
- [ ] Use `fetch` to `PUT` the updated favorite’s information to the FavoSphere API
- [ ] On success, refresh the FavoritesList, or on failure, display any validation errors from the API in the UI
- [ ] Display a list of favorites
  - [ ] Use `fetch` to `GET` a list of favorites from the FavoSphere API when the website is first loaded

### Part 5: Set up the project (Frontend)

- [ ] Create an `index.html` and `main.js` file as a starting point for your project
- [ ] Add Bootstrap to the `public/index.html` file
- [ ] Components:

  - [ ] Navigation Panel Wrapper
  - [ ] Filter Bar Component
  - [ ] Home Component
  - [ ] Login Page
  - [ ] Favorites Gallery Grid Component
  - [ ] Favorites Table Component
  - [ ] Favorites Staging/Inbox Component
  - [ ] Edit Favorite Page
  - [ ] Contact Page
  - [ ] Add a link to the Bootstrap CSS using the [CDN from the official docs](https://getbootstrap.com/docs/4.6/getting-started/introduction/#css)
  - [ ] Add the [`container` CSS class](https://getbootstrap.com/docs/4.6/layout/overview/#containers) to the `<div id="root"></div>` element

  - [ ] Use HTML and JavaScript to render the favorites array
  - [ ] Stub out click event handlers for the "Add Favorite", "Edit Favorite", and "Delete Favorite" buttons

- [ ] Conditionally render sections of the page
  - [ ] Add a state variable to track the current view
  - [ ] Add a method to update the current view and conditionally render the list or the form
  - [ ] Call the method to update the current where needed

### Part 6: Finishing Touches

- [ ] Apply Bootstrap styling

  - [ ] Style all buttons
  - [ ] Style the favoriteslist
  - [ ] Style the form

  - [ ] Add onsubmit event handler to the form element (be sure to prevent the form from submitting!)
  - [ ] Create a favorite object
  - [ ] Use `fetch` to `POST` the new favorite’s information to the FavoSphere API
  - [ ] On success, refresh the favorites list, or on failure, display any validation errors from the API in the UI

- [ ] Support deleting favorites
  - [ ] Confirm the deletion with the user
  - [ ] Use `fetch` to `DELETE` the favorite from the FavoSphere API
  - [ ] On success, refresh the favorites list
        **Commit all changes and push to GitHub**

## High-Level Requirements

Implement a full CRUD UI for favorites.

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
