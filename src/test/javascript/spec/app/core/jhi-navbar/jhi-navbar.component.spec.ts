import { createLocalVue, shallowMount, Wrapper } from '@vue/test-utils';
import JhiNavbar from '@/core/jhi-navbar/jhi-navbar.vue';
import JhiNavbarClass from '@/core/jhi-navbar/jhi-navbar.component';
import * as config from '@/shared/config/config';
import { AccountStoreModule as accountStore } from '@/shared/config/store/account-store'
import { TranslationStoreModule as translationStore } from '@/shared/config/store/translation-store'
import router from '@/router';

const localVue = createLocalVue();
config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-navbar', {});
localVue.component('b-navbar-nav', {});
localVue.component('b-dropdown-item', {});
localVue.component('b-collapse', {});
localVue.component('b-nav-item', {});
localVue.component('b-nav-item-dropdown', {});
localVue.component('b-navbar-toggle', {});
localVue.component('b-navbar-brand', {});
localVue.component('b-navbar-nav', {});

describe('JhiNavbar', () => {
  let jhiNavbar: JhiNavbarClass;
  let wrapper: Wrapper<JhiNavbarClass>;
  const loginService = { openLogin: jest.fn() };
  const accountService = { hasAnyAuthority: jest.fn() };
  const translationService = { refreshTranslation: jest.fn() };

  beforeEach(() => {
    wrapper = shallowMount<JhiNavbarClass>(JhiNavbar, {
      i18n,
      store,
      router,
      localVue,
      provide: {
        loginService: () => loginService,
        translationService: () => translationService,
        accountService: () => accountService
      }
    });
    jhiNavbar = wrapper.vm;
  });

  it('should be a Vue instance', () => {
    expect(wrapper.isVueInstance()).toBeTruthy();

    expect(translationService.refreshTranslation).toHaveBeenCalled();
  });

  it('should not have user data set', () => {
    expect(jhiNavbar.authenticated).toBeFalsy();
    expect(jhiNavbar.swaggerEnabled).toBeFalsy();
    expect(jhiNavbar.inProduction).toBeFalsy();
  });

  it('should have user data set after authentication', async () => {
    await accountStore.setAuthenticated({ login: 'test' })
    expect(jhiNavbar.authenticated).toBeTruthy();
  });

  it('should have profile info set after info retrieved', async () => {
    await accountStore.setActiveProfiles(['prod', 'swagger'])
    expect(jhiNavbar.swaggerEnabled).toBeTruthy();
    expect(jhiNavbar.inProduction).toBeTruthy();
  });

  it('should use login service', () => {
    jhiNavbar.openLogin();
    expect(loginService.openLogin).toHaveBeenCalled();
  });

  it('should use account service', () => {
    jhiNavbar.hasAnyAuthority('auth');

    expect(accountService.hasAnyAuthority).toHaveBeenCalled();
  });

  it('logout should clear credentials', async () => {
    await accountStore.setAuthenticated({ login: 'test' })
    jhiNavbar.logout();

    expect(jhiNavbar.authenticated).toBeFalsy();
  });

  it('should determine active route', () => {
    router.push('/toto');

    expect(jhiNavbar.subIsActive('/titi')).toBeFalsy();
    expect(jhiNavbar.subIsActive('/toto')).toBeTruthy();
    expect(jhiNavbar.subIsActive(['/toto', 'toto'])).toBeTruthy();
  });

  it('should call translationService when changing language', () => {
    jhiNavbar.changeLanguage('fr');

    expect(translationService.refreshTranslation).toHaveBeenCalled();
  });

  it('should check for correct language', () => {
    translationStore.setLanguage('fr')
    expect(jhiNavbar.isActiveLanguage('en')).toBeFalsy();
    expect(jhiNavbar.isActiveLanguage('fr')).toBeTruthy();
  });
});
