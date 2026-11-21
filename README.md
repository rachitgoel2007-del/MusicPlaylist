# Music Playlist Engine

A Java 17 console application using Maven, MySQL, JDBC, OOP, collections, exception handling and multithreaded playback simulation.

## Features
- Add, list, search and delete songs
- Create an in-memory playlist and add songs to it
- Next/previous playback simulation with pause, resume and stop controls
- Shuffle and sort playlist by title or artist
- Save and view recent listening history in MySQL

## Requirements
- JDK 17+
- Maven 3.8+
- MySQL 8+

## Database setup
1. Start MySQL.
2. Run `database/create_database.sql` in MySQL Workbench or the MySQL client.
3. Configure environment variables before running:
   - `MUSIC_DB_URL` (default `jdbc:mysql://localhost:3306/music_playlist`)
   - `MUSIC_DB_USER` (default `root`)
   - `MUSIC_DB_PASSWORD` (default empty)

PowerShell example (replace with your local password; do not commit it):
```powershell
$env:MUSIC_DB_URL="jdbc:mysql://localhost:3306/music_playlist"
$env:MUSIC_DB_USER="root"
$env:MUSIC_DB_PASSWORD="YOUR_LOCAL_PASSWORD"
```

## Build and run
```powershell
mvn clean compile
mvn exec:java
```

## Notes
Playback is a console simulation; it does not play actual audio. The playback worker runs on a background thread. The current playlist is held in memory for the running session; songs and listening history are stored in MySQL.

## Team contribution plan
- Rachit: application menu, playlist integration and final integration
- Hriday: playback controls and multithreaded playback
- Mannat: song model, song data access, search, sorting and shuffle
- Tanvi: database setup, connection and listening history

Each member should commit only the code they personally implement and test on their own branch.
