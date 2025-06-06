import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { waitForAsync, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';

import { LoginComponent } from './login.component';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { HardcodedAuthenticationService } from '../service/hardcoded-authentication.service';
import { BasicAuthenticationService } from '../service/basic-authentication.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let router: Router;
  let hardcodedAuthService: HardcodedAuthenticationService;
  let basicAuthService: BasicAuthenticationService;
  let httpTestingController: HttpTestingController;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [RouterTestingModule, FormsModule],
      providers: [
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting(),
        HardcodedAuthenticationService,
        BasicAuthenticationService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    hardcodedAuthService = TestBed.inject(HardcodedAuthenticationService);
    basicAuthService = TestBed.inject(BasicAuthenticationService);
    httpTestingController = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpTestingController.verify();
    sessionStorage.clear();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with default values', () => {
    expect(component.userName).toBe('username');
    expect(component.passWord).toBe('');
    expect(component.errorMessage).toBe('Invalid Credentials');
    expect(component.invalidLogin).toBeFalse();
  });

  it('should set invalidLogin to true when hardcoded authentication fails', () => {
    spyOn(hardcodedAuthService, 'authenticate').and.returnValue(false);
    spyOn(router, 'navigate');

    component.handleLogin();

    expect(component.invalidLogin).toBeTrue();
    expect(router.navigate).not.toHaveBeenCalled();
  });

  it('should navigate to welcome page when hardcoded authentication succeeds', () => {
    spyOn(hardcodedAuthService, 'authenticate').and.returnValue(true);
    spyOn(router, 'navigate');

    component.handleLogin();

    expect(component.invalidLogin).toBeFalse();
    expect(router.navigate).toHaveBeenCalledWith(['welcome', component.userName]);
  });

  it('should handle basic auth login success', () => {
    spyOn(router, 'navigate');
    const mockResponse = { message: 'Success' };

    component.handleBasicAuthLogin();

    const req = httpTestingController.expectOne(request => 
      request.url.includes('/pingWithAuthentication')
    );
    req.flush(mockResponse);

    expect(component.invalidLogin).toBeFalse();
    expect(router.navigate).toHaveBeenCalledWith(['welcome', component.userName]);
  });

  it('should handle basic auth login failure', () => {
    spyOn(router, 'navigate');
    spyOn(console, 'log'); // Prevent actual console logging

    component.handleBasicAuthLogin();

    const req = httpTestingController.expectOne(request => 
      request.url.includes('/pingWithAuthentication')
    );
    req.error(new ErrorEvent('Network error'));

    expect(component.invalidLogin).toBeTrue();
    expect(router.navigate).not.toHaveBeenCalled();
  });
});
