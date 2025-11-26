import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { WelcomeDataService, PingResponse } from './welcome-data.service';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { API_URL } from '../../app.constants';

describe('WelcomeDataService', () => {
  let service: WelcomeDataService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(WelcomeDataService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Verify that no unmatched requests are outstanding
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('executeHelloWorldBeanService', () => {
    it('should make GET request to ping endpoint', () => {
      const mockResponse: PingResponse = { message: 'Hello World!' };

      service.executeHelloWorldBeanService().subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpTestingController.expectOne(`${API_URL}/ping`);
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should handle error response', () => {
      const errorMessage = 'Server error';

      service.executeHelloWorldBeanService().subscribe({
        next: () => fail('Should have failed'),
        error: error => {
          expect(error.status).toBe(500);
          expect(error.statusText).toBe('Internal Server Error');
        }
      });

      const req = httpTestingController.expectOne(`${API_URL}/ping`);
      req.flush(errorMessage, { status: 500, statusText: 'Internal Server Error' });
    });

    it('should handle network error', () => {
      service.executeHelloWorldBeanService().subscribe({
        next: () => fail('Should have failed'),
        error: error => {
          expect(error.error).toBeInstanceOf(ProgressEvent);
        }
      });

      const req = httpTestingController.expectOne(`${API_URL}/ping`);
      req.error(new ProgressEvent('Network error'));
    });
  });

  describe('executeHelloWorldBeanServiceWithPathVariable', () => {
    it('should make GET request with username parameter', () => {
      const mockResponse: PingResponse = { message: 'Hello testuser!' };
      const username = 'testuser';

      service.executeHelloWorldBeanServiceWithPathVariable(username).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpTestingController.expectOne(`${API_URL}/ping?userName=${username}`);
      expect(req.request.method).toBe('GET');
      expect(req.request.urlWithParams).toBe(`${API_URL}/ping?userName=${username}`);
      req.flush(mockResponse);
    });

    it('should handle empty username', () => {
      const mockResponse: PingResponse = { message: 'Hello !' };
      const username = '';

      service.executeHelloWorldBeanServiceWithPathVariable(username).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });
      const req = httpTestingController.expectOne(`${API_URL}/ping?userName=`);
      expect(req.request.method).toBe('GET');
      expect(req.request.url).toBe(`${API_URL}/ping?userName=`);
      req.flush(mockResponse);
    });

    it('should handle special characters in username', () => {
      const mockResponse: PingResponse = { message: 'Hello test@user!' };
      const username = 'test@user';

      service.executeHelloWorldBeanServiceWithPathVariable(username).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });
      const req = httpTestingController.expectOne(`${API_URL}/ping?userName=${username}`);
      expect(req.request.method).toBe('GET');
      expect(req.request.url).toBe(`${API_URL}/ping?userName=${username}`);
      req.flush(mockResponse);
    });

    it('should handle error response with path variable', () => {
      const username = 'testuser';
      const errorMessage = 'User not found';

      service.executeHelloWorldBeanServiceWithPathVariable(username).subscribe({
        next: () => fail('Should have failed'),
        error: error => {
          expect(error.status).toBe(404);
          expect(error.statusText).toBe('Not Found');
        }
      });

      const req = httpTestingController.expectOne(`${API_URL}/ping?userName=${username}`);
      req.flush(errorMessage, { status: 404, statusText: 'Not Found' });
    });
  });

  describe('HTTP integration', () => {
    it('should use injected HttpClient', () => {
      expect(service['http']).toBeDefined();
    });

    it('should use correct API_URL constant', () => {
      service.executeHelloWorldBeanService().subscribe();

      const req = httpTestingController.expectOne(`${API_URL}/ping`);
      expect(req.request.url).toContain(API_URL);
      req.flush({ message: 'test' });
    });
  });
});
