import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { provideRouter, ActivatedRoute } from '@angular/router';
import { of, throwError } from 'rxjs';

import { WelcomeComponent } from './welcome.component';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { WelcomeDataService, PingResponse } from '../service/data/welcome-data.service';

describe('WelcomeComponent', () => {
  let component: WelcomeComponent;
  let fixture: ComponentFixture<WelcomeComponent>;
  let welcomeDataService: jasmine.SpyObj<WelcomeDataService>;
  let activatedRoute: jasmine.SpyObj<ActivatedRoute>;
  let httpController: HttpTestingController;

  beforeEach(waitForAsync(() => {
    const welcomeDataServiceSpy = jasmine.createSpyObj('WelcomeDataService', [
      'executeHelloWorldBeanServiceWithPathVariable',
      'executeHelloWorldBeanService'
    ]);
    const activatedRouteSpy = jasmine.createSpyObj('ActivatedRoute', [], {
      snapshot: { params: { name: 'testUser' } }
    });

    TestBed.configureTestingModule({
      imports: [WelcomeComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting(),
        { provide: WelcomeDataService, useValue: welcomeDataServiceSpy },
        { provide: ActivatedRoute, useValue: activatedRouteSpy }
      ]
    })
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(WelcomeComponent);
        component = fixture.componentInstance;
        welcomeDataService = TestBed.inject(
          WelcomeDataService
        ) as jasmine.SpyObj<WelcomeDataService>;
        activatedRoute = TestBed.inject(ActivatedRoute) as jasmine.SpyObj<ActivatedRoute>;
        httpController = TestBed.inject(HttpTestingController);
        fixture.detectChanges();
      });
  }));

  afterEach(() => {
    httpController.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should initialize name from route parameters', () => {
    expect(component.name()).toBe('testUser');
  });

  it('should handle successful welcome message with path variable', () => {
    const mockResponse: PingResponse = { message: 'Hello testUser!' };
    welcomeDataService.executeHelloWorldBeanServiceWithPathVariable.and.returnValue(
      of(mockResponse)
    );

    component.getWelcomeMessage();

    expect(welcomeDataService.executeHelloWorldBeanServiceWithPathVariable).toHaveBeenCalledWith(
      'testUser'
    );
    expect(component.welcomeMessage()).toBe('Hello testUser!');
  });

  it('should fallback to basic service when path variable service fails', () => {
    const pathVariableError = new Error('Path variable service failed');
    const mockResponse: PingResponse = { message: 'Hello World!' };

    welcomeDataService.executeHelloWorldBeanServiceWithPathVariable.and.returnValue(
      throwError(() => pathVariableError)
    );
    welcomeDataService.executeHelloWorldBeanService.and.returnValue(of(mockResponse));

    spyOn(console, 'log');
    component.getWelcomeMessage();

    expect(welcomeDataService.executeHelloWorldBeanServiceWithPathVariable).toHaveBeenCalledWith(
      'testUser'
    );
    expect(welcomeDataService.executeHelloWorldBeanService).toHaveBeenCalled();
    expect(component.welcomeMessage()).toBe('Hello World!');
    expect(console.log).toHaveBeenCalledWith(
      'Error with path variable, falling back to basic service:',
      pathVariableError
    );
  });

  it('should handle error when both services fail', () => {
    const pathVariableError = new Error('Path variable service failed');
    const basicServiceError = new Error('Basic service failed');

    welcomeDataService.executeHelloWorldBeanServiceWithPathVariable.and.returnValue(
      throwError(() => pathVariableError)
    );
    welcomeDataService.executeHelloWorldBeanService.and.returnValue(
      throwError(() => basicServiceError)
    );

    spyOn(console, 'error');
    component.getWelcomeMessage();
    expect(welcomeDataService.executeHelloWorldBeanServiceWithPathVariable).toHaveBeenCalledWith(
      'testUser'
    );
    expect(welcomeDataService.executeHelloWorldBeanService).toHaveBeenCalled();
    expect(component.welcomeMessage()).toBe(
      'An unexpected error occurred. Please check if the backend server is running.'
    );
    expect(console.error).toHaveBeenCalledWith(
      'Error fetching welcome message:',
      basicServiceError
    );
  });

  it('should handle HTTP error response with message', () => {
    const httpError = {
      error: { message: 'Server is down' }
    };

    welcomeDataService.executeHelloWorldBeanServiceWithPathVariable.and.returnValue(
      throwError(() => httpError)
    );
    welcomeDataService.executeHelloWorldBeanService.and.returnValue(throwError(() => httpError));

    spyOn(console, 'error');
    component.getWelcomeMessage();

    expect(component.welcomeMessage()).toBe('Error: Server is down');
    expect(console.error).toHaveBeenCalledWith('Error fetching welcome message:', httpError);
  });

  it('should handle HTTP error response without message', () => {
    const httpError = {
      error: {}
    };

    welcomeDataService.executeHelloWorldBeanServiceWithPathVariable.and.returnValue(
      throwError(() => httpError)
    );
    welcomeDataService.executeHelloWorldBeanService.and.returnValue(throwError(() => httpError));

    spyOn(console, 'error');
    component.getWelcomeMessage();

    expect(component.welcomeMessage()).toBe(
      'An unexpected error occurred. Please check if the backend server is running.'
    );
    expect(console.error).toHaveBeenCalledWith('Error fetching welcome message:', httpError);
  });

  it('should test isHttpErrorResponse with valid HTTP error', () => {
    const validError = {
      error: { message: 'Test error' }
    };

    expect(component['isHttpErrorResponse'](validError)).toBe(true);
  });

  it('should test isHttpErrorResponse with invalid error types', () => {
    expect(component['isHttpErrorResponse'](null)).toBe(false);
    expect(component['isHttpErrorResponse'](undefined)).toBe(false);
    expect(component['isHttpErrorResponse']('string error')).toBe(false);
    expect(component['isHttpErrorResponse']({})).toBe(false);
    expect(component['isHttpErrorResponse']({ error: null })).toBe(false);
    expect(component['isHttpErrorResponse']({ error: 'string' })).toBe(false);
    expect(component['isHttpErrorResponse']({ error: {} })).toBe(false);
  });
  it('should handle empty name parameter', () => {
    activatedRoute.snapshot.params = {};
    component.ngOnInit();
    expect(component.name()).toBe('');
  });
});
