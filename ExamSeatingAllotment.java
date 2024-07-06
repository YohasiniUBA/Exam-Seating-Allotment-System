package com.mycompany.examseatingallotment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

class Student {

    String rollNo;
    String s_name;
    String dept;
    int yearofstudy;

    public Student() {
    }

}

class Staff {

    String staffId;
    String staff_name;
    String dept;
}

class ExamTimetable {

    int year;
    String dept;
    Date date;
    String slot;
    String courseCode;
    String courseName;
}

class Hall {

    String hallNo;
    int noOfSeats;
    int noOfRows;
    int noOfColumns;
    int seatsAllocated;

    public String findNextAvailableHall(Connection conn) throws SQLException {
        String hallQuery = "SELECT hallno, no_of_seats, seats_allocated FROM halls";
        try ( PreparedStatement hallStmt = conn.prepareStatement(hallQuery)) {
            ResultSet hallRs = hallStmt.executeQuery();

            while (hallRs.next()) {
                String hallNo = hallRs.getString("hallno");
                int noOfSeats = hallRs.getInt("no_of_seats");
                int seatsAllocated = hallRs.getInt("seats_allocated");

                if (seatsAllocated < noOfSeats) {
                    return hallNo; // Return the first hall with available seats
                }
            }

            return null; // Return null if no available hall is found
        }
    }

    public String findNextHall(Connection conn, String currenthallNo) throws SQLException {
        String hallQuery = "SELECT hallno FROM halls ORDER BY hallno";
        try ( PreparedStatement hallStmt = conn.prepareStatement(hallQuery)) {
            ResultSet hallRs = hallStmt.executeQuery();

            String nextHall = null;
            boolean foundCurrentHall = false;

            while (hallRs.next()) {
                String hallNo = hallRs.getString("hallno");

                if (foundCurrentHall) {
                    // Return the next hall after finding the current hall
                    return hallNo;
                }

                if (currenthallNo.equals(hallNo)) {
                    foundCurrentHall = true;
                }

                if (nextHall == null) {
                    nextHall = hallNo;
                }
            }

            // If we reach the end, wrap around to the first hall
            return nextHall;
        }
    }

    public void updateSeatsAllocated(Connection conn, String hallNo) throws SQLException {
        // Increment the seatsAllotted count for the hall
        //System.out.println(" seats allocated incremented");
        String updateQuery = "UPDATE halls SET seats_allocated = seats_allocated + 1 WHERE hallno = ?";
        try ( PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
            updateStmt.setString(1, hallNo);
            updateStmt.executeUpdate();
        }
    }

    public void resetSeatsAllocated(Connection conn) throws SQLException {
        String resetSeatsQuery = "UPDATE halls SET seats_allocated = 0";
        try ( PreparedStatement resetSeatsStmt = conn.prepareStatement(resetSeatsQuery)) {
            resetSeatsStmt.executeUpdate();
        }
    }
}

class Seat {

    int seatNo;
    int rowNo;
    int columnNo;
    Hall hall = new Hall();

    public Seat() {

    }

    public int findNextAvailableSeat(Connection conn, String hallNo, int noOfSeats, Date examDate, String currentrollno, String currentdept) throws SQLException {
        // Find the maximum seat number for the specified hall
        String maxSeatQuery = "SELECT MAX(seatno) FROM allocated_seats WHERE hallno = ? AND date = ? AND dept = ?";//(SELECT dept FROM student WHERE rollno = ?";
        int maxSeat = 0;

        try ( PreparedStatement maxSeatStmt = conn.prepareStatement(maxSeatQuery)) {
            maxSeatStmt.setString(1, hallNo);
            maxSeatStmt.setDate(2, (java.sql.Date) examDate);
            //maxSeatStmt.setString(3, currentrollno);
            maxSeatStmt.setString(3, currentdept);
            ResultSet maxSeatRs = maxSeatStmt.executeQuery();

            if (maxSeatRs.next()) {
                maxSeat = maxSeatRs.getInt(1);
                //System.out.println("maxSeat " + maxSeat);
            }
        }

        // Check if the last allocated seat has a student from the same department
        String checkDeptQuery = "SELECT rollno FROM allocated_seats WHERE hallno = ? AND date = ? AND seatno = ?";
        boolean sameDeptFound = false;

        try ( PreparedStatement checkDeptStmt = conn.prepareStatement(checkDeptQuery)) {
            checkDeptStmt.setString(1, hallNo);
            checkDeptStmt.setDate(2, (java.sql.Date) examDate);
            checkDeptStmt.setInt(3, maxSeat);

            ResultSet checkDeptRs = checkDeptStmt.executeQuery();

            while (checkDeptRs.next()) {
                String rollNo = checkDeptRs.getString("rollno");
                String dept = getStudentDepartment(conn, rollNo);

                if (isSameDepartment(conn, currentrollno, rollNo)) {
                    sameDeptFound = true;
                    break;
                }
            }
        }

        if (sameDeptFound) {
            //System.out.println("same dept");
            maxSeat = maxSeat + 2; // Skip one seat if a student from the same department is found
        } else {
            maxSeat = maxSeat + 1;
        }
        if (maxSeat <= noOfSeats) {
            return maxSeat;
        } else {

            return 0;
        }
    }

// You can implement a method to check if two students are from the same department
    public boolean isSameDepartment(Connection conn, String rollNo1, String rollNo2) throws SQLException {
        String sameDeptQuery = "SELECT dept FROM student WHERE rollno = ?";

        String department1 = null;
        String department2 = null;

        try ( PreparedStatement sameDeptStmt = conn.prepareStatement(sameDeptQuery)) {
            sameDeptStmt.setString(1, rollNo1);
            ResultSet sameDeptRs = sameDeptStmt.executeQuery();

            while (sameDeptRs.next()) {
                department1 = sameDeptRs.getString("dept");
            }
        }

        try ( PreparedStatement sameDeptStmt = conn.prepareStatement(sameDeptQuery)) {
            sameDeptStmt.setString(1, rollNo2);
            ResultSet sameDeptRs = sameDeptStmt.executeQuery();

            while (sameDeptRs.next()) {
                department2 = sameDeptRs.getString("dept");
            }
        }

        // Compare the departments
        if (department1 != null && department2 != null && department1.equals(department2)) {
            return true; // Departments are the same
        } else {
            return false; // Departments are not the same
        }
    }

// You can implement a method to get the department of a student
    public String getStudentDepartment(Connection conn, String rollNo) {
        // Query the database to get the department of a student
        // Implement your logic here
        return "department"; // Replace with the actual department
    }

    public boolean isSeatAllocated(Connection conn, String studentRollNo) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM allocated_seats WHERE rollno = ?";

        try ( PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setString(1, studentRollNo);

            try ( ResultSet checkRs = checkStmt.executeQuery()) {
                if (checkRs.next()) {
                    int count = checkRs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isSeatOccupied(Connection conn, String hallNo, Date examDate, int currentseatNo, int noOfSeats) throws SQLException {
        if (currentseatNo > noOfSeats) {
            return true;
        }
        String checkQuery = "SELECT seatno FROM allocated_seats WHERE hallno = ? AND date = ?";
        //System.out.println("current seat:" + currentseatNo);
        try ( PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setString(1, hallNo);
            checkStmt.setDate(2, (java.sql.Date) examDate);

            try ( ResultSet checkRs = checkStmt.executeQuery()) {
                while (checkRs.next()) {
                    int seatno = checkRs.getInt(1);
                    //System.out.println("loop seatno "+ seatno);
                    if (seatno == currentseatNo) {
                        return true;
                    }

                }
            }
        }

        return false;
    }
}

class AllocatedSeats {

    Hall hall = new Hall();
    private String rollNo;
    private Date date;
    private String hallNo;
    private int seatNo;

    public void allocateSeatToStudent(Connection conn, String studentRollNo, String dept, Date examDate, String hallNo, int seatNo) throws SQLException {
        //System.out.println("seat allocated " + seatNo);
        hall.updateSeatsAllocated(conn, hallNo);
// Allocate the seat to the student in the allocated_seats table
        String insertQuery = "INSERT INTO allocated_seats (rollno, dept, date, hallno, seatno) VALUES (?, ?, ?, ?, ?)";
        try ( PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            insertStmt.setString(1, studentRollNo);
            insertStmt.setString(2, dept);
            insertStmt.setDate(3, (java.sql.Date) examDate);

            insertStmt.setString(4, hallNo);
            insertStmt.setInt(5, seatNo);
            insertStmt.executeUpdate();
        }
    }
}

final class StudentSeatAllocation {

    public StudentSeatAllocation() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/examseatingallotment";
        String dbUsername = "root";
        String dbPassword = "Yoha@)04";

        //Object instantiation
        Hall hall = new Hall();
        Seat seat = new Seat();
        AllocatedSeats allseats = new AllocatedSeats();
        Student student = new Student();

        List<String> studentsWithoutSeats = new ArrayList<>();

        try ( Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            // Query to retrieve exam dates and slots
            String examQuery = "SELECT DISTINCT date FROM exam_timetable";
            try ( PreparedStatement examStmt = conn.prepareStatement(examQuery)) {
                ResultSet examRs = examStmt.executeQuery();

                while (examRs.next()) {
                    Date examDate = examRs.getDate("date");

                    hall.resetSeatsAllocated(conn);

                    String allocatedSeats_yearQuery = "SELECT DISTINCT yearofstudy FROM student ";
                    try ( PreparedStatement allocatedSeats_yearStmt = conn.prepareStatement(allocatedSeats_yearQuery)) {
                        ResultSet allocatedSeats_yearRs = allocatedSeats_yearStmt.executeQuery();
                        while (allocatedSeats_yearRs.next()) {
                            int year = allocatedSeats_yearRs.getInt(1);
                            //System.out.println("_________________________________________________________________________________________________________________________ year : " + year);
                            String deptQuery = "SELECT DISTINCT dept FROM exam_timetable";
                            try ( PreparedStatement deptStmt = conn.prepareStatement(deptQuery)) {
                                ResultSet deptRs = deptStmt.executeQuery();

                                while (deptRs.next()) {
                                    String dept = deptRs.getString("dept");
                                    //System.out.println(dept);

                                    // Query to retrieve a list of students
                                    String studentQuery = "SELECT rollno FROM student WHERE dept = ? AND yearofstudy = ?";
                                    try ( PreparedStatement studentStmt = conn.prepareStatement(studentQuery)) {
                                        studentStmt.setString(1, dept);
                                        studentStmt.setInt(2, year);
                                        ResultSet studentRs = studentStmt.executeQuery();
                                        String studentRollNo;
                                        while (studentRs.next()) {
                                            studentRollNo = studentRs.getString("rollno");
                                            //System.out.println(studentRollNo);
                                            // Find the next available hall
                                            String hallNo = hall.findNextAvailableHall(conn);
                                            String hallQuery = "SELECT no_of_seats, seats_allocated FROM halls WHERE hallno = ? ";
                                            try ( PreparedStatement hallStmt = conn.prepareStatement(hallQuery)) {
                                                hallStmt.setString(1, hallNo);
                                                ResultSet hallRs = hallStmt.executeQuery();

                                                int seatsAllocated;

                                                while (hallRs.next()) {
                                                    int noOfSeats = hallRs.getInt("no_of_seats");
                                                    seatsAllocated = hallRs.getInt("seats_allocated");
                                                    //System.out.println(hallNo);
                                                    int seatNo;

                                                    // Check if the seat for this student on this date is already allocated
                                                    boolean seatAllocated = seat.isSeatAllocated(conn, studentRollNo);
                                                    if (seatsAllocated <= noOfSeats - 1) {
                                                        seatNo = seat.findNextAvailableSeat(conn, hallNo, noOfSeats, examDate, studentRollNo, dept);
                                                        if (seatNo == 0) {
                                                            hallNo = hall.findNextHall(conn, hallNo);
                                                            //System.out.println("next hall:" + hallNo);
                                                            seatNo = seat.findNextAvailableSeat(conn, hallNo, noOfSeats, examDate, studentRollNo, dept);
                                                        }
                                                        if (!seat.isSeatOccupied(conn, hallNo, examDate, seatNo, noOfSeats)) {

                                                        } else {
                                                            while (seat.isSeatOccupied(conn, hallNo, examDate, seatNo, noOfSeats)) {
                                                                seatNo++;
                                                            }
                                                        }
                                                        if (seatNo <= noOfSeats) {
                                                            seatsAllocated++;
                                                            allseats.allocateSeatToStudent(conn, studentRollNo, dept, examDate, hallNo, seatNo);
                                                        } else {
                                                            studentsWithoutSeats.add(studentRollNo);
                                                        }
                                                        

                                                    }
                                                }
                                            }
                                        }
                                    }
                                    // After completing all seat allocations, insert students without seats into allocated_seats table
                                    for (String studentWithoutSeat : studentsWithoutSeats) {
                                        allseats.allocateSeatToStudent(conn, studentWithoutSeat, dept, examDate, null, 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (String str : studentsWithoutSeats) {
            System.out.println(str);
        }
    }
}

class StaffAllocation {

    public StaffAllocation() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/examseatingallotment";
        String dbUsername = "root";
        String dbPassword = "Yoha@)04";

        try ( Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            String allocatedSeats_dateQuery = "SELECT DISTINCT date FROM allocated_seats ";
            try ( PreparedStatement allocatedSeats_dateStmt = conn.prepareStatement(allocatedSeats_dateQuery)) {
                ResultSet allocatedSeats_dateRs = allocatedSeats_dateStmt.executeQuery();
                while (allocatedSeats_dateRs.next()) {
                    resetStaffTable(conn);

                    Date examDate = allocatedSeats_dateRs.getDate("date");

                    //Date examDate = allocatedSeats_dateRs.getDate("date");
                    String allocatedHallQuery = "SELECT DISTINCT hallno FROM allocated_seats WHERE date = ?";
                    try ( PreparedStatement allocatedHallStmt = conn.prepareStatement(allocatedHallQuery)) {
                        allocatedHallStmt.setDate(1, (java.sql.Date) examDate);
                        ResultSet allocatedHallRs = allocatedHallStmt.executeQuery();
                        while (allocatedHallRs.next()) {
                            String hallNo = allocatedHallRs.getString("hallno");

                            String allocatedSeatQuery = "SELECT DISTINCT seatno FROM allocated_seats WHERE date = ? AND hallno = ? ORDER BY seatno ASC";
                            try ( PreparedStatement allocatedSeatStmt = conn.prepareStatement(allocatedSeatQuery)) {
                                allocatedSeatStmt.setDate(1, (java.sql.Date) examDate);
                                allocatedSeatStmt.setString(2, hallNo);
                                ResultSet allocatedSeatRs = allocatedSeatStmt.executeQuery();
                                while (allocatedSeatRs.next()) {

                                    resetStaffTable(conn);
                                    String staffId = findNextAvailableStaff(conn, hallNo, examDate);

                                    if (staffId != null) {
                                        // Allocate seats in halls for the staff
                                        allocateSeatsInHalls(conn, staffId, hallNo, examDate);
                                    }
                                }

                            }
                        }

                    }
                }
            }

            // Step 1: Retrieve allocated seats that need staff allocation
            String allocatedSeatQuery = "SELECT date, hallno, seatno, staffid FROM allocated_seats WHERE staffid IS NOT NULL";
            try ( PreparedStatement allocatedSeatStmt = conn.prepareStatement(allocatedSeatQuery)) {
                ResultSet allocatedSeatRs = allocatedSeatStmt.executeQuery();

                // Group allocated seats by staff
                while (allocatedSeatRs.next()) {
                    Date examDate = allocatedSeatRs.getDate("date");
                    String hallNo = allocatedSeatRs.getString("hallno");
                    String staffId = allocatedSeatRs.getString("staffid");

                    // Find the range of allocated seat numbers for this staff
                    String seatNosRange = findSeatNumbersRange(conn, examDate, hallNo, staffId);

                    // Step 2: Insert staff allocation with seat range
                    // Before inserting, check for duplicate records with the same date, hallno, and staffId
                    String duplicateCheckQuery = "SELECT * FROM allocated_staff WHERE date = ? AND hallno = ? AND staffid = ?";
                    try ( PreparedStatement duplicateCheckStmt = conn.prepareStatement(duplicateCheckQuery)) {
                        duplicateCheckStmt.setDate(1, (java.sql.Date) examDate);
                        duplicateCheckStmt.setString(2, hallNo);
                        duplicateCheckStmt.setString(3, staffId);
                        ResultSet duplicateCheckRs = duplicateCheckStmt.executeQuery();

                        // If a matching record exists, handle it (e.g., update or skip)
                        if (duplicateCheckRs.next()) {
                            // Handle the duplicate record here
                            // For example, you can update the existing record
                            // Or, you can choose to skip the insertion
                        } else {
                            // No matching record found, proceed with the insertion
                            seatNosRange = findSeatNumbersRange(conn, examDate, hallNo, staffId);
                            insertStaffAllocation(conn, examDate, hallNo, staffId, seatNosRange);
                        }
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void allocateSeatsInHalls(Connection conn, String staffId, String hallNo, Date examDate) throws SQLException {
        String allocateSeatsQuery = "SELECT seatno FROM allocated_seats WHERE date = ? AND hallno = ? AND staffid IS NULL ORDER BY seatno ASC LIMIT 1 ";
        try ( PreparedStatement allocateSeatsStmt = conn.prepareStatement(allocateSeatsQuery)) {
            allocateSeatsStmt.setDate(1, (java.sql.Date) examDate);
            allocateSeatsStmt.setString(2, hallNo);
            ResultSet allocateSeatsRs = allocateSeatsStmt.executeQuery();
            while (allocateSeatsRs.next()) {
                int seatNo = allocateSeatsRs.getInt("seatno");

                //System.out.println(seatNo);
                assignStaffToSeat(conn, staffId, examDate, hallNo, seatNo);
                if (isHallFull(conn, hallNo, examDate)) {
                    updateStaffAvailability(conn, staffId, false);

                    break;
                }
            }
        }
    }

    private static String findNextAvailableStaff(Connection conn, String hallNo, Date examDate) throws SQLException {
        //System.out.println("reset3");
        resetStaffTable(conn);
        String availableStaffQuery = "SELECT staffid, seats_allocated_for_staff FROM staff WHERE available = 1";
        try ( PreparedStatement availableStaffStmt = conn.prepareStatement(availableStaffQuery)) {
            ResultSet availableStaffRs = availableStaffStmt.executeQuery();
            String staffId;
            int seats_allocated_for_staff;

            while (availableStaffRs.next()) {
                staffId = availableStaffRs.getString("staffid");
                seats_allocated_for_staff = availableStaffRs.getInt("seats_allocated_for_staff");
                resetStaffTable(conn);
                if (seats_allocated_for_staff < 20) {
                    seats_allocated_for_staff++;
                    updateSeatsAllocatedForStaff(conn, staffId, seats_allocated_for_staff);
                    return staffId;
                } else {
                    updateStaffAvailability(conn, staffId, false);
                    resetStaffTable(conn);
                    staffId = findNextAvailableStaff(conn, hallNo, examDate);
                    return staffId;
                }

            }

        }
        return (" ");
    }

    private static void assignStaffToSeat(Connection conn, String staffId, Date examDate, String hallNo, int seatNo) throws SQLException {
        // Update staff allocation for the allocated seat
        String updateQuery = "UPDATE allocated_seats SET staffid = ? WHERE date = ? AND hallno = ? AND seatno = ?";
        try ( PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
            updateStmt.setString(1, staffId);
            updateStmt.setDate(2, (java.sql.Date) examDate);
            updateStmt.setString(3, hallNo);
            updateStmt.setInt(4, seatNo);
            updateStmt.executeUpdate();

        }
    }

    private static void updateSeatsAllocatedForStaff(Connection conn, String staffId, int seats_allocated_for_staff) throws SQLException {
        String updateQuery = "UPDATE staff SET seats_allocated_for_staff = ? WHERE staffid = ?";
        try ( PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
            updateStmt.setInt(1, seats_allocated_for_staff);
            updateStmt.setString(2, staffId);
            updateStmt.executeUpdate();
        }
    }

    private static void updateStaffAvailability(Connection conn, String staffId, boolean available) throws SQLException {
        // Update staff availability in the staff table
        String updateQuery = "UPDATE staff SET available = ? WHERE staffid = ?";
        try ( PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
            updateStmt.setBoolean(1, available);
            updateStmt.setString(2, staffId);
            updateStmt.executeUpdate();
        }
    }

    private static String findSeatNumbersRange(Connection conn, Date examDate, String hallNo, String staffId)
            throws SQLException {
        String seatNosQuery = "SELECT IF(MIN(seatno) = MAX(seatno), CAST(MIN(seatno) AS CHAR), CONCAT(MIN(seatno), '-', MAX(seatno))) AS seat_range " + "FROM allocated_seats "
                + "WHERE date = ? AND hallno = ? AND staffid = ?";

        try ( PreparedStatement seatNosStmt = conn.prepareStatement(seatNosQuery)) {
            seatNosStmt.setDate(1, (java.sql.Date) examDate);
            seatNosStmt.setString(2, hallNo);
            seatNosStmt.setString(3, staffId);

            ResultSet seatNosRs = seatNosStmt.executeQuery();
            if (seatNosRs.next()) {
                return seatNosRs.getString("seat_range");
            }
            return null;
        }
    }

    private static void insertStaffAllocation(Connection conn, Date examDate, String hallNo, String staffId,
            String seatNosRange) throws SQLException {
        // Insert staff allocation with seat range into the allocated_staff table
        String insertQuery = "INSERT INTO allocated_staff (date, hallno, staffid, seatnos) VALUES (?, ?, ?, ?)";
        try ( PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            insertStmt.setDate(1, (java.sql.Date) examDate);
            insertStmt.setString(2, hallNo);
            insertStmt.setString(3, staffId);
            insertStmt.setString(4, seatNosRange);
            insertStmt.executeUpdate();
        }
    }

    private static boolean isHallFull(Connection conn, String hallNo, Date examDate) throws SQLException {
        // Check if the total number of seats in the hall is allocated for a given date
        String hallFullQuery = "SELECT COUNT(*) FROM allocated_seats WHERE hallno = ? AND date = ? AND staffid IS NOT NULL";
        try ( PreparedStatement hallFullStmt = conn.prepareStatement(hallFullQuery)) {

            hallFullStmt.setString(1, hallNo);
            hallFullStmt.setDate(2, (java.sql.Date) examDate);
            ResultSet hallFullRs = hallFullStmt.executeQuery();
            hallFullRs.next();
            int seatsAllocated = hallFullRs.getInt(1);
            int totalSeats = getTotalSeatsInHall(conn, hallNo);
            if (seatsAllocated >= totalSeats) {
                return true;
            } else {
                return false;
            }
        }
    }

    private static int getTotalSeatsInHall(Connection conn, String hallNo) throws SQLException {
        // Retrieve the total number of seats in the hall
        String totalSeatsQuery = "SELECT no_of_seats FROM halls WHERE hallno = ?";
        try ( PreparedStatement totalSeatsStmt = conn.prepareStatement(totalSeatsQuery)) {
            totalSeatsStmt.setString(1, hallNo);
            ResultSet totalSeatsRs = totalSeatsStmt.executeQuery();
            totalSeatsRs.next();
            return totalSeatsRs.getInt(1);
        }
    }

    private static void resetStaffAvailable(Connection conn) throws SQLException {
        String resetSeatsQuery = "UPDATE staff SET available = 1";
        try ( PreparedStatement resetSeatsStmt = conn.prepareStatement(resetSeatsQuery)) {
            resetSeatsStmt.executeUpdate();
        }
    }

    private static void resetSeatsAllocatedForStaff(Connection conn) throws SQLException {
        String resetSeatsQuery = "UPDATE staff SET seats_allocated_for_staff = 0";
        try ( PreparedStatement resetSeatsStmt = conn.prepareStatement(resetSeatsQuery)) {
            resetSeatsStmt.executeUpdate();
        }
    }

    private static void resetStaffTable(Connection conn) throws SQLException {
        int flag = 0;
        String query = "SELECT available FROM staff";
        try ( PreparedStatement queryStmt = conn.prepareStatement(query)) {
            ResultSet queryRs = queryStmt.executeQuery();

            while (queryRs.next()) {
                int available = queryRs.getInt("available");
                if (available == 0) {
                    flag = 1;
                } else {
                    flag = 0;
                }
            }

            if (flag == 1) {
                resetStaffAvailable(conn);
                resetSeatsAllocatedForStaff(conn);
            }
        }
    }

}

final class ConflictReport {

    Connection conn;

    ConflictReport(Connection conn) throws SQLException {
        this.conn = conn;
        SeatsNotSufficient(conn);
        StaffNotSufficient(conn);
    }

    public void SeatsNotSufficient(Connection conn) throws SQLException {

        String notSufficientQuery = "SELECT DISTINCT rollno,date FROM allocated_seats WHERE rollno = (SELECT DISTINCT rollno FROM allocated_seats WHERE seatno IS NULL)";
        try ( PreparedStatement notSufficientStmt = conn.prepareStatement(notSufficientQuery)) {
            ResultSet notSufficientRs = notSufficientStmt.executeQuery();
            while (notSufficientRs.next()) {
                String date = notSufficientRs.getString("date");
                String rollNo = notSufficientRs.getString("rollno");
                
                //String courseCode = notSufficientRs.getString("course_code");
                //System.out.println("rollno: " + rollNo);
                //System.out.println("date :" + date);

                // Check if the combination of rollNo and date exists in the conflict_report table
                String checkConflictQuery = "SELECT COUNT(*) AS count FROM conflict_report WHERE rollno = ? AND date = ?";

                try ( PreparedStatement checkConflictStmt = conn.prepareStatement(checkConflictQuery)) {
                    checkConflictStmt.setString(1, rollNo);
                    checkConflictStmt.setString(2, date);

                    ResultSet countRs = checkConflictStmt.executeQuery();
                    countRs.next();
                    int count = countRs.getInt("count");

                    if (count == 0) {

                        // Insert the conflict data into the conflict_report table
                        String insertConflictQuery = "INSERT INTO conflict_report (rollno, date, reason) VALUES (?, ?, ?)";

                        try ( PreparedStatement insertConflictStmt = conn.prepareStatement(insertConflictQuery)) {
                            insertConflictStmt.setString(1, rollNo);
                            insertConflictStmt.setString(2, date);
                            insertConflictStmt.setString(3, "Seats not sufficient");

                            // Execute the INSERT statement
                            insertConflictStmt.executeUpdate();
                        }
                    }
                }
            }
        }
    }

    public void StaffNotSufficient(Connection conn) throws SQLException {

        String notSufficientQuery = "SELECT DISTINCT rollno, date FROM allocated_seats WHERE staffid IS NULL ";
        try ( PreparedStatement notSufficientStmt = conn.prepareStatement(notSufficientQuery)) {
            ResultSet notSufficientRs = notSufficientStmt.executeQuery();
            while (notSufficientRs.next()) {
                String rollNo = notSufficientRs.getString("rollno");
                String date = notSufficientRs.getString("date");
                //System.out.println("rollno: " + rollNo);
                //System.out.println("date :" + date);

                // Check if the combination of rollNo and date exists in the conflict_report table
                String checkConflictQuery = "SELECT COUNT(*) AS count FROM conflict_report WHERE rollno = ? AND date = ?";

                try ( PreparedStatement checkConflictStmt = conn.prepareStatement(checkConflictQuery)) {
                    checkConflictStmt.setString(1, rollNo);
                    checkConflictStmt.setString(2, date);

                    ResultSet countRs = checkConflictStmt.executeQuery();
                    countRs.next();
                    int count = countRs.getInt("count");

                    if (count == 0) {

                        // Insert the conflict data into the conflict_report table
                        String insertConflictQuery = "INSERT INTO conflict_report (rollno, date, reason) VALUES (?, ?, ?)";

                        try ( PreparedStatement insertConflictStmt = conn.prepareStatement(insertConflictQuery)) {
                            insertConflictStmt.setString(1, rollNo);
                            insertConflictStmt.setString(2, date);
                            insertConflictStmt.setString(3, "Staff not sufficient");

                            // Execute the INSERT statement
                            insertConflictStmt.executeUpdate();
                        }
                    }
                }
            }
        }
    }
}

class GUI {

    public GUI(Connection conn) {
        // Create the main frame
        JFrame mainFrame = new JFrame("Login");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(300, 150);

        // Create a panel with GridBagLayout for centering the buttons
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5); // Padding
        // Set the background color
        panel.setBackground(Color.LIGHT_GRAY);

        // Create buttons for student, staff, and admin
        JButton studentButton = new JButton("Student");
        JButton staffButton = new JButton("Staff");
        JButton adminButton = new JButton("Admin");

        // Add action listeners to the buttons
        studentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentGUI studentGUI = new StudentGUI(conn);
            }
        });

        staffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StaffGUI stafftGUI = new StaffGUI(conn);
            }
        });

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AdminGUI adminGUI = new AdminGUI(conn);
                } catch (SQLException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Add buttons to the panel with constraints to center them vertically
        constraints.gridy = 0;
        panel.add(studentButton, constraints);

        constraints.gridy = 1;
        panel.add(staffButton, constraints);

        constraints.gridy = 2;
        panel.add(adminButton, constraints);

        // Add the panel to the main frame
        mainFrame.add(panel);

        // Center the frame on the page
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setLocation(dim.width / 2 - mainFrame.getSize().width / 2, dim.height / 2 - mainFrame.getSize().height / 2);

        // Display the main frame
        mainFrame.setVisible(true);
    }
}

class AdminGUI extends JFrame {

    Connection conn;

    public AdminGUI(Connection conn) throws SQLException {
        this.conn = conn;
        createLoginPage(conn);
    }

    public void createLoginPage(Connection conn) {
        JFrame loginFrame = new JFrame("Admin Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 150);

        // Center the frame on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) ((screenSize.getWidth() - loginFrame.getWidth()) / 2);
        int centerY = (int) ((screenSize.getHeight() - loginFrame.getHeight()) / 2);
        loginFrame.setLocation(centerX, centerY);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Set the background color
        panel.setBackground(Color.LIGHT_GRAY);

        loginFrame.add(panel);

        JLabel usernameLabel = new JLabel("Admin Id:");
        usernameLabel.setBounds(10, 10, 80, 25);
        panel.add(usernameLabel);

        JTextField usernameField = new JTextField(20);
        usernameField.setBounds(100, 10, 160, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 40, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 40, 160, 25);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(100, 80, 80, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String adminid = usernameField.getText();
                String given_password = new String(passwordField.getPassword());

                //System.out.println("loginFrame");
                boolean isValid;
                try {
                    isValid = validateCredentials(conn, adminid, given_password);
                    if (isValid) {
                        //System.out.println("isValid");
                        openAdminOptions(conn);
                        loginFrame.dispose(); // Close the login frame
                    } else {
                        JOptionPane.showMessageDialog(loginFrame, "Invalid username or password");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(AdminGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        loginFrame.setVisible(true);
    }

    private void openAdminOptions(Connection conn) {
        JFrame optionsFrame = new JFrame("Admin");
        optionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        optionsFrame.setSize(300, 250); // Increase the height for the additional button

        // Center the frame on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) ((screenSize.getWidth() - optionsFrame.getWidth()) / 2);
        int centerY = (int) ((screenSize.getHeight() - optionsFrame.getHeight()) / 2);
        optionsFrame.setLocation(centerX, centerY);

        JPanel panel = new JPanel();
        optionsFrame.add(panel);
        panel.setLayout(new GridLayout(5, 1, 10, 10)); // Use GridLayout for alignment
        panel.setBackground(Color.LIGHT_GRAY); // Change the background color

        JButton scheduleSeatsButton = new JButton("Schedule Seats");
        JButton scheduleStaffButton = new JButton("Schedule Staff");
        JButton viewStudentReportButton = new JButton("View Student Report");
        JButton viewStaffReportButton = new JButton("View Staff Report");
        JButton viewConflictReportButton = new JButton("View Conflict Report");

        // Add buttons to the panel
        panel.add(scheduleSeatsButton);
        panel.add(scheduleStaffButton);
        panel.add(viewStudentReportButton);
        panel.add(viewStaffReportButton);
        panel.add(viewConflictReportButton);

        scheduleSeatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentSeatAllocation studentSeatAllocation = new StudentSeatAllocation();
                System.out.println("Seats allocated.");
            }
        });

        scheduleStaffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StaffAllocation staffAllocation = new StaffAllocation();
                System.out.println("Staff allocated.");
            }
        });

        viewStudentReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewStudentReport(conn);
            }
        });

        viewStaffReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewStaffReport(conn);
            }
        });

        viewConflictReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewConflictReport(conn);
            }
        });

        optionsFrame.setVisible(true);
    }

    private boolean validateCredentials(Connection conn, String adminid, String given_password) throws SQLException {
        //System.out.println("validate credentials");
        String passQuery = "SELECT password FROM admin WHERE adminid = ?";
        //System.out.println("admin id : "+ adminid);
        try ( PreparedStatement passStmt = conn.prepareStatement(passQuery)) {
            passStmt.setString(1, adminid);
            ResultSet passRs = passStmt.executeQuery();
            String password;
            while (passRs.next()) {
                password = passRs.getString("password");
                if (given_password.equals(password)) {
                    return true;
                }
                //System.out.println("while loop");
                //return false;
            }

        }
        return false;
    }

    public void viewStudentReport(Connection conn) {
        // Create a new JFrame to display the student report
        JFrame frame = new JFrame("Student Report");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a DefaultTableModel for the JTable
        DefaultTableModel model = new DefaultTableModel();

        // Create a JTable and set its model
        JTable table = new JTable(model);

        // Add columns to the model
        model.addColumn("Date");
        model.addColumn("Slot");
        model.addColumn("Roll No");
        model.addColumn("Hall No");
        model.addColumn("Seat No");

        // Query the allocated_seats table
        String query = "SELECT tt.date, tt.slot, all_seats.rollno, all_seats.hallno, all_seats.seatno "
                + "FROM allocated_seats AS all_seats "
                + "INNER JOIN exam_timetable AS tt ON tt.date = all_seats.date ";
        try ( PreparedStatement stmt = conn.prepareStatement(query);  ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String date = rs.getString("date");
                String slot = rs.getString("slot");
                String rollNo = rs.getString("rollno");
                String hallNo = rs.getString("hallno");
                int seatNo = rs.getInt("seatno");

                // Add a row to the table model
                model.addRow(new Object[]{date, slot, rollNo, hallNo, seatNo});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create a scroll pane to display the table
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to the frame
        frame.add(scrollPane);

        // Make the frame visible
        frame.setVisible(true);
    }

    public void viewStaffReport(Connection conn) {
        // Create a new JFrame to display the staff report
        JFrame frame = new JFrame("Staff Report");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a DefaultTableModel for the JTable
        DefaultTableModel model = new DefaultTableModel();

        // Create a JTable and set its model
        JTable table = new JTable(model);

        // Add columns to the model
        model.addColumn("Date");
        model.addColumn("Hall No");
        model.addColumn("Staff ID");
        model.addColumn("Seat Numbers");

        // Query the allocated_staff table
        String query = "SELECT date, hallno, staffid, seatnos FROM allocated_staff";
        try ( PreparedStatement stmt = conn.prepareStatement(query);  ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String date = rs.getString("date");
                String hallNo = rs.getString("hallno");
                String staffId = rs.getString("staffid");
                String seatnos = rs.getString("seatnos");

                // Add a row to the table model
                model.addRow(new Object[]{date, hallNo, staffId, seatnos});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create a scroll pane to display the table
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to the frame
        frame.add(scrollPane);

        // Make the frame visible
        frame.setVisible(true);
    }

    public void viewConflictReport(Connection conn) {
        JFrame frame = new JFrame("Conflict Report");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable conflictTable = new JTable(tableModel);

        tableModel.addColumn("Roll No");
        tableModel.addColumn("Date");
        tableModel.addColumn("Reason");

        // Retrieve data from the database and populate the table
        String query = "SELECT rollno, date, reason FROM conflict_report";
        try ( PreparedStatement Stmt = conn.prepareStatement(query)) {
            ResultSet rs = Stmt.executeQuery();
            while (rs.next()) {
                String rollNo = rs.getString("rollno");
                String date = rs.getString("date");
                String reason = rs.getString("reason");
                tableModel.addRow(new Object[]{rollNo, date, reason});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(conflictTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}

class StudentGUI {

    Connection conn;

    public StudentGUI(Connection conn) {
        this.conn = conn;
        createStudentLoginWindow(conn);
    }

    public void createStudentLoginWindow(Connection conn) {
        JFrame frame = new JFrame("Student Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 200);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.LIGHT_GRAY); // Set background color
        frame.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Adjust padding

        JLabel usernameLabel = new JLabel("Roll No:");
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");

        // Center the "Login" button vertically
        gbc.gridx = 1;
        gbc.gridy = 2;  // Adjust the y-coordinate
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER; // Center both horizontally and vertically
        panel.add(loginButton, gbc);

        // Reset gridwidth and anchor
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                String enteredPassword = new String(password);

                try {
                    // Verify the username and password
                    if (authenticateStudent(conn, username, enteredPassword)) {
                        String rollNo = username; // Assuming the username is the student's roll number.
                        String seatInfo = getSeatInfo(rollNo);
                        if (!seatInfo.equals("Student not found or seat not allocated.")) {
                            createSeatInfoFrame(conn, rollNo, seatInfo, frame);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Student not found or seat not allocated.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Login Failed. Invalid username or password.");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(StudentGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        frame.setVisible(true);
    }

    private boolean authenticateStudent(Connection conn, String rollno, String given_password) throws SQLException {
        //System.out.println("validate credentials");
        String passQuery = "SELECT password FROM student WHERE rollno = ?";
        //System.out.println("admin id : "+ adminid);
        try ( PreparedStatement passStmt = conn.prepareStatement(passQuery)) {
            passStmt.setString(1, rollno);
            ResultSet passRs = passStmt.executeQuery();
            String password;
            while (passRs.next()) {
                password = passRs.getString("password");
                if (given_password.equals(password)) {
                    return true;
                }
                //System.out.println("while loop");
                //return false;
            }

        }
        return false;

    }

    public void updateTableData(Connection conn, String rollNo, DefaultTableModel tableModel) throws SQLException {

        //System.out.println("Rollno:" + rollNo);
        String query = "SELECT tt.date, tt.slot, tt.course_code, tt.course_name, all_seats.hallno, all_seats.seatno "
                + "FROM allocated_seats AS all_seats "
                + "INNER JOIN exam_timetable AS tt ON tt.date = all_seats.date "
                + "WHERE all_seats.rollno = ? "
                + "AND tt.dept = (SELECT dept FROM student WHERE rollno = ?)"
                + "AND tt.year = (SELECT yearofstudy FROM student WHERE rollno = ?)";

        //String query = "SELECT tt.date ,tt.slot ,tt.course_code ,tt.course_name , all_seats.hallno ,all_seats.seatno AS seatno FROM allocated_seats AS all_seats INNER JOIN exam_timetable AS tt ON tt.date = all_seats.date WHERE all_seats.rollno = ? AND tt.dept = (SELECT dept FROM student WHERE rollno = ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, rollNo);
        stmt.setString(2, rollNo);
        stmt.setString(3, rollNo);
        ResultSet rs = stmt.executeQuery();

        tableModel.setRowCount(0); // Clear the existing data in the table
        //System.out.println("outside while");
        while (rs.next()) {
            //System.out.println("inside while");
            Object[] rowData = {
                //rs.getInt("sno"),
                rs.getString("date"),
                rs.getString("slot"),
                rs.getString("course_code"),
                rs.getString("course_name"),
                rs.getString("hallno"),
                //rs.getString("rowno"),
                rs.getInt("seatno")

            };
            tableModel.addRow(rowData);
        }
    }

    public void createSeatInfoFrame(Connection conn, String rollNo, String seatInfo, JFrame loginframe) throws SQLException {
        JFrame frame = new JFrame("Student Seat Info");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        frame.add(panel);

        String[] columnNames = {"Date", "Slot", "Course Code", "Course Name", "Hall No", "Seat No"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create a Back button
        JButton backButton = new JButton("Back");
        panel.add(backButton, BorderLayout.PAGE_END);
        backButton.setPreferredSize(new Dimension(100, 30));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 0, 0); // Adjust top padding
        buttonPanel.add(backButton, gbc);

        panel.add(buttonPanel, BorderLayout.PAGE_END);

        updateTableData(conn, rollNo, tableModel);
        frame.setVisible(true);

        // Add an ActionListener to the Back button to close the current frame
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the current frame
                loginframe.dispose();
                GUI gui = new GUI(conn);
            }
        });
        //System.out.println("before call update table");
        updateTableData(conn, rollNo, tableModel);
        //System.out.println("after call update table");
        frame.setVisible(true);
    }

    public String getSeatInfo(String rollNo) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/examseatingallotment";
        String dbUsername = "root";
        String dbPassword = "Yoha@)04";

        try ( Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            // Assuming you have a table named 'seating_info' to store the seat information.
            String query = "SELECT tt.date ,tt.slot ,tt.course_code ,tt.course_name , all_seats.hallno ,all_seats.seatno AS seatno FROM allocated_seats AS all_seats INNER JOIN exam_timetable AS tt ON tt.date = all_seats.date WHERE all_seats.rollno = ? AND tt.dept = (SELECT dept FROM student WHERE rollno = ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, rollNo);
            stmt.setString(2, rollNo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                //int sno = rs.getInt("sno");
                String date = rs.getString("date");
                String slot = rs.getString("slot");
                String courseCode = rs.getString("course_code");
                String courseName = rs.getString("course_name");
                String hallNumber = rs.getString("hallno");
                //String rowNumber = rs.getString("rowno");
                int seatNumber = rs.getInt("seatno");

                // Build a string with the retrieved information
                StringBuilder seatInfo = new StringBuilder();
                //seatInfo.append("Sno: ").append(sno).append("\n");
                seatInfo.append("Date: ").append(date).append("\n");
                seatInfo.append("Slot: ").append(slot).append("\n");
                seatInfo.append("Course Code: ").append(courseCode).append("\n");
                seatInfo.append("Course Name: ").append(courseName).append("\n");
                seatInfo.append("Hall Number: ").append(hallNumber).append("\n");
                //seatInfo.append("Row Number: ").append(rowNumber).append("\n");
                seatInfo.append("Seat Number: ").append(seatNumber);

                //createSeatInfoFrame(seatInfo.toString()); // Display seat information in a separate frame
                return seatInfo.toString();

            } else {
                return "Student not found or seat not allocated.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "getSeat Info  Error retrieving information.";
        }
    }
}

class StaffGUI {

    Connection conn;

    public StaffGUI(Connection conn) {
        this.conn = conn;
        createStaffLoginWindow(conn);
    }

    public void createStaffLoginWindow(Connection conn) {
        JFrame frame = new JFrame("Staff Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 200);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.LIGHT_GRAY); // Set background color
        frame.add(panel);

        JLabel usernameLabel = new JLabel("Staff Id: ");
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password: ");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");

        panel.setBackground(Color.LIGHT_GRAY); // Set background color
        frame.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Adjust padding

        // Center the "Login" button vertically
        gbc.gridx = 1;
        gbc.gridy = 2;  // Adjust the y-coordinate
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER; // Center both horizontally and vertically
        panel.add(loginButton, gbc);

        // Reset gridwidth and anchor
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                String enteredPassword = new String(password);

                try {
                    // Verify the username and password
                    if (authenticateStaff(conn, username, enteredPassword)) {
                        String staffid = username; // Assuming the username is the student's roll number.
                        String staffInfo = getStaffInfo();
                        if (!staffInfo.equals("Staff not found.")) {
                            createStaffInfoFrame(conn, staffid, staffInfo, frame);
                            //JOptionPane.showMessageDialog(frame, "Login Successful\n" + seatInfo);

                        } else {
                            JOptionPane.showMessageDialog(frame, "Staff not found.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Login Failed. Invalid username or password.");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(StaffGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        frame.setVisible(true);
    }

    private boolean authenticateStaff(Connection conn, String staffid, String given_password) throws SQLException {
        //System.out.println("validate credentials");
        String passQuery = "SELECT password FROM staff WHERE staffid = ?";
        //System.out.println("admin id : "+ adminid);
        try ( PreparedStatement passStmt = conn.prepareStatement(passQuery)) {
            passStmt.setString(1, staffid);
            ResultSet passRs = passStmt.executeQuery();
            String password;
            while (passRs.next()) {
                password = passRs.getString("password");
                if (given_password.equals(password)) {
                    return true;
                }
                //System.out.println("while loop");
                //return false;
            }

        }
        return false;

    }

    public String getStaffInfo() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/examseatingallotment";
        String dbUsername = "root";
        String dbPassword = "Yoha@)04";

        try ( Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            // Assuming you have a table named 'seating_info' to store the seat information.
            //String query = "SELECT tt.date ,tt.slot ,tt.course_code ,tt.course_name , all_seats.hallno ,all_seats.seatno AS seatno FROM allocated_seats AS all_seats INNER JOIN exam_timetable AS tt ON tt.date = all_seats.date WHERE all_seats.rollno = ? AND tt.dept = (SELECT dept FROM student WHERE rollno = ?)";
            String query = "SELECT tt.date, tt.slot, all_staff.staffid, all_staff.hallno, all_staff.seatnos "
                    + "FROM allocated_staff AS all_staff "
                    + "INNER JOIN exam_timetable AS tt ON tt.date = all_staff.date ";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                //int sno = rs.getInt("sno");
                String date = rs.getString("date");
                String slot = rs.getString("slot");
                String staffid = rs.getString("staffid");
                String hallNumber = rs.getString("hallno");
                //String rowNumber = rs.getString("rowno");
                String seatNumbers = rs.getString("seatnos");

                // Build a string with the retrieved information
                StringBuilder staffInfo = new StringBuilder();
                //seatInfo.append("Sno: ").append(sno).append("\n");
                staffInfo.append("Date: ").append(date).append("\n");
                staffInfo.append("Slot: ").append(slot).append("\n");
                staffInfo.append("Staff Id: ").append(staffid).append("\n");
                staffInfo.append("Hall Number: ").append(hallNumber).append("\n");
                //seatInfo.append("Row Number: ").append(rowNumber).append("\n");
                staffInfo.append("Seat Number: ").append(seatNumbers);

                //createSeatInfoFrame(seatInfo.toString()); // Display seat information in a separate frame
                return staffInfo.toString();

            } else {
                return "Staff not found.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return " Error retrieving information.";
        }
    }

    public void createStaffInfoFrame(Connection conn, String staffid, String seatInfo, JFrame loginframe) throws SQLException {
        JFrame frame = new JFrame("Staff Seat Info");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        frame.add(panel);

        String[] columnNames = {"Date", "Slot", "Staff Id", "Hall No", "Seat Numbers"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        //System.out.println("before call update table");
        updateTableData(conn, staffid, tableModel);
        //System.out.println("after call update table");

        // Create a Back button
        JButton backButton = new JButton("Back");
        panel.add(backButton, BorderLayout.PAGE_END);
        backButton.setPreferredSize(new Dimension(100, 30));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 0, 0); // Adjust top padding
        buttonPanel.add(backButton, gbc);

        panel.add(buttonPanel, BorderLayout.PAGE_END);

        updateTableData(conn, staffid, tableModel);
        frame.setVisible(true);

        // Add an ActionListener to the Back button to close the current frame
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the current frame
                loginframe.dispose();
                GUI gui = new GUI(conn);
            }
        });
        frame.setVisible(true);
    }

    public void updateTableData(Connection conn, String staffid, DefaultTableModel tableModel) throws SQLException {

        //System.out.println("Staffid:" + staffid);
        String query = "SELECT DISTINCT tt.date, tt.slot, all_staff.staffid, all_staff.hallno, all_staff.seatnos "
                + "FROM allocated_staff AS all_staff "
                + "INNER JOIN exam_timetable AS tt ON tt.date = all_staff.date "
                + "WHERE all_staff.staffid = ?";

        //String query = "SELECT tt.date ,tt.slot ,tt.course_code ,tt.course_name , all_seats.hallno ,all_seats.seatno AS seatno FROM allocated_seats AS all_seats INNER JOIN exam_timetable AS tt ON tt.date = all_seats.date WHERE all_seats.rollno = ? AND tt.dept = (SELECT dept FROM student WHERE rollno = ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, staffid);
        ResultSet rs = stmt.executeQuery();

        tableModel.setRowCount(0); // Clear the existing data in the table
        //System.out.println("outside while");
        while (rs.next()) {
            //System.out.println("inside while");
            Object[] rowData = {
                //rs.getInt("sno"),
                rs.getString("date"),
                rs.getString("slot"),
                rs.getString("staffid"),
                rs.getString("hallno"),
                //rs.getString("rowno"),
                rs.getString("seatnos")

            };
            tableModel.addRow(rowData);
        }
    }
}

public class ExamSeatingAllotment {

    public static void main(String[] args) throws SQLException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/examseatingallotment";
        String dbUsername = "root";
        String dbPassword = "Yoha@)04";
        Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);

        //StudentSeatAllocation studentSeatAllocation = new StudentSeatAllocation();
        //StaffAllocation staffAllocation = new StaffAllocation();
        ConflictReport conflictReport = new ConflictReport(conn);
        SwingUtilities.invokeLater(() -> {
            // Create an instance of the Admin class to start the application
            GUI gui = new GUI(conn);
        });
    }
}




