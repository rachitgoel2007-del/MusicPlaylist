# Console-Based Music Playlist Engine (Core Java)

A beginner-friendly, modular console application built using Core Java to manage, organize, and control music playlists with persistent file storage.

---

## 📌 Project Overview
- **Language**: Java (JDK 8+)
- **Architecture**: Object-Oriented (Model-Controller Separation)
- **Persistence**: File I/O (`songs.txt`)
- **Interface**: Interactive Console Menu

---

## 🚀 Features
1. **Add Song**: Adds song with ID, Title, Artist, and Duration. Prevents duplicate IDs.
2. **Remove Song**: Removes a song by its unique ID.
3. **Display Playlist**: Lists all songs in the current playlist queue.
4. **Play Current Song**: Displays the currently active track.
5. **Next / Previous Navigation**: Safely navigates tracks with boundary detection.
6. **Shuffle Playlist**: Randomizes queue order using `Collections.shuffle()`.
7. **Recently Played History**: Automatically logs the last 10 played songs using an `ArrayList` queue.
8. **Search**: Case-insensitive search by Song Name or Artist.
9. **Sort**: In-place Bubble sort implementation by Title (A-Z), Artist (A-Z), or Duration (shortest to longest).
10. **File Persistence (Save & Auto-Load)**:
    - Automatically loads saved songs from `songs.txt` when the program starts.
    - Saves changes to `songs.txt` manually (Option 11) or automatically on exit (Option 12).
11. **Safe Exit**: Graceful shutdown.

---

## 🛠️ Class Structure

```
MusicPlaylist/
├── Song.java       # Model class: attributes, constructor, getters, display
├── Playlist.java   # Data management: ArrayList, Bubble Sort, search, shuffle, File I/O
├── Main.java       # Controller: menu loop, scanner input, navigation, history
├── songs.txt       # File storage: persistent comma-separated text storage
└── README.md       # Project documentation
```

---

## 💻 How to Compile and Run

### 1. Compile
```bash
javac *.java
```

### 2. Run
```bash
java Main
```

---

## 📚 Course Concepts Demonstrated
- **Object-Oriented Programming (OOP)**: Encapsulation (`private` fields, `public` getters), Constructors.
- **Java Collections Framework**: `ArrayList<Song>` dynamic storage and element traversal.
- **File Handling & Persistence**: `File`, `FileWriter`, and `PrintWriter` for reading and writing `songs.txt`.
- **Exception Handling**: `try-catch` blocks for robust File I/O error prevention.
- **Algorithms**: In-place Bubble Sort using `.compareToIgnoreCase()`.
- **String Manipulation**: `.toLowerCase()`, `.contains()`, and `.split(",")`.
- **Console Input**: Clear handling of newline buffers with `scanner.nextLine()`.
