import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { HardcodedAuthenticationService } from './hardcoded-authentication.service';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

describe('HardcodedAuthenticationService', () => {
  let service: HardcodedAuthenticationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(HardcodedAuthenticationService);
    // Clear session storage before each test
    sessionStorage.clear();
  });

  afterEach(() => {
    // Clean up session storage after each test
    sessionStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('authenticate', () => {
    it('should return true and set user in session storage for valid credentials', () => {
      const result = service.authenticate('username', 'dummy');

      expect(result).toBe(true);
      expect(sessionStorage.getItem('authenticatedUser')).toBe('username');
      expect(service.isUserLoggedIn()).toBe(true);
      expect(service.currentUser()).toBe('username');
    });

    it('should return false for invalid username', () => {
      const result = service.authenticate('wronguser', 'dummy');

      expect(result).toBe(false);
      expect(sessionStorage.getItem('authenticatedUser')).toBeNull();
      expect(service.isUserLoggedIn()).toBe(false);
      expect(service.currentUser()).toBeNull();
    });

    it('should return false for invalid password', () => {
      const result = service.authenticate('username', 'wrongpass');

      expect(result).toBe(false);
      expect(sessionStorage.getItem('authenticatedUser')).toBeNull();
      expect(service.isUserLoggedIn()).toBe(false);
      expect(service.currentUser()).toBeNull();
    });

    it('should return false for both invalid credentials', () => {
      const result = service.authenticate('wronguser', 'wrongpass');

      expect(result).toBe(false);
      expect(sessionStorage.getItem('authenticatedUser')).toBeNull();
      expect(service.isUserLoggedIn()).toBe(false);
      expect(service.currentUser()).toBeNull();
    });
  });

  describe('logout', () => {
    it('should clear user from session storage and reset signals', () => {
      // First authenticate
      service.authenticate('username', 'dummy');
      expect(service.isUserLoggedIn()).toBe(true);

      // Then logout
      service.logout();

      expect(sessionStorage.getItem('authenticatedUser')).toBeNull();
      expect(service.isUserLoggedIn()).toBe(false);
      expect(service.currentUser()).toBeNull();
    });

    it('should handle logout when no user is authenticated', () => {
      // Logout without being authenticated
      service.logout();

      expect(sessionStorage.getItem('authenticatedUser')).toBeNull();
      expect(service.isUserLoggedIn()).toBe(false);
      expect(service.currentUser()).toBeNull();
    });
  });
  describe('initialization from session storage', () => {
    it('should initialize with user from session storage', () => {
      // Set user in session storage before service creation
      sessionStorage.setItem('authenticatedUser', 'storedUser');

      // Create new service instance by getting a fresh instance
      TestBed.resetTestingModule();
      TestBed.configureTestingModule({
        providers: [
          provideRouter([]),
          provideHttpClient(withInterceptorsFromDi()),
          provideHttpClientTesting()
        ]
      });
      const newService = TestBed.inject(HardcodedAuthenticationService);

      expect(newService.isUserLoggedIn()).toBe(true);
      expect(newService.currentUser()).toBe('storedUser');
    });

    it('should initialize with no user when session storage is empty', () => {
      // Ensure session storage is empty
      sessionStorage.clear();

      // Create new service instance
      TestBed.resetTestingModule();
      TestBed.configureTestingModule({
        providers: [
          provideRouter([]),
          provideHttpClient(withInterceptorsFromDi()),
          provideHttpClientTesting()
        ]
      });
      const newService = TestBed.inject(HardcodedAuthenticationService);

      expect(newService.isUserLoggedIn()).toBe(false);
      expect(newService.currentUser()).toBeNull();
    });
  });

  describe('computed signals', () => {
    it('should update isUserLoggedIn when user changes', () => {
      expect(service.isUserLoggedIn()).toBe(false);

      service.authenticate('username', 'dummy');
      expect(service.isUserLoggedIn()).toBe(true);

      service.logout();
      expect(service.isUserLoggedIn()).toBe(false);
    });

    it('should update currentUser when user changes', () => {
      expect(service.currentUser()).toBeNull();

      service.authenticate('username', 'dummy');
      expect(service.currentUser()).toBe('username');

      service.logout();
      expect(service.currentUser()).toBeNull();
    });
  });
});
