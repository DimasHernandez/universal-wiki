import { FormControl } from '@angular/forms';
import { Roles } from './enums';

export interface IRegisterForm {
  username: FormControl<string | null>;
  password: FormControl<string | null>;
  passwordConfirm: FormControl<string | null>;
  roles: FormControl<Roles | null>;
  isActivate: FormControl<boolean | null>;
}
