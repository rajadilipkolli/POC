import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

import { LoginComponent } from './login.component';
import { HardcodedAuthenticationService } from '../service/hardcoded-authentication.service';
import { BasicAuthenticationService } from '../service/basic-authentication.service';
import { API_URL } from '../app.constants';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let router: Router;
  let hardcodedAuthService: HardcodedAuthenticationService;
  let httpTestingController: HttpTestingController;
  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting(),
        HardcodedAuthenticationService,
        BasicAuthenticationService
      ]
    })
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(LoginComponent);
        component = fixture.componentInstance;
        router = TestBed.inject(Router);
        hardcodedAuthService = TestBed.inject(HardcodedAuthenticationService);
        httpTestingController = TestBed.inject(HttpTestingController);
        fixture.detectChanges();
      });
  }));

  afterEach(() => {
    httpTestingController.verify();
    sessionStorage.clear();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with default form values', () => {
    expect(component.loginForm.get('username')?.value).toBe('username');
    expect(component.loginForm.get('password')?.value).toBe('dummy');
    expect(component.errorMessage()).toBe('Invalid Credentials');
    expect(component.invalidLogin()).toBeFalse();
    expect(component.isLoading()).toBeFalse();
  });

  it('should validate form inputs', () => {
    const usernameControl = component.loginForm.get('username');
    const passwordControl = component.loginForm.get('password');

    // Initial state should be valid with default values
    expect(component.isFormValid()).toBeTrue();

    // Test empty values
    usernameControl?.setValue('');
    passwordControl?.setValue('');
    usernameControl?.markAsTouched();
    passwordControl?.markAsTouched();

    // Need to trigger change detection for signals to update
    fixture.detectChanges();

    expect(component.isFormValid()).toBeFalse();
    expect(component.usernameError()).toBeTruthy();
    expect(component.passwordError()).toBeTruthy();

    // Test too short values
    usernameControl?.setValue('ab');
    passwordControl?.setValue('xy');

    fixture.detectChanges();

    expect(component.isFormValid()).toBeFalse();
    expect(component.usernameError()).toBeTruthy();
    expect(component.passwordError()).toBeTruthy();

    // Test valid values
    usernameControl?.setValue('testuser');
    passwordControl?.setValue('testpass');
    usernameControl?.markAsUntouched();
    passwordControl?.markAsUntouched();

    fixture.detectChanges();

    expect(component.isFormValid()).toBeTrue();
    expect(component.usernameError()).toBeNull();
    expect(component.passwordError()).toBeNull();
  });

  it('should handle successful hardcoded authentication', () => {
    spyOn(hardcodedAuthService, 'authenticate').and.returnValue(true);
    spyOn(router, 'navigate');

    component.loginForm.patchValue({
      username: 'testuser',
      password: 'testpass'
    });

    component.handleLogin();

    expect(component.invalidLogin()).toBeFalse();
    expect(router.navigate).toHaveBeenCalledWith(['welcome', 'testuser']);
  });

  it('should handle failed hardcoded authentication', () => {
    spyOn(hardcodedAuthService, 'authenticate').and.returnValue(false);
    spyOn(router, 'navigate');

    component.loginForm.patchValue({
      username: 'testuser',
      password: 'wrongpass'
    });

    component.handleLogin();

    expect(component.invalidLogin()).toBeTrue();
    expect(router.navigate).not.toHaveBeenCalled();
  });
  it('should not proceed with invalid form on hardcoded login', () => {
    spyOn(hardcodedAuthService, 'authenticate');
    spyOn(router, 'navigate');

    component.loginForm.patchValue({
      username: '',
      password: ''
    });

    // Mark the form controls as touched to trigger validation
    component.loginForm.get('username')?.markAsTouched();
    component.loginForm.get('password')?.markAsTouched();
    fixture.detectChanges();

    component.handleLogin();

    expect(hardcodedAuthService.authenticate).not.toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();
  });

  it('should handle successful basic auth login', () => {
    spyOn(router, 'navigate');

    component.loginForm.patchValue({
      username: 'testUser',
      password: 'testPass'
    });

    component.handleBasicAuthLogin();

    expect(component.isLoading()).toBeTrue();

    const req = httpTestingController.expectOne(`${API_URL}/pingWithAuthentication`);
    expect(req.request.headers.get('Authorization')).toBeDefined();
    req.flush({ message: 'Success' });

    expect(component.isLoading()).toBeFalse();
    expect(component.invalidLogin()).toBeFalse();
    expect(router.navigate).toHaveBeenCalledWith(['welcome', 'testUser']);
  });

  it('should handle failed basic auth login', () => {
    spyOn(router, 'navigate');
    spyOn(console, 'error');

    component.loginForm.patchValue({
      username: 'testUser',
      password: 'wrongPass'
    });

    component.handleBasicAuthLogin();

    expect(component.isLoading()).toBeTrue();

    const req = httpTestingController.expectOne(`${API_URL}/pingWithAuthentication`);
    const errorResponse = new ProgressEvent('error');
    req.error(errorResponse);

    expect(component.isLoading()).toBeFalse();
    expect(component.invalidLogin()).toBeTrue();
    expect(router.navigate).not.toHaveBeenCalled();
    expect(console.error).toHaveBeenCalled();
  });
  it('should not proceed with invalid form on basic auth login', () => {
    component.loginForm.patchValue({
      username: '',
      password: ''
    });

    // Mark the form controls as touched to trigger validation
    component.loginForm.get('username')?.markAsTouched();
    component.loginForm.get('password')?.markAsTouched();
    fixture.detectChanges();

    component.handleBasicAuthLogin();

    httpTestingController.expectNone(`${API_URL}/pingWithAuthentication`);
    expect(component.isLoading()).toBeFalse();
  });

  it('should set proper Authorization header in basic auth request', () => {
    component.loginForm.patchValue({
      username: 'testUser',
      password: 'testPass'
    });

    const expectedAuthHeader = 'Basic ' + window.btoa('testUser:testPass');

    component.handleBasicAuthLogin();

    const req = httpTestingController.expectOne(`${API_URL}/pingWithAuthentication`);
    expect(req.request.headers.get('Authorization')).toBe(expectedAuthHeader);
    req.flush({ message: 'Success' });
  });

  it('should clear invalid login state on successful auth', () => {
    component.invalidLogin.set(true);
    spyOn(router, 'navigate');

    component.loginForm.patchValue({
      username: 'testUser',
      password: 'testPass'
    });

    component.handleBasicAuthLogin();

    const req = httpTestingController.expectOne(`${API_URL}/pingWithAuthentication`);
    req.flush({ message: 'Success' });

    expect(component.invalidLogin()).toBeFalse();
  });

  it('should preserve error state on failed auth', () => {
    component.invalidLogin.set(true);
    spyOn(router, 'navigate');
    spyOn(console, 'error');

    component.loginForm.patchValue({
      username: 'testUser',
      password: 'wrongPass'
    });

    component.handleBasicAuthLogin();

    const req = httpTestingController.expectOne(`${API_URL}/pingWithAuthentication`);
    const errorResponse = new ProgressEvent('error');
    req.error(errorResponse);

    expect(component.invalidLogin()).toBeTrue();
  });

  it('should show loading state during authentication', () => {
    component.loginForm.patchValue({
      username: 'testUser',
      password: 'testPass'
    });

    expect(component.isLoading()).toBeFalse();

    component.handleBasicAuthLogin();

    expect(component.isLoading()).toBeTrue();

    const req = httpTestingController.expectOne(`${API_URL}/pingWithAuthentication`);
    req.flush({ message: 'Success' });

    expect(component.isLoading()).toBeFalse();
  });
  it('should compute form validity correctly', () => {
    // Default form should be valid
    expect(component.isFormValid()).toBeTrue();

    // Invalid form - short username
    component.loginForm.patchValue({
      username: 'ab',
      password: 'validpass'
    });
    fixture.detectChanges();
    expect(component.isFormValid()).toBeFalse();

    // Invalid form - short password
    component.loginForm.patchValue({
      username: 'validuser',
      password: 'ab'
    });
    fixture.detectChanges();
    expect(component.isFormValid()).toBeFalse();

    // Valid form
    component.loginForm.patchValue({
      username: 'validuser',
      password: 'validpass'
    });
    fixture.detectChanges();
    expect(component.isFormValid()).toBeTrue();
  });
});
