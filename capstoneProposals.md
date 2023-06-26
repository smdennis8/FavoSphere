# Capstone Proposals

Should include the following.

## 1. Problem Statement

Define a problem someone is having that can be solved with software. 

Note: the problem does not have to be original. It's absolutely okay to recreate an existing application: Instagram, Slack, etc.

### Example

> Running clubs across the world host group runs that are open to the public. **Group runs** range from an impromptu weekend run around the lake to a formal event (generate interest in the club, say thank you to friends, family, and sponsors, celebrate an occasion, etc). The important thing is that anyone can join a group run. You don't have to be a running club member.
> 
> Group run discovery is difficult. Some running clubs post a message on their home page. Others post on social media. Still others don't post anything at all and hope that word-of-mouth will spread the message. When clubs do post a message, it's usually not on a formal calendar or easy to find on a map. It's not clear if the run has already occurred.
> 
> Worse, there's no easy way to sign up for a group run. Clubs never know who will show up. If a group run is limited, it's embarrassing when too many people show up. It's also a little embarrassing when no one shows up.

## 2. Technical Solution

Briefly describe a technical solution to your problem with a couple concrete scenarios.

### Example

> Create an application for posting group runs on a formal calendar. Make it easy to sign up for runs.
> 
> ### Scenario 1
> Emma is vacationing in Austin for two weeks. She wants to relax and enjoy her vacation, but she also wants to keep up on her running. Running helps her relax. She uses the Group Run application to quickly search for runs during her two-week window, located in Austin. She signs up for one or two. Running clubs in Austin know the best routes and she gets a chance to meet new people.
> 
> ### Scenario 2
> Kelsey isn't a member of a running club, he's not ready for that commitment, but he does like to run with a group once in a while. Each weekend, he uses the Group Run application to browse runs near his Chicago neighborhood. If it feels right, he signs up and runs. There are a few running clubs near him. He doesn't have to join a club. He can pick and choose only the runs that interest him.

## 3. Glossary

Define key domain terms. This won't map one-to-one with model classes, but it may be close.

### Example

> ### Running Club
> An organization based on a shared love of running. Clubs have members. They host runs. Some are informal with infrequent runs. Others are large, have budgets, and charge membership fees.
> ### Runner
> Anyone who signs up for a run. Runners can be members of a club, but don't have to be. All members are runners but not all runners are members.
> ### Member
> A runner who is formally affiliated with a running club. A runner can be a member of more than one club.
> ### Club Admin
> A running club member with an administrator role. They have more privileges in the Group Run application. All admins are members, but not all members are admins.
> ### Run
> A running event with a specific time, date, and location. A run may also include a route (stretch goal).

## 4. High Level Requirement

Briefly describe what each user role/authority can do. (These are user stories.)

### Example

> - Create a run (MEMBER, ADMIN).
> - Edit a future run (MEMBER, ADMIN).
> - Cancel a future run (ADMIN).
> - Approve a run (ADMIN).
> - Browse runs (anyone).
> - Sign up for a run (authenticated).
> - Apply for membership (authenticated).
> - Approve a membership (ADMIN).

## 5. User Stories/Scenarios

Elaborate use stories.

### Example

> ### Create a Run
> 
> Create a run that runners can join.
> 
> Suggested data:
> - brief description (e.g. "Saturday run along the river road.")
> - date and time (must be in the future)
> - a location (choose a level of difficulty from a single address field to a separately-tracked data entity)
> - running club identifier (runs are always attached to a club. If a runner belongs to more than one club, they may need to choose)
> - max participants (`null` for unlimited?)
> - a route (data from a map integration, if appropriate)
> 
> **Precondition**: User must be logged in with the MEMBER or ADMIN role.
> 
> **Post-condition**: If the user is a MEMBER, the run is not automatically posted. It must be approved by an ADMIN. If the user is an ADMIN, they can choose to post it immediately or keep it in a pending status.
> 
> ### Edit a Run
> 
> Can only edit a run in the future.
> 
> **Precondition**: User must be logged in with the MEMBER or ADMIN role. Run datetime must be in the future.
> 
> **Post-condition**: If the user is a MEMBER, the run is set to a pending status even if it was initially posted. If the user is an ADMIN, they can choose to post it immediately or keep it in a pending status.
> 
> ### Cancel a Run
> 
> Can only cancel a run in the future.
> 
> **Precondition**: User must be logged in with the ADMIN role. Run datetime must be in the future.
> 
> **Post-condition**: Data is not deleted. The run is set to a canceled status and is no longer visible in the public UI. It *is* visible to the admin.
> 
> ### Approve a Run
> 
> Through an administrative UI, the ADMIN user finds pending runs for their club. They can choose to: post directly, edit and post, or cancel.
> 
> **Precondition**: User must be logged in with the ADMIN role.
> 
> **Post-condition**: None
> 
> ### Browse Runs
> 
> Decide how to display runs to anyone who uses the application.
> 
> - Text-based: Users filter by date and location. Display results as HTML with action UI to sign up.
> - Calendar-based: Users page through a calendar UI. Limit by location or manage the UI so there's not 200 runs on a single day.
> - Map-based: Users navigate to different locations to see future runs as pins on the map.
> 
> **Precondition**: None
> 
> **Post-condition**: None
> 
> ### Sign Up for a Run
> 
> Once a runner finds a run they're interested in, they can sign up.
> 
> **Precondition**: User must be logged in. The run must not be over-capacity. The runner cannot already be registered for the run.
> 
> **Post-condition**: Runner is registered for the run.
> 
> ### Apply for Membership (Optional)
> 
> If a runner enjoys a club's runs, they may wish to join the club. Give them an easy way to apply for membership.
> 
> **Precondition**: User must be logged in. The user cannot already be a member of the club.
> 
> **Post-condition**: Membership is in a pending status waiting for ADMIN approval.
> 
> ### Approve a Membership (Optional)
> 
> Through an administrative UI, the ADMIN user finds pending memberships for their club. They can choose to accept or reject the membership application.
> 
> **Precondition**: User must be logged in with the ADMIN role.
> 
> **Post-condition**: Data is not deleted. The membership is set to a rejected status. This prevents the runner from applying again and again.