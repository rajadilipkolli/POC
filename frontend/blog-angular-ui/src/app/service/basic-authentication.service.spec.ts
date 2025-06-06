import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { API_URL } from '../app.constants';

import { BasicAuthenticationService, TOKEN, AUTHENTICATED_USER } from './basic-authentication.service';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

describe('BasicAuthenticationService', () => {
  let service: BasicAuthenticationService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [
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
    const mockResponse = { message: 'Success' };

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
});
