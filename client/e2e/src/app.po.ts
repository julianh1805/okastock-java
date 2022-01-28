import { browser, by, element } from 'protractor';

export class AppPage {
  navigateHome() {
    return browser.get('/#/produits');
  }

  getTitle() {
    return element(by.css('h2.category-title')).getText();
  }

  openConexionModal() {
    return element(by.css('button.auth'));
  }

  async signIn({ email, password }) {
    var emailElement = element(by.css('.modal-style #email'))
    await emailElement.clear().then(() => {
      emailElement.sendKeys(email);
    })
    var passwordElement = element(by.css('.modal-style #password'))
    await passwordElement.clear().then(() => {
      passwordElement.sendKeys(password);
    })
    await element(by.css('.modal-style button')).click();
  }

  getEmailErrorMessage() {
    return element(by.css('.modal-style .email-invalid')).getText();
  }

  getPasswordErrorMessage() {
    return element(by.css('.modal-style .password-invalid')).getText();
  }

  getCredentialsErrorMessage() {
    return element(by.css('.modal-style .credentials-invalid')).getText();
  }
}
