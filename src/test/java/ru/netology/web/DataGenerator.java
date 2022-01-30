package ru.netology.web;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {


    private DataGenerator() {
    }

    public static class Registration {
        private static Faker faker = new Faker(new Locale("ru"));

        public static UserInfo generateUser() {
            return new UserInfo(generateCity(), generateName(), generatePhone());
        }

        public static String generateCity() {
            String[] cities = new String[]{"Москва", "Уфа", "Омск", "Самара", "Казань"};
            int itemIndex = (int) (Math.random() * cities.length);
            return cities[itemIndex];
        }

        public static String generateFakeCity() {
            String[] cities = new String[]{"Ожск", "Лахта", "Торецк", "Выдринск", "Счастьинск"};
            int itemIndex = (int) (Math.random() * cities.length);
            return cities[itemIndex];
        }

        public static String generateDate(int daysToAdd) {
            return LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }

        public static String generateName() {
            return faker.name().lastName() + " " + faker.name().firstName();
        }

        public static String generateFakeName() {
            Faker faker = new Faker(new Locale("en"));
            return faker.name().lastName() + " " + faker.name().firstName();
        }

        public static String generatePhone() {
            return faker.phoneNumber().phoneNumber();
        }
    }

}
