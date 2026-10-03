# DevDiary

Live: https://devdiary-haseb.vercel.app

Technology Used:
------------------------
• Java
• Spring Boot
• Spring MVC
• Spring Security
• Unit Testing using JUnit and Mockito
• React
• HTML5
• CSS3
• MySQL

# Description
Unregistered/anonymous blog users can view all posts. 
Registered and logged-in users (Authenticated users) can add new posts, view only their own posts, and edit or delete them (CRUD functionality). 
Spring Security authentication and authorization rules ensure that users are only able to edit or delete their own posts.
Front-end made using ReactJs.


# Features
- Sign up / Sign in with JWT
- Sign in as guest (shared demo account, profile can not be changed)
- Create, update, delete own stories
- My stories and stories of any user
- Pagination (newest story first)
- Search stories by title or description
- Tags on stories and filter stories by tag
- Comments on stories (only the writer can delete a comment)
- Like / unlike a story
- Admin user and dummy data

# API Endpoints
Base url: `/api/v1`

| Method | Url | Login needed |
|---|---|---|
| POST | /signup | No |
| POST | /signin | No |
| POST | /signin/guest | No |
| GET | /stories/ | No |
| GET | /stories/page?pageNo=0&pageSize=6 | No |
| GET | /stories/search?keyword=spring | No |
| GET | /stories/my | Yes |
| GET | /stories/{id} | No |
| POST | /stories/ | Yes |
| PUT | /stories/{id} | Yes (owner) |
| DELETE | /stories/{id} | Yes (owner) |
| GET | /users/ | No |
| GET | /users/{id} | No |
| GET | /users/{id}/stories | No |
| GET | /tags/ | No |
| GET | /tags/{name}/stories | No |
| GET | /stories/{id}/comments | No |
| POST | /stories/{id}/comments | Yes |
| DELETE | /comments/{id} | Yes (owner) |
| GET | /stories/{id}/likes | No |
| POST | /stories/{id}/likes | Yes (like / unlike) |

Example story body:
```json
{ "title": "Learning Spring", "description": "Spring boot basics", "tags": ["java", "spring"] }
```

# Admin and dummy data
- The admin can edit or delete any story and delete any comment.
- When the database has no story, the app creates some dummy users, stories, tags, comments and likes.
- A second batch of stories about AI (LLMs, RAG, prompt engineering and more) is added one time, also to an existing database.
- Dummy users (password `Demo1234`): `rahim.uddin@demo.com`, `nusrat.jahan@demo.com`, `tanvir.ahmed@demo.com`,
  `farhan.kabir@demo.com`, `sadia.islam@demo.com`, `arif.hossain@demo.com`,
  and two parody accounts `elon.tusk@demo.com`, `mark.zuckerbot@demo.com`.

# How to run
Need Java 17 and MySQL. Create a database named `tech_blog` first.

Environment variables:

| Name | Default | |
|---|---|---|
| JWT_SECRET_KEY | - | required, make one with `openssl rand -base64 32` |
| DB_URL | jdbc:mysql://localhost:3306/tech_blog | |
| DB_USERNAME | root | |
| DB_PASSWORD | empty | |
| ALLOWED_ORIGINS | http://localhost:3000 | frontend urls, comma separated |
| ADMIN_EMAIL | hasebulhassan21@gmail.com | admin account is created only when ADMIN_PASSWORD is set |
| ADMIN_PASSWORD | empty | admin password is reset to this value on every start |
| ADMIN_NAME | Admin | |
| SEED_DATA | true | create dummy users, stories, comments and likes when there is no story |
| PORT | 8080 | |

```bash
export JWT_SECRET_KEY=$(openssl rand -base64 32)
./gradlew bootRun
./gradlew test
```

# Deploy
The app has a `Dockerfile` and a `render.yaml`, so it can be deployed on Render as a docker web service.
Set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` of a hosted MySQL (for example Aiven free MySQL, `jdbc:mysql://HOST:PORT/defaultdb?sslMode=REQUIRED`), `ADMIN_EMAIL`, `ADMIN_PASSWORD` and set `ALLOWED_ORIGINS` to `https://devdiary-haseb.vercel.app`.

The frontend is in the `frontend` folder and is deployed on Vercel.
