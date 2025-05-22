package Topic1;

import java.sql.*;

public class JDBCExample {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "12345";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();

            // вывод в консоль количество контрагентов
            String countQuery = "SELECT COUNT(*) FROM organizations;";
            ResultSet rs1 = statement.executeQuery(countQuery);
            if (rs1.next()) {
                System.out.println("Количество контрагентов всего: " + rs1.getInt(1));
            }

            System.out.println("\nГруппирование по типу юридического лица:");
            // группировка по типу юрлица
            String groupQuery = """
                SELECT org_type,
                COUNT(*) AS count,
                COALESCE(SUM(contracts.total_amount), 0) AS total_payment
                FROM organizations
                LEFT JOIN contracts ON organizations.id = contracts.organization_id
                GROUP BY org_type;
            """;
            ResultSet rs2 = statement.executeQuery(groupQuery);
            while (rs2.next()) {
                String type = rs2.getString("org_type");
                int count = rs2.getInt("count");
                double total = rs2.getDouble("total_payment");
                System.out.printf("Тип: %-10s | Кол-во: %d | Сумма выплат: %.2f\n", type, count, total);
            }

            System.out.println("\nСписок о всех контрагентах:");
            // вывод всех контрагентов
            String fullListQuery = "SELECT * FROM organizations;";
            ResultSet rs3 = statement.executeQuery(fullListQuery);
            while (rs3.next()) {
                String name = rs3.getString("name");
                String orgType = rs3.getString("org_type").equalsIgnoreCase("legal") ? "юридическое лицо" : "физическое лицо";
                String residency = rs3.getString("residency").equalsIgnoreCase("RF") ? "Россия" : "иностранное";
                String address = rs3.getString("address");
                String inn = rs3.getString("inn") == null || rs3.getString("inn").isBlank() ? "—" : rs3.getString("inn");
                String email = rs3.getString("email");
                String phone = rs3.getString("phone");

                // вывод
                System.out.printf("%s / %s / %s / %s / %s / %s / %s\n",
                        name, orgType, residency, "—", address, email, phone);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
