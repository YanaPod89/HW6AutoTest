package ru.netology.web.test;

import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.*;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

import ru.netology.web.page.DashboardPage;

class MoneyTransferTest {
    @Test
    void shouldTransferMoneyBetweenOwnCardsV1() {
      open("http://localhost:9999");
      var loginPage = new LoginPageV1();
//    var loginPage = open("http://localhost:9999", LoginPageV1.class);
      var authInfo = DataHelper.getAuthInfo();
      var verificationPage = loginPage.validLogin(authInfo);
      var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
      DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
      DataHelper.CardInfo firstCardInfo = DataHelper.getFirstCardInfo();
      DataHelper.CardInfo secondCardInfo = DataHelper.getSecondCardInfo();
      int firstCardBalance = dashboardPage.getCardBalance(0);
      int secondCardBalance = dashboardPage.getCardBalance(1);

      var amount = generateValidAmount(firstCardBalance);
      var expectedBalanceFirstCard = firstCardBalance - amount;
      var expectedBalanceSecondCard = secondCardBalance + amount;
      var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
      dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
      dashboardPage.reloadDashboardPage();
      var actualBalanceFirstCard = dashboardPage.getCardBalance(0);
      var actualBalanceSecondCard = dashboardPage.getCardBalance(1);
      assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
      assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);

    }

  @Test
  void shouldNotTransferMoneyBetweenOwnCardsV1() {
    open("http://localhost:9999");
    var loginPage = new LoginPageV1();
//    var loginPage = open("http://localhost:9999", LoginPageV1.class);
    var authInfo = DataHelper.getAuthInfo();
    var verificationPage = loginPage.validLogin(authInfo);
    var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
    DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
    DataHelper.CardInfo firstCardInfo = DataHelper.getFirstCardInfo();
    DataHelper.CardInfo secondCardInfo = DataHelper.getSecondCardInfo();
    int firstCardBalance = dashboardPage.getCardBalance(0);
    int secondCardBalance = dashboardPage.getCardBalance(1);

    var amount = generateInvalidAmount(secondCardBalance);
    var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
    transferPage.makeTransfer(String.valueOf(amount), secondCardInfo);
    transferPage.findErrorMessage("Выполнена попытка перевода суммы, превышающий остаток на карте списания");
    dashboardPage.reloadDashboardPage();

    var actualBalanceFirstCard = dashboardPage.getCardBalance(0);
    var actualBalanceSecondCard = dashboardPage.getCardBalance(1);
    assertEquals(firstCardBalance, actualBalanceFirstCard);
    assertEquals(secondCardBalance, actualBalanceSecondCard);
  }

}

