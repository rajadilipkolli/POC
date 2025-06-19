import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { HttpRequest, HttpHandler, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { HttpInterceptorBasicAuthService } from './http-interceptor-basic-auth.service';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { BasicAuthenticationService } from '../basic-authentication.service';

describe('HttpInterceptorBasicAuthService', () => {
  let service: HttpInterceptorBasicAuthService;
  let basicAuthService: jasmine.SpyObj<BasicAuthenticationService>;
  let httpHandler: jasmine.SpyObj<HttpHandler>;

  beforeEach(() => {
    const basicAuthSpy = jasmine.createSpyObj('BasicAuthenticationService', [
      'getAuthenticatedToken',
      'getAuthenticatedUser'
    ]);
    const httpHandlerSpy = jasmine.createSpyObj('HttpHandler', ['handle']);

    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting(),
        { provide: BasicAuthenticationService, useValue: basicAuthSpy }
      ]
    });

    service = TestBed.inject(HttpInterceptorBasicAuthService);
    basicAuthService = TestBed.inject(
      BasicAuthenticationService
    ) as jasmine.SpyObj<BasicAuthenticationService>;
    httpHandler = httpHandlerSpy;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should add authorization header when user is authenticated', () => {
    const mockToken = 'Basic dGVzdDp0ZXN0';
    const mockUser = 'testUser';
    const mockRequest = new HttpRequest('GET', '/api/test');
    const mockResponse = new HttpResponse({ status: 200, body: {} });

    basicAuthService.getAuthenticatedUser.and.returnValue(mockUser);
    basicAuthService.getAuthenticatedToken.and.returnValue(mockToken);
    httpHandler.handle.and.returnValue(of(mockResponse));

    service.intercept(mockRequest, httpHandler).subscribe();

    expect(basicAuthService.getAuthenticatedUser).toHaveBeenCalled();
    expect(basicAuthService.getAuthenticatedToken).toHaveBeenCalled();
    expect(httpHandler.handle).toHaveBeenCalledWith(
      jasmine.objectContaining({
        headers: jasmine.objectContaining({
          lazyUpdate: jasmine.arrayContaining([
            jasmine.objectContaining({
              name: 'Authorization',
              value: mockToken,
              op: 's'
            })
          ])
        })
      })
    );
  });

  it('should not add authorization header when user is not authenticated', () => {
    const mockRequest = new HttpRequest('GET', '/api/test');
    const mockResponse = new HttpResponse({ status: 200, body: {} });

    basicAuthService.getAuthenticatedUser.and.returnValue(null);
    basicAuthService.getAuthenticatedToken.and.returnValue('');
    httpHandler.handle.and.returnValue(of(mockResponse));

    service.intercept(mockRequest, httpHandler).subscribe();

    expect(basicAuthService.getAuthenticatedUser).toHaveBeenCalled();
    expect(basicAuthService.getAuthenticatedToken).toHaveBeenCalled();
    expect(httpHandler.handle).toHaveBeenCalledWith(mockRequest);
  });

  it('should not add authorization header when token is empty but user exists', () => {
    const mockUser = 'testUser';
    const mockRequest = new HttpRequest('GET', '/api/test');
    const mockResponse = new HttpResponse({ status: 200, body: {} });

    basicAuthService.getAuthenticatedUser.and.returnValue(mockUser);
    basicAuthService.getAuthenticatedToken.and.returnValue('');
    httpHandler.handle.and.returnValue(of(mockResponse));

    service.intercept(mockRequest, httpHandler).subscribe();

    expect(basicAuthService.getAuthenticatedUser).toHaveBeenCalled();
    expect(basicAuthService.getAuthenticatedToken).toHaveBeenCalled();
    expect(httpHandler.handle).toHaveBeenCalledWith(mockRequest);
  });

  it('should not add authorization header when user exists but token is null', () => {
    const mockUser = 'testUser';
    const mockRequest = new HttpRequest('GET', '/api/test');
    const mockResponse = new HttpResponse({ status: 200, body: {} });

    basicAuthService.getAuthenticatedUser.and.returnValue(mockUser);
    basicAuthService.getAuthenticatedToken.and.returnValue('');
    httpHandler.handle.and.returnValue(of(mockResponse));

    service.intercept(mockRequest, httpHandler).subscribe();

    expect(basicAuthService.getAuthenticatedUser).toHaveBeenCalled();
    expect(basicAuthService.getAuthenticatedToken).toHaveBeenCalled();
    expect(httpHandler.handle).toHaveBeenCalledWith(mockRequest);
  });
});
