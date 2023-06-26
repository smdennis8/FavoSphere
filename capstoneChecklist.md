# Capstone Checklist

1. Create a team name.
2. One person from your team creates a capstone Github repository. Be sure to include an appropriate `.gitignore`.
3. Add team members as read/write collaborators.
4. Add `BrityHemming` and `scertian` as read/write collaborators.
5. Work out project details from many possibilities. Be careful. Two things could go wrong:

    - If other team members seem a little _meh_ about the idea, your team will accomplish less. Enthusiasm goes a long way.
    - If you feel a little _meh_ about the project and don't speak up, how are your other team members to know?

6. Start fleshing out your project's features by writing user stories.

    - https://www.atlassian.com/agile/project-management/user-stories
    - https://en.wikipedia.org/wiki/User_story

    Dev10 doesn't have a rigid format requirement for user stories. The important thing is to focus on a discrete thing a user can do with your app. Your story should mention the user's role/persona (public/unauthenticated, standard user, moderator, admin). Users may have more than one role. It should include a single, meaningful interaction with an app feature.

    Generic template: "As a [persona], I [want to], [so that]."

    ### Good ✔

    - As a casual user, I want to search for products, so I can find something I might be interested in purchasing.
    - As an authenticated user, I want to add a product to my cart, so I may purchase it later.
    - As a product administrator, I want to mark a product as unavailable, so I can control inventory when automatic inventory management isn't working.

    ### Bad ❌

    - As a user, I want to be able to shop on the site, so I can purchase things with my credit card. (too vague, multiple stories)
    - As an administrator, I want to control data in the Oracle database that isn't editable by a standard user, so I can better serve our customers. (too vague, don't mention specific technologies)
    - I need to be able to view reports so I can make informed decisions. (too vague, no persona)

    User stories may have dependencies: 
    
    - "As an authenticated user, I want to add a product to my cart, so I may purchase it later." 
    - Depends on: "As an unauthenticated user, I want to log in, so I can use features that require authentication." (could be standard user or admin)
    - Which depends on: "As a new user, I want to create an account, so I can make purchases." (new users can't automatically become admins)

7. Once you're happy with user stories, think about the technical details behind them. To "realize" the user story, what has to happen in?:

    - the database (tables and relationships)
    - the REST API (models, repositories, services, controllers)
    - the UI (components and their relationships)
    - other details (complex rules, calculations, challenging technologies)

8. Create concrete technical tasks, estimated to take no longer than 4 hours, that target design and/or code in specific physical or conceptual layers. Hour estimates are required. From the user story perspective, some technical tasks may be repeated. For example, "Create the `product` table in the database." may be a technical requirement for several user stories. 

    Try your best to keep tasks granular. If a task will take longer than 4 hours, consider ways to break it down further: 

    - Implement the `findByCategory` method in the `ProductRepository`.
    - Implement the `findById` method in the `ProductRepository`.

    versus

    - Implement the `ProductRepository`.

    Determine tasks dependencies. For example, a data model task must happen _before_ a repository task. A REST API task may need to be complete before a React component task. (Though not always. Sometimes we agree on the "shape" of a model/class/object and then mock those interactions in different layers.)

9. When everything is estimated to the most granular level possible, total up your hours. Is it even possible to complete the project in 2 weeks? Is there too much work? Too little? If too much, preemptively start removing features. If too little, add features.

10. Take a step back from user stories. Consider your data model, class model, layers, and project structure more cohesively. Work through design from a zoomed-out context. Does this change technical tasks?

11. Finally, with dependencies in mind, start scheduling tasks for your team members.

## Tips

- Tackle the hardest thing first. If you save it for last, the effort required won't be known until the end. By then, it will be too late. By scheduling it first, the hardest thing's scope reveals itself immediately. If you're still struggling with it at the end of the week, pivot.

- Prototype, prototype, prototype (mostly for unknowns). Don't assume you'll know how to integrate something unknown immediately. Instead, build low-effort, low-risk prototypes that won't be used in the project and then use lessons-learned to integrate the formerly-unknown tech into your project.