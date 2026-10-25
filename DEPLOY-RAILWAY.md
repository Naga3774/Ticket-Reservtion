# Deploy to Railway (beginner guide)

Railway runs the app from the included `Dockerfile` and provides a MySQL database.
(Free credits/pricing change; check railway.com/pricing first.)

## A. Put the project on GitHub
1. Create an account at github.com and click **New repository** (name: `ticket-booking-system`, Public or Private, do NOT add a README).
2. Easiest upload: on the empty repo page click **uploading an existing file**, drag in everything INSIDE the unzipped `ticket-booking-system` folder
   (`src`, `pom.xml`, `Dockerfile`, etc. must be at the top level of the repo), then **Commit changes**.
   IMPORTANT: do not upload `application.properties` with a real password in it.

## B. Create the Railway project
1. Go to railway.com and sign up with GitHub.
2. **New Project -> Deploy from GitHub repo** -> choose `ticket-booking-system`.
   Railway detects the Dockerfile and starts building (first build takes a few minutes; it may fail until the database variables exist, that's fine).

## C. Add MySQL
1. In the same project click **+ New -> Database -> Add MySQL**.
2. A service named `MySQL` appears.

## D. Connect the app to MySQL
1. Click your **app service** -> **Variables** tab -> **New Variable**, and add these 5 (type them exactly; `MySQL` must match your database service's name):

| Name | Value |
|---|---|
| DB_HOST | `${{MySQL.MYSQLHOST}}` |
| DB_PORT | `${{MySQL.MYSQLPORT}}` |
| DB_USERNAME | `${{MySQL.MYSQLUSER}}` |
| DB_PASSWORD | `${{MySQL.MYSQLPASSWORD}}` |
| DB_NAME | `${{MySQL.MYSQLDATABASE}}` |

2. (Optional) `SAMPLE_DATA` = `true` to insert 2 fictional tickets once.
3. Railway redeploys automatically.

## E. Get the public link
1. App service -> **Settings -> Networking -> Generate Domain**.
2. Open `https://YOUR-DOMAIN.up.railway.app/ticket` and `/tickets`.

## Troubleshooting
- **Build failed**: check Deployments -> View logs. Make sure `pom.xml` and `Dockerfile` are at the repo root.
- **Communications link failure / Access denied**: re-check the 5 variables and the database service name.
- **Out of memory / app restarts**: the Dockerfile caps Java heap at 384 MB; raise the plan memory or lower `-Xmx`.
- **Data**: stored in Railway's MySQL, so it survives redeploys.
