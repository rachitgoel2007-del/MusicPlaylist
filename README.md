# MusicPlaylist (Java Console Project)

## Requirements
- Java 17 or newer (Java 26 is fine)
- Maven
- MySQL Server

## 1. Create the database
Open MySQL and run:
```sql
CREATE DATABASE IF NOT EXISTS musicplaylist;
```
The application creates the `songs` and `play_history` tables automatically.

## 2. Set MySQL password
Open `src/main/resources/db.properties`.
Set `db.user` and `db.password` to your local MySQL credentials.

## 3. Build and run
From the folder containing `pom.xml`:
```powershell
mvn clean package
mvn exec:java
```
The Maven Exec plugin is already configured with the correct main class.

## Features
- Add, view, search and delete songs
- Store songs in MySQL using JDBC and PreparedStatement
- Simulated playback on a separate Java thread
- Pause, resume and stop playback
- Listening history stored in MySQL
- Basic input and database exception handling

Note: Playback is a console simulation; it does not play actual audio files.

## Four-member work split
1. Member 1 (project lead): `Main.java`, `Song.java`, menu and integration.
2. Member 2: `DBConnection.java`, `DatabaseSetup.java`, `SongDAO.java`, MySQL/JDBC.
3. Member 3: `PlaybackService.java`, playback thread, pause/resume/stop.
4. Member 4: `HistoryDAO.java`, testing, README and documentation.

Each member should work on a separate Git branch and submit a pull request to `main`.
