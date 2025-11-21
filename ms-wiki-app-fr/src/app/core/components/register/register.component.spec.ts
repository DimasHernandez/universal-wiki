import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { Roles } from '@core/interfaces/enums';
import { IRegisterRequest } from '@core/interfaces/requests/register.request';
import { AuthService } from '@core/services/auth.service';
import { SharedModule } from '@shared/shared.module';
import { of, throwError } from 'rxjs';
import { RegisterComponent } from './register.component';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceMock: AuthService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [ReactiveFormsModule, HttpClientModule, SharedModule],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    authServiceMock = TestBed.inject(AuthService);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('formDirty', () => {
    it('should be form dirty', () => {
      component.registerForm.reset();
      const usernameControl = component.registerForm.controls.username;

      component.formDirty();

      expect(usernameControl.touched).toBeTrue();
    });
  });

  describe('checkValidPassword', () => {
    it('should be check password and should be return true', () => {
      component.registerForm.setValue({
        username: 'pepe mal',
        password: 'pass3',
        passwordConfirm: 'pass3',
        role: Roles.ROLE_USER,
        isActivate: true,
      });
      component.registerForm.controls.passwordConfirm.markAsDirty();

      const result = component.checkValidPassword();

      expect(result).toBeTrue();
    });

    it('should be check password and should be return false', () => {
      component.registerForm.setValue({
        username: 'pepe mal',
        password: 'pass3',
        passwordConfirm: 'pass233',
        role: Roles.ROLE_USER,
        isActivate: true,
      });
      component.registerForm.controls.passwordConfirm.markAsDirty();

      const result = component.checkValidPassword();

      expect(result).toBeFalse();
    });
  });

  describe('onSubmit', () => {
    it('should register successfully', () => {
      const request: IRegisterRequest = {
        username: 'pepe mal',
        password: 'pass3',
        active: true,
        roles: ['ROLE_USER'],
      };
      //arrange
      component.registerForm.setValue({
        username: 'pepe mal',
        password: 'pass3',
        passwordConfirm: 'pass3',
        role: Roles.ROLE_USER,
        isActivate: true,
      });
      const registerSpy = spyOn(authServiceMock, 'register').and.returnValue(
        of('Register success')
      );

      //act
      component.onSubmit();

      //assert
      expect(registerSpy).toHaveBeenCalledOnceWith(request);
      expect(registerSpy.calls.count()).toEqual(1);
    });

    it('should be valid form', () => {
      component.registerForm.setValue({
        username: 'asd',
        password: 'aaa',
        passwordConfirm: 'asa',
        role: Roles.ROLE_USER,
        isActivate: true,
      });

      const componentSpy = spyOn(component, 'formDirty');

      component.onSubmit();
      expect(componentSpy).toHaveBeenCalled();
    });

    it('should register failure', () => {
      const request: IRegisterRequest = {
        username: 'pepe mal',
        password: 'pass3',
        active: true,
        roles: ['ROLE_USER'],
      };
      //arrange
      component.registerForm.setValue({
        username: 'pepe mal',
        password: 'pass3',
        passwordConfirm: 'pass3',
        role: Roles.ROLE_USER,
        isActivate: true,
      });
      const registerSpy = spyOn(authServiceMock, 'register').and.returnValue(
        throwError(() => new Error('something went wrong'))
      );

      //act
      component.onSubmit();

      //assert
      expect(registerSpy).toHaveBeenCalledOnceWith(request);
      expect(registerSpy.calls.count()).toEqual(1);
    });
  });
});
