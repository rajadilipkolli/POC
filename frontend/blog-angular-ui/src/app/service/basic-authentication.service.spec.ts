import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing';
import {TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';
import {API_URL} from '../app.constants';

import {AUTHENTICATED_USER, BasicAuthenticationService, TOKEN} from './basic-authentication.service';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';

describe('BasicAuthenticationService', () => {
  let service: BasicAuthenticationService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(BasicAuthenticationService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
    sessionStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should execute authentication service and store session data on success', () => {
    const username = 'testUser';
    const password = 'testPass';
    const mockResponse = {message: 'Success'};

    service.executeAuthenticationService(username, password).subscribe(response => {
      expect(response).toEqual(mockResponse);
      expect(sessionStorage.getItem(AUTHENTICATED_USER)).toBe(username);
      expect(sessionStorage.getItem(TOKEN)).toBe('Basic ' + window.btoa(username + ':' + password));
    });

    const req = httpTestingController.expectOne(`${API_URL}/pingWithAuthentication`);
    expect(req.request.headers.get('Authorization')).toBe('Basic ' + window.btoa(username + ':' + password));
    req.flush(mockResponse);
  });

  it('should return correct authentication status when user is logged in', () => {
    sessionStorage.setItem(AUTHENTICATED_USER, 'testUser');
    expect(service.isUserLoggedIn()).toBeTrue();
  });

  it('should return correct authentication status when user is not logged in', () => {
    expect(service.isUserLoggedIn()).toBeFalse();
  });

  it('should retrieve authenticated user from session storage', () => {
    const testUser = 'testUser';
    sessionStorage.setItem(AUTHENTICATED_USER, testUser);
    expect(service.getAuthenticatedUser()).toBe(testUser);
  });

  it('should retrieve authentication token when user is logged in', () => {
    const testUser = 'testUser';
    const testToken = 'testToken';
    sessionStorage.setItem(AUTHENTICATED_USER, testUser);
    sessionStorage.setItem(TOKEN, testToken);
    expect(service.getAuthenticatedToken()).toBe(testToken);
  });

  it('should return empty string for token when user is not logged in', () => {
    expect(service.getAuthenticatedToken()).toBe('');
  });

  it('should clear session storage on logout', () => {
    sessionStorage.setItem(AUTHENTICATED_USER, 'testUser');
    sessionStorage.setItem(TOKEN, 'testToken');

    service.logout();

    expect(sessionStorage.getItem(AUTHENTICATED_USER)).toBeNull();
    expect(sessionStorage.getItem(TOKEN)).toBeNull();
  });

  it('should handle authentication error', () => {
    const username = 'testUser';
    const password = 'wrongPass';
    const consoleSpy = spyOn(console, 'log');

    service.executeAuthenticationService(username, password).subscribe({
      next: () => fail('Should have failed'),
      error: (error) => {
        expect(error).toBeDefined();
        expect(sessionStorage.getItem(AUTHENTICATED_USER)).toBeNull();
        expect(sessionStorage.getItem(TOKEN)).toBeNull();
      }
    });

    const req = httpTestingController.expectOne(`${API_URL}/pingWithAuthentication`);
    req.flush('Unauthorized', { status: 401, statusText: 'Unauthorized' });
  });

  it('should handle empty username and password', () => {
    const username = '';
    const password = '';
    const mockResponse = {message: 'Success'};

    service.executeAuthenticationService(username, password).subscribe(response => {
      expect(response).toEqual(mockResponse);
      expect(sessionStorage.getItem(AUTHENTICATED_USER)).toBe('');
      expect(sessionStorage.getItem(TOKEN)).toBe('Basic ' + window.btoa(':'));
    });

    const req = httpTestingController.expectOne(`${API_URL}/pingWithAuthentication`);
    expect(req.request.headers.get('Authorization')).toBe('Basic ' + window.btoa(':'));
    req.flush(mockResponse);
  });

  it('should handle special characters in username and password', () => {
    const username = 'test@user.com';
    const password = 'p@ssw0rd!';
    const mockResponse = {message: 'Success'};

    service.executeAuthenticationService(username, password).subscribe(response => {
      expect(response).toEqual(mockResponse);
      expect(sessionStorage.getItem(AUTHENTICATED_USER)).toBe(username);
      expect(sessionStorage.getItem(TOKEN)).toBe('Basic ' + window.btoa(username + ':' + password));
    });

    const req = httpTestingController.expectOne(`${API_URL}/pingWithAuthentication`);
    req.flush(mockResponse);
  });

  it('should return null when no authenticated user in session', () => {
    expect(service.getAuthenticatedUser()).toBeNull();
  });

  it('should return empty string for token when user exists but no token', () => {
    sessionStorage.setItem(AUTHENTICATED_USER, 'testUser');
    // Don't set token
    expect(service.getAuthenticatedToken()).toBe('');
  });

  it('should handle network error during authentication', () => {
    const username = 'testUser';
    const password = 'testPass';

    service.executeAuthenticationService(username, password).subscribe({
      next: () => fail('Should have failed'),
      error: (error) => {
        expect(error).toBeDefined();
        expect(sessionStorage.getItem(AUTHENTICATED_USER)).toBeNull();
        expect(sessionStorage.getItem(TOKEN)).toBeNull();
      }
    });

    const req = httpTestingController.expectOne(`${API_URL}/pingWithAuthentication`);
    req.error(new ErrorEvent('Network error'));
  });

  it('should handle null values in session storage gracefully', () => {
    sessionStorage.setItem(AUTHENTICATED_USER, 'null');
    expect(service.isUserLoggedIn()).toBeTrue(); // 'null' string is truthy
    
    sessionStorage.removeItem(AUTHENTICATED_USER);
    expect(service.isUserLoggedIn()).toBeFalse();
  });

  it('should clear only authentication-related items from session storage', () => {
    sessionStorage.setItem(AUTHENTICATED_USER, 'testUser');
    sessionStorage.setItem(TOKEN, 'testToken');
    sessionStorage.setItem('other-item', 'other-value');

    service.logout();

    expect(sessionStorage.getItem(AUTHENTICATED_USER)).toBeNull();
    expect(sessionStorage.getItem(TOKEN)).toBeNull();
    expect(sessionStorage.getItem('other-item')).toBe('other-value');
  });
});
