package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private SelenideElement heading = $(byText("Ваши карты"));
    private SelenideElement transferButton = $("[data-test-id= 'action-transfer']");
    private SelenideElement amountInput = $("[data-test-id='amount'] input");
    private SelenideElement fromInput = $("data-test-id='from'] input");
    private SelenideElement transferHead = $(byText("Пополнение карты"));
    private SelenideElement errorMessage = $("[data-test-id='error-notification'] .notification__content");

    public TransferPage() {
        heading.shouldBe(visible);
    }


}
