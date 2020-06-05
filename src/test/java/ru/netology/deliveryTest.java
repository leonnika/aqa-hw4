package ru.netology;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class deliveryTest {
    private int cityNumber = 5;
    private int dayNumber = 10;
    private int intervalDayDefault = 3;
    private int intervalDelivery = 7;
    private String[] city = {"Москва", "Санкт-Петербург", "Екатеринбург", "Волгоград", "Краснодар"};
    private int randomIndexCity;
    private int randomIndexDay;
    private String randomCity;
    private LocalDate randomDate;
    private LocalDate date = LocalDate.now();
    private String searchStringForListCity;
    int intervalDeliveryForCalendar;


    @BeforeEach
    void init() {
        open("http://localhost:9999");
        randomIndexCity = (int) (Math.random() * cityNumber);
        randomIndexDay = (int) (Math.random() * dayNumber);
        date = date.plusDays(intervalDayDefault);
        randomCity = city[randomIndexCity];
        randomDate = date.plusDays(randomIndexDay);
        intervalDeliveryForCalendar = intervalDelivery - intervalDayDefault - 1;
    }

    @Nested
    public class EmptyFieldsOtions {
        String expected = "Поле обязательно для заполнения";

        @Test
        void sholdNotSentCityEmpty() {
            $("[type='tel'][placeholder='Дата встречи']").click();
            $("[type='tel'][placeholder='Дата встречи']").sendKeys(Keys.SHIFT, Keys.HOME, Keys.DELETE);
            $("[type='tel'][placeholder='Дата встречи']").setValue(randomDate.format(DateTimeFormatter.ofPattern("dd MM yyyy", new Locale("ru"))));
            $("[name='name']").setValue("Иванов Иван");
            $("[name='phone']").setValue("+78787878787");
            $("[role='presentation']").click();
            $$("button").find(exactText("Забронировать")).click();
            $("[class='input__sub']").shouldHave(exactText(expected));
        }

        @Test
        void sholdNotSentDataEmpty() {
            $("[placeholder='Город']").setValue(randomCity);
            $("[type='tel'][placeholder='Дата встречи']").click();
            $("[type='tel'][placeholder='Дата встречи']").sendKeys(Keys.SHIFT, Keys.HOME, Keys.DELETE);
            $("[name='name']").setValue("Иванов Иван");
            $("[name='phone']").setValue("+78787878787");
            $("[role='presentation']").click();
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement date = $("[data-test-id='date']");
            date.$("[class='input__sub']").shouldHave(exactText("Неверно введена дата"));
        }

        @Test
        void sholdNotSentNameEmpty() {
            $("[placeholder='Город']").setValue(randomCity);
            $("[type='tel'][placeholder='Дата встречи']").click();
            $("[type='tel'][placeholder='Дата встречи']").sendKeys(Keys.SHIFT, Keys.HOME, Keys.DELETE);
            $("[type='tel'][placeholder='Дата встречи']").setValue(randomDate.format(DateTimeFormatter.ofPattern("dd MM yyyy", new Locale("ru"))));
            $("[name='phone']").setValue("+78787878787");
            $("[role='presentation']").click();
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement name = $("[data-test-id='name']");
            name.$("[class='input__sub']").shouldHave(exactText(expected));
        }

        @Test
        void sholdNotSentPhoneEmpty() {
            $("[placeholder='Город']").setValue(randomCity);
            $("[type='tel'][placeholder='Дата встречи']").click();
            $("[type='tel'][placeholder='Дата встречи']").sendKeys(Keys.SHIFT, Keys.HOME, Keys.DELETE);
            $("[type='tel'][placeholder='Дата встречи']").setValue(randomDate.format(DateTimeFormatter.ofPattern("dd MM yyyy", new Locale("ru"))));
            $("[name='name']").setValue("Иванов Иван");
            $("[role='presentation']").click();
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement name = $("[data-test-id='phone']");
            name.$("[class='input__sub']").shouldHave(exactText(expected));
        }

        @Test
        void sholdNotSentCheckIsNot() {
            $("[placeholder='Город']").setValue(randomCity);
            $("[type='tel'][placeholder='Дата встречи']").click();
            $("[type='tel'][placeholder='Дата встречи']").sendKeys(Keys.SHIFT, Keys.HOME, Keys.DELETE);
            $("[type='tel'][placeholder='Дата встречи']").setValue(randomDate.format(DateTimeFormatter.ofPattern("dd MM yyyy", new Locale("ru"))));
            $("[name='name']").setValue("Иванов Иван");
            $("[name='phone']").setValue("+78787878787");
            $$("button").find(exactText("Забронировать")).click();
            assertEquals("rgba(255, 92, 92, 1)", $("[data-test-id=agreement]").getCssValue("color"));
        }
    }

    @Nested
    class NameOptions {

        @BeforeEach
        void init() {
            $("[placeholder='Город']").setValue(randomCity);
            $("[type='tel'][placeholder='Дата встречи']").click();
            $("[type='tel'][placeholder='Дата встречи']").sendKeys(Keys.SHIFT, Keys.HOME, Keys.DELETE);
            $("[type='tel'][placeholder='Дата встречи']").setValue(randomDate.format(DateTimeFormatter.ofPattern("dd MM yyyy", new Locale("ru"))));
            $("[name='phone']").setValue("+78787878787");
            $("[role='presentation']").click();
        }

        @Test
        void shouldSentFormDubleName() {
            $("[name='name']").setValue("Иванов Петр-Иван ");
            $$("button").find(exactText("Забронировать")).click();
            $(withText("Успешно!")).waitUntil(visible, 15000);
        }

        @Test
        void shouldSentFormNameLong() {
            $("[name='name']").setValue("Христорождественский Максимилиан");
            $$("button").find(exactText("Забронировать")).click();
            $(withText("Успешно!")).waitUntil(visible, 15000);
        }

        @Test
        void shouldSentFormNameShort() {
            $("[name='name']").setValue("И Я");
            $$("button").find(exactText("Забронировать")).click();
            $(withText("Успешно!")).waitUntil(visible, 15000);
        }

        @Test
        void shouldNotSentFormNameEnglish() {
            $("[name='name']").setValue("Ivanov Ivan");
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement name = $("[data-test-id='name']");
            name.$("[class='input__sub']").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        }

        @Test
        void shouldNotSentFormNameNumber() {
            $("[name='name']").setValue("767688");
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement name = $("[data-test-id='name']");
            name.$("[class='input__sub']").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        }

        @Test
        void shouldNotSentFormNameSpecialSymbols() {
            $("[name='name']").setValue("%^%&^%&%&^");
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement name = $("[data-test-id='name']");
            name.$("[class='input__sub']").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        }

        @Test
            //issue
        void shouldNotSentFormNameOnly() {
            $("[name='name']").setValue("Иванов");
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement name = $("[data-test-id='name']");
            name.$("[class='input__sub']").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        }

        @Test
            //issue
        void shouldNotSentFormNameDifferentCaseLetters() {
            $("[name='name']").setValue("иВаноВ иВАН");
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement name = $("[data-test-id='name']");
            name.$("[class='input__sub']").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        }

        @Test
            //issue
        void shouldNotSentFormNameDubleLong() {
            $("[name='name']").setValue("Иванова-Перевозкина-Пограничникова Александра Виктория Валентина");
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement name = $("[data-test-id='name']");
            name.$("[class='input__sub']").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        }
    }

    @Nested
    class PhoneOptions {
        private String expected;

        @BeforeEach
        void init() {
            $("[placeholder='Город']").setValue(randomCity);
            $("[type='tel'][placeholder='Дата встречи']").click();
            $("[type='tel'][placeholder='Дата встречи']").sendKeys(Keys.SHIFT, Keys.HOME, Keys.DELETE);
            $("[type='tel'][placeholder='Дата встречи']").setValue(randomDate.format(DateTimeFormatter.ofPattern("dd MM yyyy", new Locale("ru"))));
            $("[name='name']").setValue("Иванов Иван");
            $("[role='presentation']").click();
            expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        }

        @Test
        void shouldNotSentFormPhoneSpecialSymbols() {
            $("[name='phone']").setValue("^&&&");
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement name = $("[data-test-id='phone']");
            name.$("[class='input__sub']").shouldHave(exactText(expected));
        }

        @Test
        void shouldNotSentFormPhoneFistSymbolNotPlus() {
            $("[name='phone']").setValue("89898989890");
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement name = $("[data-test-id='phone']");
            name.$("[class='input__sub']").shouldHave(exactText(expected));
        }

        @Test
        void shouldNotSentFormPhoneNot11element() {
            $("[name='phone']").setValue("898989");
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement name = $("[data-test-id='phone']");
            name.$("[class='input__sub']").shouldHave(exactText(expected));
        }

        @Test
            //issue
        void shouldNotSentFormPhoneFist0() {
            $("[name='phone']").setValue("+08989898909");
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement name = $("[data-test-id='phone']");
            name.$("[class='input__sub']").shouldHave(exactText(expected));
        }
    }

    @Nested
    class CityOptions {
        private String expected;

        @BeforeEach
        void init() {
            $("[type='tel'][placeholder='Дата встречи']").click();
            $("[type='tel'][placeholder='Дата встречи']").sendKeys(Keys.SHIFT, Keys.HOME, Keys.DELETE);
            $("[type='tel'][placeholder='Дата встречи']").setValue(randomDate.format(DateTimeFormatter.ofPattern("dd MM yyyy", new Locale("ru"))));
            $("[name='name']").setValue("Иванов Иван");
            $("[name='phone']").setValue("+78787878787");
            $("[role='presentation']").click();
        }

        @Test
        void shouldNotSentFormCityIsNotList() {
            $("[placeholder='Город']").setValue("Сургут");
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement city = $("[data-test-id='city']");
            city.$("[class='input__sub']").shouldHave(exactText("Доставка в выбранный город недоступна"));
        }

        @Test
        void shouldNotSentFormCityNumber() {
            $("[placeholder='Город']").setValue("565757");
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement city = $("[data-test-id='city']");
            city.$("[class='input__sub']").shouldHave(exactText("Доставка в выбранный город недоступна"));
        }

        @Test
        void shouldNotSentFormCitySpecialSymbols() {
            $("[placeholder='Город']").setValue("#$#$#$");
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement city = $("[data-test-id='city']");
            city.$("[class='input__sub']").shouldHave(exactText("Доставка в выбранный город недоступна"));
        }
    }

    @Nested
    class DataOptions {
        private String expected;

        @BeforeEach
        void init() {
            $("[placeholder='Город']").setValue(randomCity);
            $("[name='name']").setValue("Иванов Иван");
            $("[name='phone']").setValue("+78787878787");
            $("[role='presentation']").click();
        }

        @Test
        void shouldNotSentFormDataLetters() {
            $("[type='tel'][placeholder='Дата встречи']").click();
            $("[type='tel'][placeholder='Дата встречи']").sendKeys(Keys.SHIFT, Keys.HOME, Keys.DELETE);
            $("[type='tel'][placeholder='Дата встречи']").setValue("енен");
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement date = $("[data-test-id='date']");
            date.$("[class='input__sub']").shouldHave(exactText("Неверно введена дата"));
        }

        @Test
        void shouldNotSentFormDataLessPossible() {
            date = LocalDate.now();
            $("[type='tel'][placeholder='Дата встречи']").click();
            $("[type='tel'][placeholder='Дата встречи']").sendKeys(Keys.SHIFT, Keys.HOME, Keys.DELETE);
            $("[type='tel'][placeholder='Дата встречи']").setValue(date.format(DateTimeFormatter.ofPattern("dd MM yyyy", new Locale("ru"))));
            $$("button").find(exactText("Забронировать")).click();
            SelenideElement date = $("[data-test-id='date']");
            date.$("[class='input__sub']").shouldHave(exactText("Заказ на выбранную дату невозможен"));
        }

    }

    @Test
    void shouldSentDeliveryAllValid() {
        $("[placeholder='Город']").setValue(randomCity);
        $("[type='tel'][placeholder='Дата встречи']").click();
        $("[type='tel'][placeholder='Дата встречи']").sendKeys(Keys.SHIFT, Keys.HOME, Keys.DELETE);
        $("[type='tel'][placeholder='Дата встречи']").setValue(randomDate.format(DateTimeFormatter.ofPattern("dd MM yyyy", new Locale("ru"))));
        $("[name='name']").setValue("Иванов Иван");
        $("[name='phone']").setValue("+78787878787");
        $("[role='presentation']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Успешно!")).waitUntil(visible, 15000);
    }

    @Test
    void shouldSentUseListCity() {
        searchStringForListCity = "ка";
        $("[placeholder='Город']").setValue(searchStringForListCity);
        $("[class='popup popup_direction_bottom-left popup_target_anchor popup_size_m popup_visible popup_height_adaptive popup_theme_alfa-on-white input__popup']").waitUntil(visible, 15000);
        SelenideElement list = $("[class='popup popup_direction_bottom-left popup_target_anchor popup_size_m popup_visible popup_height_adaptive popup_theme_alfa-on-white input__popup']");
        list.$(withText(searchStringForListCity)).click();
        $("[type='tel'][placeholder='Дата встречи']").click();
        $("[type='tel'][placeholder='Дата встречи']").sendKeys(Keys.SHIFT, Keys.HOME, Keys.DELETE);
        $("[type='tel'][placeholder='Дата встречи']").setValue(randomDate.format(DateTimeFormatter.ofPattern("dd MM yyyy", new Locale("ru"))));
        $("[name='name']").setValue("Иванов Иван");
        $("[name='phone']").setValue("+78787878787");
        $("[role='presentation']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Успешно!")).waitUntil(visible, 15000);
    }

    @Test
    void shouldSentUseDateFromCalendar() {
        $("[placeholder='Город']").setValue(randomCity);
        $("[name='phone']").setValue("+78787878787");
        $("[type='tel'][placeholder='Дата встречи']").click();
        $("[class='icon-button icon-button_size_m icon-button_theme_alfa-on-white']").click();
        $("[class='popup popup_direction_bottom-left popup_target_anchor popup_size_s popup_visible popup_padded popup_theme_alfa-on-white']").waitUntil(visible, 4500);
        $$("[data-day]").first(intervalDeliveryForCalendar).last().click();
        $("[role='presentation']").click();
        $("[name='name']").setValue("Иванов Иван");
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Успешно!")).waitUntil(visible, 15000);
    }
}
