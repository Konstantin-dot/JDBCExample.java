package ru.synergy.db;

import java.sql.*;

public class JDBCExample {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "12345";

        try (Connection connection = DriverManager.getConnection(
                url, user, password
        )) {
            Statement statement = connection.createStatement();



            String createStudentsTable = """
                    CREATE TABLE IF NOT EXISTS Students (
                    id serial primary key,
                    name varchar(100),
                    age int
                    );""";
            statement.execute(createStudentsTable);

            String insertStudents = """
                    INSERT INTO students (name, age) values
                    ('Alice', 22),
                    ('Bob', 52);""";

            System.out.println(statement.executeUpdate(insertStudents));

            String selectAllstudents = "SELECT * FROM students;";

            ResultSet resultSet = statement.executeQuery(selectAllstudents);


            while (resultSet.next()){
                int id = resultSet.getInt("id");
                int idByColumnNum = resultSet.getInt(1);

                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");

                System.out.printf("Студент номер %d(%d) по имени %s, возраст %d\n",
                        id, idByColumnNum, name, age);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
