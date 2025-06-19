import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';

import { RouteGaurdService } from './route-gaurd.service';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { BasicAuthenticationService } from './basic-authentication.service';
import { HardcodedAuthenticationService } from './hardcoded-authentication.service';

describe('RouteGaurdService', () => {
  let service: RouteGaurdService;
  let basicAuthService: BasicAuthenticationService;
  let hardcodedAuthService: HardcodedAuthenticationService;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideRouter([
          { path: 'login', component: class {} },
          { path: 'protected', component: class {} }
        ]),
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(RouteGaurdService);
    basicAuthService = TestBed.inject(BasicAuthenticationService);
    hardcodedAuthService = TestBed.inject(HardcodedAuthenticationService);
    router = TestBed.inject(Router);

    // Clear any existing authentication
    sessionStorage.clear();
    basicAuthService.logout();
  });

  afterEach(() => {
    sessionStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('canActivate', () => {
    it('should return true when user is logged in via BasicAuthenticationService', () => {
      // Mock the isUserLoggedIn method to return true
      spyOn(basicAuthService, 'isUserLoggedIn').and.returnValue(true);

      const result = service.canActivate();

      expect(result).toBe(true);
    });

    it('should return false and navigate to login when user is not logged in', () => {
      // Mock the isUserLoggedIn method to return false
      spyOn(basicAuthService, 'isUserLoggedIn').and.returnValue(false);
      const navigateSpy = spyOn(router, 'navigate');

      const result = service.canActivate();

      expect(result).toBe(false);
      expect(navigateSpy).toHaveBeenCalledWith(['login']);
    });

    it('should use BasicAuthenticationService for authentication check', () => {
      const isUserLoggedInSpy = spyOn(basicAuthService, 'isUserLoggedIn').and.returnValue(true);

      service.canActivate();

      expect(isUserLoggedInSpy).toHaveBeenCalled();
    });
    it('should handle authentication state changes', () => {
      const navigateSpy = spyOn(router, 'navigate');
      const isLoggedInSpy = spyOn(basicAuthService, 'isUserLoggedIn');

      // First call - user not logged in
      isLoggedInSpy.and.returnValue(false);
      let result = service.canActivate();
      expect(result).toBe(false);
      expect(navigateSpy).toHaveBeenCalledWith(['login']);

      // Second call - user logged in
      isLoggedInSpy.and.returnValue(true);
      result = service.canActivate();
      expect(result).toBe(true);
    });

    it('should work with real authentication flow', () => {
      const navigateSpy = spyOn(router, 'navigate');

      // Initially not authenticated
      let result = service.canActivate();
      expect(result).toBe(false);
      expect(navigateSpy).toHaveBeenCalledWith(['login']);

      // Authenticate user
      sessionStorage.setItem('authenticatedUser', 'testuser');
      sessionStorage.setItem('token', 'Basic dGVzdDp0ZXN0');
      basicAuthService.refreshFromStorage();

      // Now should be authenticated
      result = service.canActivate();
      expect(result).toBe(true);
    });
  });

  describe('service dependencies', () => {
    it('should inject BasicAuthenticationService', () => {
      expect(service['basicAuthenticationService']).toBe(basicAuthService);
    });

    it('should inject HardcodedAuthenticationService', () => {
      expect(service['hardcodedAuthenticationService']).toBe(hardcodedAuthService);
    });

    it('should inject Router', () => {
      expect(service['router']).toBe(router);
    });
  });
});
