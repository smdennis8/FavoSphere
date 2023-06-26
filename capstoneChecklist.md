
# FavoSphere Capstone Application

## Tasks

### Part 1: Project Setup

* [ ] Create a team name.
  * [ ] The Favorites, The Spheres, The Spheres of Influence, The Spherical Cows, The Spherical Earthers, The AtmoSpheres

### Part 2: Planning and Wireframing

* [X] Setup Github project repository for FavoSphere application
* [X] Add team members as read/write collaborators.
* [X] Add sample markdown files from cohort-56 repo
* [X] Add README.md for tasklist
* [X] Add `BrityHemming` and `scertian` as read/write collaborators to repo.

* [X] Detail discussion on .gitignore and file management setup.
* [X] Detail discussion on application purpose and user flow.
* [X] Detail discussion on application MVP, features, and stretch goals by milestone/release version.
* [X] Detail discussion on roles vs permissions based access for database design.

* [X] Construct a user story
As a casual user, I would like to create an account so that I will be able to save my media favorites to the service in the future.
As an authenticated user, I would like to be able to save different forms of media (videos, articles, blog posts, etc…) that I find on the internet so that I can reference them later in an organized manner. 
As an authenticated user, I would like to be able to view all of the favorites I have saved and edit and delete any such favorite.
As an admin, in addition to all of the capabilities of an authenticated user, I would also like to be able to view all users’ favorites so I can scan for any nefarious links a user may post to there; as such, I would like to be able to edit and delete all users’ favorites
Stretch Goal: As a group user, I will be able to view the favorites of any other user within my group; I will only be able to add, edit, and delete for myself and/or to the group’s page

* [X] Create a database schema diagram
  * [X] Upload schema/ERD diagram to Github

* [ ] Construct a wireframe
  * [X] Display Home/Gallery Screen
  * [X] Add a basic user flow
  * [ ] Favoriting a favorite user flow
  * [ ] Editing a favorite user flow
  * [ ] Searching a favorite user flow
  * [ ] Sharing a favorite user flow

### Part 3: Set up the project (Backend)

* [ ] Create packages for controllers, data, domain, models, and security
  * [ ] Within Controllers:
    * [ ] AuthController
    * [ ] FavoriteController
  * [ ] Within Data:
    * [ ] AppUserJdbcTemplateRepository
    * [ ]  AppUserMapper
    * [ ] FavoriteJdbcTemplateRepository
    * [ ] FavoriteMapper
    * [ ] AppUserRepository Interface
    * [ ] FavoriteRepository Interface
  * [ ] Within Domain:
    * [ ] EmailService
    * [ ] ActionStatus Enum
    * [ ] FavoriteService
    * [ ] FavoriteResult
* [ ] Within Models:
  * [ ] AppUser
  * [ ] Favorite
* [ ] Within Security:
  * [ ] AppUserService
  * [ ] JwtConverter
  * [ ] JwtRequestFilter
  * [ ] SecurityConfig

### Part 4: Make requests to the server

* [ ]Create requests.http in http directory
* [ ] Retrieve the favorite to edit
* [ ] Update the form with the favorite’s property values
* [ ] Update the onsubmit event handler to handle both `POST` and `PUT` requests
* [ ] Set the favorite’s ID on the favorite object
* [ ] Use `fetch` to `PUT` the updated favorite’s information to the FavoSphere API
* [ ] On success, refresh the FavoritesList, or on failure, display any validation errors from the API in the UI
* [ ] Display a list of favorites
  * [ ] Use `fetch` to `GET` a list of favorites from the FavoSphere API when the website is first loaded

### Part 5: Set up the project (Frontend)

* [ ] Create an `index.html` and `main.js` file as a starting point for your project
* [ ] Add Bootstrap to the `public/index.html` file
* [ ] Components:
  * [ ] Navigation Panel Wrapper
  * [ ] Filter Bar Component
  * [ ] Home Component
  * [ ] Login Page
  * [ ] Favorites Gallery Grid Component
  * [ ] Favorites Table Component
  * [ ] Favorites Staging/Inbox Component
  * [ ] Edit Favorite Page
  * [ ] Contact Page
  * [ ] Add a link to the Bootstrap CSS using the [CDN from the official docs](https://getbootstrap.com/docs/4.6/getting-started/introduction/#css)
  * [ ] Add the [`container` CSS class](https://getbootstrap.com/docs/4.6/layout/overview/#containers) to the `<div id="root"></div>` element

  * [ ] Use HTML and JavaScript to render the favorites array
  * [ ] Stub out click event handlers for the "Add Favorite", "Edit Favorite", and "Delete Favorite" buttons

* [ ] Conditionally render sections of the page
  * [ ] Add a state variable to track the current view
  * [ ] Add a method to update the current view and conditionally render the list or the form
  * [ ] Call the method to update the current where needed

### Part 6: Finishing Touches

* [ ] Apply Bootstrap styling
  * [ ] Style all buttons
  * [ ] Style the favoriteslist
  * [ ] Style the form

  * [ ] Add onsubmit event handler to the form element (be sure to prevent the form from submitting!)
  * [ ] Create a favorite object
  * [ ] Use `fetch` to `POST` the new favorite’s information to the FavoSphere API
  * [ ] On success, refresh the favorites list, or on failure, display any validation errors from the API in the UI

* [ ] Support deleting favorites
  * [ ] Confirm the deletion with the user
  * [ ] Use `fetch` to `DELETE` the favorite from the FavoSphere API
  * [ ] On success, refresh the favorites list
**Commit all changes and push to GitHub**

## High-Level Requirements

Implement a full CRUD UI for favorites.

* Display all favorites (user, admin)
* Add a favorite (user, admin)
* Update a favorite (user, admin)
* Delete a favorite (user, admin)
* CRUD for other users (admin)
* Stretch Goal: CRUD for other users within the same group (group user)

## Technical Requirements

* Always use semantically correct markup.
* With the exception of Bootstrap (or another CSS framework) for styles, don't use any libraries or frameworks.
* Use `fetch` for async HTTP.
* Scrapers specific to website links copied to FavoSphere