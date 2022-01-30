package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.web.DataGenerator.Registration.*;

public class CardDeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
    }

    @Test
    void shouldSendFormValid() {
        UserInfo userInfo = generateUser();
        $("[data-test-id=city] input").setValue(userInfo.getCity());
        $(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".calendar-input input").setValue(generateDate(8));
        $("[data-test-id=name] input").setValue(userInfo.getName());
        $("[data-test-id=phone] input").setValue(userInfo.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $(withText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id=success-notification]")
                .shouldHave(exactText("Успешно! Встреча успешно запланирована на " + generateDate(8)));
        $(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".calendar-input input").setValue(generateDate(9));
        $(".button").click();
        $(withText("Необходимо подтверждение")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(".button__text").click();
        $("[data-test-id=success-notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Успешно! Встреча успешно запланирована на " + generateDate(9)));
    }

    @Test
    void shouldGetErrorSendFormWithInvalidSurname() {
        UserInfo user = generateUser();
        $("[data-test-id=city] input").setValue(user.getCity());
        $(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".calendar-input input").setValue(generateDate(8));
        $("[data-test-id=name] input").setValue(generateFakeName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name] .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldGetErrorSendFormWithInvalidCity() {
        $("[data-test-id=city] input").setValue(generateFakeCity());
        $(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".calendar-input input").setValue(generateDate(8));
        $("[data-test-id=name] input").setValue(generateName());
        $("[data-test-id=phone] input").setValue(generateUser().getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=city] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldGetErrorSendFormWithInvalidDate() {
        $("[data-test-id=city] input").setValue(generateUser().getCity());
        $("[data-test-id=date] input").setValue(generateDate(-3));
        $("[data-test-id=name] input").setValue(generateName());
        $("[data-test-id=phone] input").setValue(generateUser().getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=date] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldGetErrorSendFormWithEmptyName() {
        $("[data-test-id=city] input").setValue(generateUser().getCity());
        $(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".calendar-input input").setValue(generateDate(8));
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue(generateUser().getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldGetErrorSendFormWithEmptyNumber() {
        $("[data-test-id=city] input").setValue(generateUser().getCity());
        $(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".calendar-input input").setValue(generateDate(8));
        $("[data-test-id=name] input").setValue(generateName());
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=phone] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSendFormWithoutCheckbox() {
        $("[data-test-id=city] input").setValue(generateUser().getCity());
        $(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".calendar-input input").setValue(generateDate(8));
        $("[data-test-id=name] input").setValue(generateName());
        $("[data-test-id=phone] input").setValue(generateUser().getPhone());
        $(".button").click();
        $("[data-test-id='agreement'].input_invalid .checkbox__text")
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}
