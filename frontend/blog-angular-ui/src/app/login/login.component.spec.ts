import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing';
import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import {FormsModule} from '@angular/forms';
import {provideRouter, Router} from '@angular/router';

import {LoginComponent} from './login.component';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {HardcodedAuthenticationService} from '../service/hardcoded-authentication.service';
import {BasicAuthenticationService} from '../service/basic-authentication.service';
import {API_URL} from '../app.constants';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let router: Router;
  let hardcodedAuthService: HardcodedAuthenticationService;
  let httpTestingController: HttpTestingController;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule, LoginComponent],
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
        const basicAuthService = TestBed.inject(BasicAuthenticationService);
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
  it('should initialize with default values', () => {
    expect(component.userName).toBe('username');
    expect(component.passWord).toBe('dummy');
    expect(component.errorMessage).toBe('Invalid Credentials');
    expect(component.invalidLogin).toBeFalse();
  });

  it('should handle successful hardcoded authentication', () => {
    spyOn(hardcodedAuthService, 'authenticate').and.returnValue(true);
    spyOn(router, 'navigate');

    component.handleLogin();

    expect(component.invalidLogin).toBeFalse();
    expect(router.navigate).toHaveBeenCalledWith(['welcome', component.userName]);
  });

  it('should handle failed hardcoded authentication', () => {
    spyOn(hardcodedAuthService, 'authenticate').and.returnValue(false);
    spyOn(router, 'navigate');

    component.handleLogin();

    expect(component.invalidLogin).toBeTrue();
    expect(router.navigate).not.toHaveBeenCalled();
  });

  it('should handle successful basic auth login', () => {
    spyOn(router, 'navigate');
    component.userName = 'testUser';
    component.passWord = 'testPass';

    component.handleBasicAuthLogin();

    const req = httpTestingController.expectOne(`${API_URL}/pingWithAuthentication`);
    expect(req.request.headers.get('Authorization')).toBeDefined();
    req.flush({message: 'Success'});

    expect(component.invalidLogin).toBeFalse();
    expect(router.navigate).toHaveBeenCalledWith(['welcome', component.userName]);
  });

  it('should handle failed basic auth login', () => {
    spyOn(router, 'navigate');
    spyOn(console, 'log');

    component.handleBasicAuthLogin();

    const req = httpTestingController.expectOne(`${API_URL}/pingWithAuthentication`);
    const errorResponse = new ProgressEvent('error');
    req.error(errorResponse);

    expect(component.invalidLogin).toBeTrue();
    expect(router.navigate).not.toHaveBeenCalled();
    expect(console.log).toHaveBeenCalled();
  });

  it('should set proper Authorization header in basic auth request', () => {
    component.userName = 'testUser';
    component.passWord = 'testPass';
    const expectedAuthHeader = 'Basic ' + window.btoa(component.userName + ':' + component.passWord);

    component.handleBasicAuthLogin();

    const req = httpTestingController.expectOne(`${API_URL}/pingWithAuthentication`);
    expect(req.request.headers.get('Authorization')).toBe(expectedAuthHeader);
    req.flush({message: 'Success'});
  });

  it('should clear invalid login state on successful auth', () => {
    component.invalidLogin = true;
    spyOn(router, 'navigate');

    component.handleBasicAuthLogin();

    const req = httpTestingController.expectOne(`${API_URL}/pingWithAuthentication`);
    req.flush({message: 'Success'});

    expect(component.invalidLogin).toBeFalse();
  });

  it('should preserve error state on failed auth', () => {
    component.invalidLogin = true;
    spyOn(router, 'navigate');
    spyOn(console, 'log');

    component.handleBasicAuthLogin();

    const req = httpTestingController.expectOne(`${API_URL}/pingWithAuthentication`);
    const errorResponse = new ProgressEvent('error');
    req.error(errorResponse);

    expect(component.invalidLogin).toBeTrue();
  });
});
