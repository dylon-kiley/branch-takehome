# Branch Takehome

This project provides a service for interacting with GitHub's API to fetch user and repository details. 

## Features

- Fetch GitHub user details by username
- Fetch a list of repositories for a specific user
- Combine the GitHub data into a single response
- Provides basic exception handling for errors

---

## Running the Code

### Testing Your Own Capacities

1. Run the program:
   - Right-click on `TakehomeApplication.java`.
   - Select **Run**.
   - Service will start on port `8880`
2. Make a `GET` request to the `/users/{username}` endpoint in the `UserController`:
   - curl "http://localhost:8880/users/octocat"
   - could use Postman if preferred

---

## Example Output

### Request:

http://localhost:8880/users/octocat

### Response:

```
{
    "user_name": "octocat",
    "display_name": "The Octocat",
    "avatar": "https://avatars.githubusercontent.com/u/583231?v=4",
    "geo_location": "San Francisco",
    "email": null,
    "url": "https://github.com/octocat",
    "created_at": "2011-01-25 18:44:36",
    "repos": [
        {
            "name": "boysenberry-repo-1",
            "url": "https://github.com/octocat/boysenberry-repo-1"
        },
        {
            "name": "git-consortium",
            "url": "https://github.com/octocat/git-consortium"
        },
        {
            "name": "hello-worId",
            "url": "https://github.com/octocat/hello-worId"
        },
        {
            "name": "Hello-World",
            "url": "https://github.com/octocat/Hello-World"
        },
        {
            "name": "linguist",
            "url": "https://github.com/octocat/linguist"
        },
        {
            "name": "octocat.github.io",
            "url": "https://github.com/octocat/octocat.github.io"
        },
        {
            "name": "Spoon-Knife",
            "url": "https://github.com/octocat/Spoon-Knife"
        },
        {
            "name": "test-repo1",
            "url": "https://github.com/octocat/test-repo1"
        }
    ]
}

```

## Running Tests

1. Navigate to the `src/test` directory.
2. Right-click on the test files or the test directory.
3. Select **Run** to execute the tests.


---

## Tools and Frameworks

Developed using Java 21, Spring, Jackson, JUnit, Mockito, Gradle, Postman, and IntelliJ 