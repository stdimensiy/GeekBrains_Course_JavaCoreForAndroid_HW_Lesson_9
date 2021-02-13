package ru.geekbrains.JavaCoreForAndroid;

import java.sql.*;
import java.util.Scanner;

/**
 * Сourse: java core for android
 * Faculty of Geek University Android Development
 *
 * @Author Student Dmitry Veremeenko aka StDimensiy
 * Group 24.12.2020
 * <p>
 * HomeWork for lesson 9
 * Created 11.02.2021
 * v1.0
 */
public class Lesson9 {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement ps;
    private static final String primaryLoad = "CREATE TABLE IF NOT EXISTS \"Cats\" (\"CatId\"INTEGER NOT NULL UNIQUE,\"Name\"TEXT NOT NULL,\"Species\"TEXT NOT NULL,\"Color\"TEXT,\"Weight\"INTEGER,\"YearOfBirth\"INTEGER,PRIMARY KEY(\"CatId\" AUTOINCREMENT))";

    private static final String startSingleQ = "INSERT INTO Cats(Name, Species, Color, Weight, YearOfBirth) VALUES ('Алиса', 'Абессинская', 'ruddy', 2700, 2020)";
    private static final String preparedStrS = "INSERT INTO Cats(Name, Species, Color, Weight, YearOfBirth) VALUES (?, ?, ?, ?, ?)";
    private static final String countBString = "SELECT COUNT(*) FROM Cats";

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        connect();
        statement = connection.createStatement();
        statement.execute(primaryLoad);
        //System.out.println(statement.executeUpdate(startSingleQ));
        int actionNumber;
        statusDB(5);
        do {
            // ststusDB(5);
            do {
                System.out.println("Выберите действие:");
                System.out.println("1 - Посмотреть весь список котов");
                System.out.println("2 - получить информацию о коте по его ID");
                System.out.println("3 - Добавить нового кота в базу");
                System.out.println("4 - удалить кота из базы");
                System.out.println("5 - Редактировать данные кота в базе");
                System.out.println("6 - получить выборку по Году рождения");
                System.out.println("7 - ВЫХОД из программы");
                actionNumber = SCANNER.nextInt();
            } while (!(actionNumber > 0 && actionNumber <= 7));

            switch (actionNumber) {
                case (1):
                    viewEntireList();
                    break;
                case (2):
                    viewCatInfoByID();
                    break;
                case (3):
                    prepareAddCatToDB();
                    break;
                case (4):
                    prepareDeleteCatFromDB();
                    break;
                case (5):
                    prepareUpdateCatFromDB();
                    break;
                case (6):
                    prepareGetSelectionFromDB();
            }
        } while (actionNumber != 7);
        close();
    }

    private static void viewEntireList() throws SQLException {
        ResultSet rs = statement.executeQuery("select * from Cats;");
        System.out.println("_______________________________________________________________________________________________");
        String format = "|%1$-7.6s|%2$-16.15s|%3$-24.22s|%4$-16.14s|%5$-10.10s|%6$-15.13s|\n";
        System.out.format(format, "  ID", "    Кличка", "     Порода", "     Окрас", " Вес, (гр)", " Год рождения");
        System.out.println("===============================================================================================");
        while (rs.next()) {
            System.out.format(format,
                    rs.getInt("CatId"),
                    rs.getString("Name"),
                    rs.getString("Species"),
                    rs.getString("Color"),
                    rs.getInt("Weight"),
                    rs.getInt("YearOfBirth"));
        }
        System.out.println("-----------------------------------------------------------------------------------------------");
    }

    private static void viewCatInfoByID() throws SQLException {
        int catId;
        int maxCatId = statement.executeQuery("SELECT COUNT(*)FROM Cats;").getInt(1);
        System.out.println("Введите идентификатор кота для просмотра информации о нем.");
        System.out.println("Идентификатор не должен быть меньше 0 и больше " + maxCatId);
        System.out.println("Введите 0 ля выхода из данного режима");
        do {
            catId = SCANNER.nextInt();
        } while (catId < 0 && catId > maxCatId);
        if (catId > 0) {
            Cat cat = createCatFromDB(catId);
            System.out.println(cat);
        }
    }

    private static Cat createCatFromDB(int catId) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM Cats WHERE CatId = " + catId + ";");
        Cat cat = new Cat();
        cat.setCatId(rs.getInt("CatId"));
        cat.setName(rs.getString("Name"));
        cat.setSpecies(rs.getString("Species"));
        cat.setColor(rs.getString("Color"));
        cat.setWeight(rs.getInt("Weight"));
        cat.setYearOfBirth(rs.getInt("YearOfBirth"));
        return cat;
    }

    private static void prepareAddCatToDB() throws SQLException {
        System.out.println("Режим добавления нового кота (кошки) в базу данных");
        System.out.println("Ввод основных характеристик----------");
        System.out.println("Введите кличку животного...\n(поле не должно остаться пустым)");
        String prepNameCat;
        do {
            prepNameCat = SCANNER.nextLine();
        } while (prepNameCat.equals(""));
        System.out.println("Введите породу животного...\n(Если не знаете нажмите Enter (пропустить) Будет присвоено значение \"Дворовый\").");
        String prepSpeciesCat = SCANNER.nextLine();
        System.out.println("Введите окрас животного... \n(поле не должно остаться пустым)");
        String prepColorCat;
        do {
            prepColorCat = SCANNER.nextLine();
        } while (prepColorCat.equals(""));
        System.out.println("Введите вес животного... \n(Если не знаете, введите 0 и нажмите Enter (пропустить))");
        int prepWeightCat;
        do {
            prepWeightCat = SCANNER.nextInt();
        } while (prepWeightCat < 0);
        System.out.println("Введите год рождения... \n(Если не знаете, введите 0 и нажмите Enter (пропустить))");
        int prepYearOfBirthCat;
        do {
            prepYearOfBirthCat = SCANNER.nextInt();
        } while (prepYearOfBirthCat < 0);
        // подготовка данных закончена
        Cat cat = new Cat();
        cat.setName(prepNameCat);
        cat.setSpecies(prepSpeciesCat);
        cat.setColor(prepColorCat);
        cat.setWeight(prepWeightCat);
        cat.setYearOfBirth(prepYearOfBirthCat);
        System.out.println("Новая запись добавлена в базу данных: " + (addCatToDB(cat) ? " ОК !" : "ERORR !!!"));
        statusDB(5);
    }

    public static Boolean addCatToDB(Cat cat) throws SQLException {
        String stSQ = "INSERT INTO Cats(Name, Species, Color, Weight, YearOfBirth) VALUES ('" +
                cat.getName() + "', '" +
                cat.getSpecies() + "', '" +
                cat.getColor() + "', " +
                cat.getWeight() + ", " +
                cat.getYearOfBirth() + ");";
        int rs = statement.executeUpdate(stSQ);
        //System.out.println("ответ: " + rs);
        return rs >= 1;
    }

    private static void prepareDeleteCatFromDB() throws SQLException {
        int catId;
        int maxCatId = statement.executeQuery("SELECT COUNT(*)FROM Cats;").getInt(1);
        System.out.println("Режим удаления кота (кошки) из базы данных");
        System.out.println("Идентификатор не должен быть меньше 0 и больше " + maxCatId);
        System.out.println("Введите 0 ля выхода из данного режима");
        do {
            catId = SCANNER.nextInt();
        } while (catId < 0 && catId > maxCatId);
        if (catId > 0) {
            Cat cat = new Cat();
            cat.setCatId(catId);
            System.out.println("Удаление записи ID: " + catId + " из базы данных: " + (deleteCatFromDB(cat) ? " ОК !" : "ERORR !!!"));
            System.out.println();
            statusDB(5);
        }
    }

    private static boolean deleteCatFromDB(Cat cat) throws SQLException {
        String stSQ = "DELETE FROM Cats WHERE CatId = " + cat.getCatId() + ";";
        int rs = statement.executeUpdate(stSQ);
        //System.out.println("ответ2: " + rs);
        return rs >= 1;
    }

    private static void prepareUpdateCatFromDB() throws SQLException {
        int catId;
        int maxCatId = statement.executeQuery("SELECT COUNT(*)FROM Cats;").getInt(1);
        System.out.println("Режим редактирования данных кота (кошки) в базе данных");
        System.out.println("----- Введите уникальный идентификатор животного.");
        System.out.println("Идентификатор не должен быть меньше 0 и больше " + maxCatId);
        System.out.println("Введите 0 для выхода из данного режима.");
        do {
            catId = SCANNER.nextInt();
        } while (catId < 0 && catId > maxCatId);
        System.out.println("Текущие значения редактируемого элемента:");
        Cat cat = createCatFromDB(catId);
        System.out.println(cat);
        System.out.println("Редактирование основных характеристик----------");
        System.out.println("Кличка (тек. значение:" + cat.getName() + ") если нет изменений жми Enter");
        String prepNameCat;
        do {
            prepNameCat = SCANNER.nextLine();
        } while (prepNameCat.equals(""));
        System.out.println("Порода (тек. значение:" + cat.getSpecies() + ") если нет изменений жми Enter");
        String prepSpeciesCat = SCANNER.nextLine();
        System.out.print("Окрас (тек. значение:" + cat.getColor() + ") если нет изменений жми Enter");
        String prepColorCat = SCANNER.nextLine();
        System.out.print("Вес животного (гр.) (тек. значение:" + cat.getWeight() + ") если нет изменений ставь 0 и жми Enter");
        int prepWeightCat;
        do {
            prepWeightCat = SCANNER.nextInt();
        } while (prepWeightCat < 0);
        System.out.print("\nГод рождения животного (гр.) (тек. значение:" + cat.getWeight() + ") если нет изменений ставь 0 и жми Enter");
        int prepYearOfBirthCat;
        do {
            prepYearOfBirthCat = SCANNER.nextInt();
        } while (prepYearOfBirthCat < 0);
        //прием данных закончен
        if (!prepNameCat.equals("")) cat.setName(prepNameCat);
        if (!prepSpeciesCat.equals("")) cat.setSpecies(prepSpeciesCat);
        if (!prepColorCat.equals("")) cat.setColor(prepColorCat);
        if (prepWeightCat != 0) cat.setWeight(prepWeightCat);
        if (prepYearOfBirthCat != 0) cat.setYearOfBirth(prepYearOfBirthCat);
        // подготовка данных закончена передаем значения в метод записи изменений в базу
        System.out.println("Измененное состояние записи " + catId);
        System.out.println(cat);
        System.out.println("Сохранить внесенные изменения 1 - да / 0 - нет");
        int saveChanges;
        do {
            saveChanges = SCANNER.nextInt();
        } while (!(saveChanges == 1 || saveChanges == 0));
        if(saveChanges ==1){
            System.out.println("Редактирование записи ID: " + catId + " из базы данных: " + (updateCatFromDB(cat) ? " ОК !" : "ERORR !!!"));
        } else {
            System.out.println(" Отказ по инициативе пользователя.\n Редактирование записи ID: " + catId + " Не произведено !!!");
        }
        statusDB(5);
    }

    private static boolean updateCatFromDB(Cat cat) throws SQLException {
        String stSQ = "UPDATE Cats SET name = '" + cat.getName() + "', Species = '" + cat.getSpecies() + "', Color = '" + cat.getColor() + "', Weight = " + cat.getWeight() + ", YearOfBirth = " + cat.getYearOfBirth() + " WHERE CatId = " + cat.getCatId() + ";";
        int rs = statement.executeUpdate(stSQ);
        System.out.println("ответ3: " + rs);
        return rs >= 1;
    }


    private static void prepareGetSelectionFromDB() throws SQLException {
        System.out.println("Режим получения выборки записей из базы данных по критерию : год рождения.");
        System.out.println("Введите 0 для выхода из данного режима.");
        System.out.println("----- Введите дату (год) начала периода выборки (включительно)");
        int yearStart;
        int yearStop;
        do {
            yearStart = SCANNER.nextInt();
        } while (!(yearStart > 1980 && yearStart < 2022) && yearStart != 0);
        if (yearStart == 0) return;
        System.out.println("----- Введите дату (год) окончания периода выборки (включительно).");
        do {
            yearStop = SCANNER.nextInt();
        } while (!(yearStop >= yearStart && yearStop <= 2021) && yearStop != 0);
        if (yearStop == 0) return;
        GetSelectionFromDB(yearStart, yearStop);
    }

    private static void GetSelectionFromDB(int yearStart, int yearStop) throws SQLException {
        System.out.println("Выборка значений базы данных по критерию : год рождения.");
        System.out.println("за период: с " + yearStart + "г., по " + yearStop + "г. ");
        ResultSet rs = statement.executeQuery("SELECT * FROM Cats WHERE YearOfBirth>=" + yearStart + " AND YearOfBirth<=" + yearStop + "  ORDER BY YearOfBirth ASC");
        System.out.println("_______________________________________________________________________________________________");
        String format = "|%1$-7.6s|%2$-16.15s|%3$-24.22s|%4$-16.14s|%5$-10.10s|%6$-15.13s|\n";
        System.out.format(format, "  ID", "    Кличка", "     Порода", "     Окрас", " Вес, (гр)", " Год рождения");
        System.out.println("===============================================================================================");
        while (rs.next()) {
            System.out.format(format,
                    rs.getInt("CatId"),
                    rs.getString("Name"),
                    rs.getString("Species"),
                    rs.getString("Color"),
                    rs.getInt("Weight"),
                    rs.getInt("YearOfBirth"));
        }
        System.out.println("-----------------------------------------------------------------------------------------------");
    }

    //Метод, запускается при старте, предоставляет общее состояние базы данных и выводит форматированную таблицу определенного количества котов
    private static void statusDB(int numberOfRecords) throws SQLException {
        System.out.println("Количество записей в базе: " + statement.executeQuery("SELECT COUNT(*)FROM Cats ;").getInt(1));
        System.out.println("В таблице отражены последние " + numberOfRecords + " записей.");
        ResultSet rs = statement.executeQuery("SELECT * FROM Cats ORDER BY CatId DESC LIMIT " + numberOfRecords + ";");
        System.out.println("_______________________________________________________________________________________________");
        String format = "|%1$-7.6s|%2$-16.15s|%3$-24.22s|%4$-16.14s|%5$-10.10s|%6$-15.13s|\n";
        System.out.format(format, "  ID", "    Кличка", "     Порода", "     Окрас", " Вес, (гр)", " Год рождения");
        System.out.println("===============================================================================================");
        while (rs.next()) {
            System.out.format(format,
                    rs.getInt("CatId"),
                    rs.getString("Name"),
                    rs.getString("Species"),
                    rs.getString("Color"),
                    rs.getInt("Weight"),
                    rs.getInt("YearOfBirth"));
        }
        System.out.println("-----------------------------------------------------------------------------------------------");
    }

    private static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:lesson9.db");
        } catch (ClassNotFoundException e) {
            System.out.println("Напиши нормально имя класса!!!!");
        } catch (SQLException thr) {
            thr.printStackTrace();
        }
    }

    private static void close() throws SQLException {
        connection.close();
        SCANNER.close();
    }
}
