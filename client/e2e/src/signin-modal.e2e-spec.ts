import { AppPage } from './app.po';
import { browser, logging, by, element, protractor } from 'protractor';


describe('Connect to Okastock', () => {
    let page: AppPage;

    beforeEach(() => {
        page = new AppPage();
    });

    it('should display home title', () => {
        page.navigateHome();
        expect(page.getTitle()).toEqual('Toutes les catégories')
    });

    it('should open conection modal', () => {
        page.openConexionModal().click();
        expect(element(by.css('.modal-style h2')).getText()).toEqual('Se connecter')
    });

    it('should not be able to login with empty email', async () => {
        await page.signIn({
            email: '',
            password: 'azer1234'
        });
        expect(page.getEmailErrorMessage()).toEqual('Veuillez indiquer une adresse mail.')
    })

    it('should not be able to login with invalid email', async () => {
        await page.signIn({
            email: 'test@test.',
            password: 'azer1234'
        });
        expect(page.getEmailErrorMessage()).toEqual('Veuillez indiquer une adresse mail valide.')
    })

    it('should not be able to login with wrong credentials (email exists, wrong password)', async () => {
        await page.signIn({
            email: 'site@okastock.com',
            password: 'azer1234'
        });
        expect(page.getCredentialsErrorMessage()).toEqual('Votre email ou mot de passe est invalide.')
    })

    it('should not be able to login with bad credentials (email dont exists, wrong password)', async () => {
        await page.signIn({
            email: 'test@test.com',
            password: 'azer1234'
        });
        expect(page.getCredentialsErrorMessage()).toEqual('Votre email ou mot de passe est invalide.')
    })

    it('should be able to login with good credentials', async () => {
        await page.signIn({
            email: 'site@okastock.com',
            password: '1234AZER'
        }).then();
        browser.wait(function () {
            return browser.executeScript("return window.localStorage.getItem('user');");
        }, 5000);
    })

});
