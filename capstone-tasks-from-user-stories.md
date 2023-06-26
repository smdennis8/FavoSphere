# Creating Tasks From User Stories

Once you have user stories at a sufficiently granular level, use them to generate concrete tasks. Walk through the questions below. Not all will apply, but they will get you thinking about what is required. Questions are intended to be exhaustive, but, of course, they're not. Consider other technical and non-technical tasks that may arise from a user story.

## For each user story

1. Which database tables and relationships are required? If you're using a non-relational data store as a stretch goal, how is data represented in that stretch goal?

2. Are you designing your database at a higher level, not per user story? If so:

    - Create a task for the diagram. How long will it take?
    - Create a task from the DDL. How long will that take?
    - Create a task for populating test data via DML. Estimate it.
    - If your test database is different than your production database (maybe establishing known good state), create a task for that DML. Estimate it.

3. Which repository classes are required? Which repository methods are required? Do the repositories use interfaces?

    - Create discrete tasks, not a single task for _everything_. I prefer a task per repository method, estimated to include private implementation details and both the interface and class contracts.
    - Create a task for the class diagram, when applicable. If you do the class diagram once, it only needs one task. If you're breaking up the diagram per logical layer, consider a task for each sub-diagram.

4. Which repository tests are required? How do we establish known good state?

    - Create a task that details which positive and negative test cases you will use.
    - If there are many, consider creating more than one task for different test categories.
    - Create a task for known good state.

5. Which models are required? Do models enforce any sort of domain rules or are they POJOs?

    - Create a task for each model.
    - If models include domain rules (think calculating a reservation total), create a task for those.
    - If models include domain rules, test them. Create tasks for tests.

6. Which service classes and methods are required? Which other domain classes and methods are required? What validation is needed? Can your team use the Validation API? Are there interesting domain transaction requirements?

    - Create a task for result-type classes.
    - Create a task for the user story "feature" methods.
    - Consider creating a task for validation if it's separate from the service method (validation annotations in the model).
    - Consider creating an input validation task (verifying data conforms to some restriction without the need to look elsewhere) and a domain validation task (verifying consistency across several fields, verifying something exists in the database, verifying an object is in the correct state based on complex rules, etc).
    - If domain rules are complex, consider creating a separate task to capture that complexity.

7. Which domain tests are required? Are stubs used? Mocks?

    - Create a task that details which positive and negative test cases you will use.
    - If there are many, consider creating more than one task for different test categories.

8. Which controller classes, methods, and mappings (routes) are required? Does global error handling need to be applied based on this user story?

    - Create a task for each user story feature method.
    - Create a task for each global error handling exception.

9. Since most teams will use Spring Boot for their REST API, think about the Spring Boot plumbing required.

    - Which annotations are required? Does that require a task? (Maybe a very short task so it's not forgotten.)
    - Who sets up the Spring Boot `main`? Does that need a task?
    - Does using `@SpringBootTest` change anything?

10. In React, which components are required? How do components relate? How do they share state?

    - Create a task for component wireframes.
    - Create a task for wireframes at a larger scope (showing more than a single component).
    - Create a task for each component, at a minimum. If components are complex, consider breaking each "scenario" into a task.
    - A React Router task?
    - A task for React service methods? (`fetch`)
    - A context task?
    - If you're planning on testing your component, create a task.
    - Do we need a task for creating the React app and initial set up?
    - Does this user story require auth? If so, does it need a task? (If this is the first time we've encountered auth, then yes.)

## Summary

Use user stories to drive tasks, but don't forgot about larger, more general tasks. Create tasks for absolutely everything. Looking ahead, that might include:

- Creating presentation slides.
- Scripting the presentation.
- Allocating time for new technology prototypes. (Time-box prototypes so that if you can't get something working in X hours, switch to a different new technology.)
- Allocating time to troubleshoot cloud deployment.

Once every task is defined and estimated, total them up. Assuming a ten-day project with 8 hours per person per day (that's 8 hours of real work, most humans "work" longer but don't get 8 hours of productivity), anything more than 240 hours is too much. Reduce scope. If your estimate is significantly less than 240 hours, increase scope.
>