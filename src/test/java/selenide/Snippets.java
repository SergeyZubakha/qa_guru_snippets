package selenide;

import com.codeborne.selenide.*;
import org.openqa.selenium.*;

import java.io.*;
import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

// this is not a full list, just the most common
public class Snippets {

  void browser_command_examples() {

    open("https://google.com");
    open("/customer/orders");     // -Dselenide.baseUrl=http://123.23.23.1
    open("/", AuthenticationType.BASIC,
            new BasicAuthCredentials("", "user", "password")); // авторизация на сайте, basic

    Selenide.back(); //кнопка назад в браузере
    Selenide.refresh(); //оюновить страницу в браузере

    Selenide.clearBrowserCookies();
    Selenide.clearBrowserLocalStorage();
    executeJavaScript("sessionStorage.clear();"); // no Selenide command for this yet

    Selenide.confirm(); // OK in alert dialogs по типу как Basic авторизация
    Selenide.dismiss(); // Cancel in alert dialogs по типу как Basic авторизация

    Selenide.closeWindow(); // close active tab закрывает текущую вкладку
    Selenide.closeWebDriver(); // close browser completely закрывает весь браузер

    Selenide.switchTo().frame("ID или класс frame"); // и дальше искать внуть это фрейма
    Selenide.switchTo().defaultContent(); // return from frame back to the main DOM

    Selenide.switchTo().window("The Internet"); //можно по индекс первая вкладка 0 и тд
    
// созадние куки
    var cookie = new Cookie("foo", "bar");
    WebDriverRunner.getWebDriver().manage().addCookie(cookie);
// иногда после этого еще нужно сделать  Selenide.refresh();

  }

  void selectors_examples() {
    $("div").click();
    element("div").click(); // элемент синоним $ 

    $("div", 2).click(); // the third div

    $x("//h1/div").click();
    $(byXpath("//h1/div")).click();

    $(byText("full text")).click(); //ищет по целому тексту
    $(withText("ull tex")).click(); //ищет по куску текста

    $(byTagAndText("div", "full text")); // ищет по тегу и текстом например див такой-от текст такой-то
    $(withTagAndText("div", "ull text"));

    $("").parent();
    $("").sibling(1); //вниз по одной ветке (сестры) в скобках идекс
    $("").preceding(1); // вверх
    $("").closest("div"); // ищет вверх по дереву ближайший див
    $("").ancestor("div"); // the same as closest
    $("div:last-child");

    $("div").$("h1").find(byText("abc")).click();
    // very optional
    $(byAttribute("abc", "x")).click();
    $("[abc=x]").click();

    $(byId("mytext")).click();
    $("#mytext").click();

    $(byClassName("red")).click();
    $(".red").click();
  }

  void actions_examples() {
    $("").click();
    $("").doubleClick();
    $("").contextClick(); //правая кнопка мыши

    $("").hover(); //поднести мышку и не нажимать (всплывающее окно)
    
    
  //команды с клавиатуры
    $("").setValue("text");
    $("").append("text");// добавить текст (сначала setValue затем append)
    $("").clear(); 
    $("").setValue(""); // clear

    $("div").sendKeys("c"); // hotkey c on element
    actions().sendKeys("c").perform(); //находимся в окне и нажимаем горячую клавишу 
    actions().sendKeys(Keys.chord(Keys.CONTROL, "f")).perform(); // Ctrl + F
    $("html").sendKeys(Keys.chord(Keys.CONTROL, "f"));

    $("").pressEnter();
    $("").pressEscape();
    $("").pressTab();


    // complex actions with keybord and mouse, example
    actions().moveToElement($("div")).clickAndHold().moveByOffset(300, 200).release().perform(); //типичный драг анд дроп

    // old html actions don't work with many modern frameworks Выбор у элементов dropDown и radioBox
    $("").selectOption("dropdown_option");
    $("").selectRadio("radio_options");

  }

  void assertions_examples() {
    $("").shouldBe(visible);
    $("").shouldNotBe(visible);
    $("").shouldHave(text("abc"));
    $("").shouldNotHave(text("abc"));
    $("").should(appear);
    $("").shouldNot(appear);
    $$( cssSelector: "sdfsdf") .findBy ( condition: visible) .shouldHave ( ...condition: text ( text: "abc"));//если есть 2 одинаковых элемента и один из них невидимый


    //longer timeouts задержка в каком-то конкретном месте нужно подождть (медленный стенд)
    $("").shouldBe(visible, Duration.ofSeconds(30));

  }

  void conditions_examples() {
    $("").shouldBe(visible); // что-то появилось  
    $("").shouldBe(hidden); // что-то исчезло

    $("").shouldHave(text("abc")); //есть текст
    $("").shouldHave(exactText("abc"));
    $("").shouldHave(textCaseSensitive("abc"));
    $("").shouldHave(exactTextCaseSensitive("abc"));
    $("").should(matchText("[0-9]abc$"));

    $("").shouldHave(cssClass("red")); //проверяет что таокой класс есть 
    $("").shouldHave(cssValue("font-size", "12")); //или цвет 

    $("").shouldHave(value("25")); //проверка что в инпут боксе отображается такой текст т.е. если инпут то не shouldHave(text) а shouldHave(value)
    $("").shouldHave(exactValue("25"));
    $("").shouldBe(empty);

    $("").shouldHave(attribute("disabled"));
    $("").shouldHave(attribute("name", "example"));
    $("").shouldHave(attributeMatching("name", "[0-9]abc$"));

    $("").shouldBe(checked); // for checkboxes проверка что чекбокс выбран

    // Warning! Only checks if it is in DOM, not if it is visible! You don't need it in most tests!
    $("").should(exist); //проверяет что элемнт есть в ДОМЕ но не тоже самое что ВИДИМЫЙ!

    // Warning! Checks only the "disabled" attribute! Will not work with many modern frameworks
    $("").shouldBe(disabled);
    $("").shouldBe(enabled);
  }

  void collections_examples() {

    $$("div"); // does nothing!

    $$x("//div"); // by XPath

    // selections
    $$("div").filterBy(text("123")).shouldHave(size(1));
    $$("div").excludeWith(text("123")).shouldHave(size(1));

    $$("div").first().click();
    elements("div").first().click();
    // $("div").click();
    $$("div").last().click();
    $$("div").get(1).click(); // the second! (start with 0)
    $("div", 1).click(); // same as previous
    $$("div").findBy(text("123")).click(); //  finds first

    // assertions
    $$("").shouldHave(size(0));
    $$("").shouldBe(CollectionCondition.empty); // the same

    $$("").shouldHave(texts("Alfa", "Beta", "Gamma"));
    $$("").shouldHave(exactTexts("Alfa", "Beta", "Gamma"));

    $$("").shouldHave(textsInAnyOrder("Beta", "Gamma", "Alfa"));
    $$("").shouldHave(exactTextsCaseSensitiveInAnyOrder("Beta", "Gamma", "Alfa"));

    $$("").shouldHave(itemWithText("Gamma")); // only one text

    $$("").shouldHave(sizeGreaterThan(0));
    $$("").shouldHave(sizeGreaterThanOrEqual(1));
    $$("").shouldHave(sizeLessThan(3));
    $$("").shouldHave(sizeLessThanOrEqual(2));


  }

  void file_operation_examples() throws FileNotFoundException {
    
//скачивание файлов
    File file1 = $("a.fileLink").download(); // only for <a href=".."> links
    File file2 = $("div").download(DownloadOptions.using(FileDownloadMode.FOLDER)); //основной способ скачать файл more common options, but may have problems with Grid/Selenoid

    File file = new File("src/test/resources/readme.txt");
    $("#file-upload").uploadFile(file);
    $("#file-upload").uploadFromClasspath("readme.txt");
    // don't forget to submit!
    $("uploadButton").click();
  }

  void javascript_examples() {
    executeJavaScript("alert('selenide')");
    executeJavaScript("alert(arguments[0]+arguments[1])", "abc", 12);
    long fortytwo = executeJavaScript("return arguments[0]*arguments[1];", 6, 7);
  }
}

