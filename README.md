## 🚌 BUS RESERVATION SYSTEM

A simple full-stack **Bus Reservation System** developed using a **GUI-based Java application** and **MySQL** as the backend database.


## 💻 TECH STACK

- **Frontend**: Java Swing (GUI)
- **Backend**: Java
- **Database**: MySQL
- **IDE**: NetBeans (recommended)

---

## 🛠️ Features

- User-friendly graphical interface.
- Connects to MySQL database for real-time data operations.
- Supports:
  - User registration and login.
  - Booking and cancellation of tickets.
  - Admin-side bus and route management.

---

## 🗃️ Database Setup

1. Open MySQL Workbench or any MySQL client.
2. Run the script in the `Database_bus.sql` file provided.
3. Make sure your database name matches the one used in the Java code.
4. Update DB credentials in the source code if necessary:
   ```java
   String url = "jdbc:mysql://localhost:3306/<your_database>";
   String user = "<your_username>";
   String pass = "<your_password>";

## 🚀 How to Run

Extract the project folder.

Open the project in NetBeans or your preferred Java IDE.

Set up your database (as described above).

Run the main class (typically something like Main.java or Login.java).

Start booking!

## Fullstackproject/

├── src/
│   ├── <Java source files>
├── Database_bus.sql
├── README.md

## 📌 Notes
Make sure MySQL is running before starting the application.

Ensure the required JDBC driver is added to your project library.





